// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * @title ViseuRegistry
 * @dev Decentralized registry for edge node services in the Viseu framework.
 */
contract ViseuRegistry {
    struct Service {
        uint256 id;
        string name;
        string endpoint;
        address provider;
        uint256 price;
        bool active;
    }

    uint256 public nextServiceId;
    mapping(uint256 => Service) public services;
    mapping(address => uint256[]) public providerServices;

    event ServiceRegistered(uint256 indexed id, string name, address indexed provider);
    event ServiceUpdated(uint256 indexed id, bool active);

    /**
     * @dev Register a new edge service.
     */
    function registerService(string memory _name, string memory _endpoint, uint256 _price) public returns (uint256) {
        uint256 serviceId = nextServiceId++;
        
        services[serviceId] = Service({
            id: serviceId,
            name: _name,
            endpoint: _endpoint,
            provider: msg.sender,
            price: _price,
            active: true
        });

        providerServices[msg.sender].push(serviceId);

        emit ServiceRegistered(serviceId, _name, msg.sender);
        return serviceId;
    }

    /**
     * @dev Deactivate a service.
     */
    function setServiceStatus(uint256 _id, bool _active) public {
        require(services[_id].provider == msg.sender, "Only provider can update status");
        services[_id].active = _active;
        emit ServiceUpdated(_id, _active);
    }

    /**
     * @dev Get total services registered by a provider.
     */
    function getProviderServiceCount(address _provider) public view returns (uint256) {
        return providerServices[_provider].length;
    }
}
