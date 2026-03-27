// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./ViseuRegistry.sol";

/**
 * @title ViseuLeaseManager
 * @dev Manages service leases and QoS-aware payments for Viseu.
 */
contract ViseuLeaseManager {
    enum LeaseStatus { ACTIVE, COMPLETED, VIOLATED, CANCELLED }

    struct Lease {
        uint256 id;
        uint256 serviceId;
        address consumer;
        uint256 startTime;
        uint256 duration;
        uint256 amount;
        LeaseStatus status;
    }

    ViseuRegistry public registry;
    uint256 public nextLeaseId;
    mapping(uint256 => Lease) public leases;

    event LeaseCreated(uint256 indexed leaseId, uint256 indexed serviceId, address indexed consumer);
    event LeaseClosed(uint256 indexed leaseId, LeaseStatus status);

    constructor(address _registryAddress) {
        registry = ViseuRegistry(_registryAddress);
    }

    /**
     * @dev Create a new lease for a service.
     */
    function createLease(uint256 _serviceId, uint256 _duration) public payable returns (uint256) {
        // Retrieve service details from registry
        (,,, address provider, uint256 price, bool active) = registry.services(_serviceId);
        
        require(active, "Service is not active");
        require(msg.value >= price, "Insufficient payment");

        uint256 leaseId = nextLeaseId++;
        
        leases[leaseId] = Lease({
            id: leaseId,
            serviceId: _serviceId,
            consumer: msg.sender,
            startTime: block.timestamp,
            duration: _duration,
            amount: msg.value,
            status: LeaseStatus.ACTIVE
        });

        emit LeaseCreated(leaseId, _serviceId, msg.sender);
        return leaseId;
    }

    /**
     * @dev Mark lease as completed and release funds to provider.
     * In a production scenario, this could be triggered by an oracle or a mutual agreement (reputation-based).
     */
    function completeLease(uint256 _leaseId) public {
        Lease storage lease = leases[_leaseId];
        require(lease.consumer == msg.sender, "Only consumer can sign off");
        require(lease.status == LeaseStatus.ACTIVE, "Lease is not active");

        lease.status = LeaseStatus.COMPLETED;
        
        (,,, address provider,,) = registry.services(lease.serviceId);
        payable(provider).transfer(lease.amount);

        emit LeaseClosed(_leaseId, LeaseStatus.COMPLETED);
    }

    /**
     * @dev Report SLA violation.
     */
    function reportViolation(uint256 _leaseId) public {
        Lease storage lease = leases[_leaseId];
        require(lease.consumer == msg.sender, "Only consumer can report violations");
        require(lease.status == LeaseStatus.ACTIVE, "Lease is not active");

        lease.status = LeaseStatus.VIOLATED;
        // In this implementation, violated funds stay in contract for arbitration or partial refund logic.
        
        emit LeaseClosed(_leaseId, LeaseStatus.VIOLATED);
    }
}
