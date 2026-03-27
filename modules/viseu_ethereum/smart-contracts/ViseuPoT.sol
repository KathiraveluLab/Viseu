// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * @title ViseuPoT
 * @dev Implementation of the Proof of Trust (PoT) logic for the Viseu framework.
 * Integrates Proof of Stake (Wealth) with historical Reputation.
 */
contract ViseuPoT {
    struct PeerTrust {
        uint256 stake;
        uint256 reputation;
        uint256 lastUpdate;
        bool exists;
    }

    mapping(address => PeerTrust) public registry;
    address public admin;

    event TrustUpdated(address indexed peer, uint256 trustScore);
    event ReputationChanged(address indexed peer, uint256 newReputation);

    constructor() {
        admin = msg.sender;
    }

    modifier onlyAdmin() {
        require(msg.sender == admin, "Only admin can perform this action");
        _;
    }

    /**
     * @dev Add or update stake for a peer (Proof of Stake component).
     */
    function depositStake() public payable {
        registry[msg.sender].stake += msg.value;
        registry[msg.sender].exists = true;
        registry[msg.sender].lastUpdate = block.timestamp;
        
        emit TrustUpdated(msg.sender, calculateTrust(msg.sender));
    }

    /**
     * @dev Update reputation for a peer. In production, this should be called by the LeaseManager.
     */
    function updateReputation(address _peer, uint256 _newReputation) public onlyAdmin {
        registry[_peer].reputation = _newReputation;
        registry[_peer].lastUpdate = block.timestamp;
        
        emit ReputationChanged(_peer, _newReputation);
        emit TrustUpdated(_peer, calculateTrust(_peer));
    }

    /**
     * @dev Calculate the normalized Trust Score.
     * T = (Stake * Reputation) / normalization_factor
     */
    function calculateTrust(address _peer) public view returns (uint256) {
        PeerTrust storage peer = registry[_peer];
        if (!peer.exists) return 0;

        // Simplified time-weighted trust calculation
        // In a full implementation, this involves time integrals as per the Viseu paper.
        return (peer.stake * (peer.reputation + 1)) / 1e18; 
    }
}
