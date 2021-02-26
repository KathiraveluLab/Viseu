
## Prequisites

  Ensure you have docker and docker compose installed

## Smart Contracts Module

This module contains the code to the Smart Contracts that will govern how nodes interact with Viseu. The contracts will include available assets and services and how a peer will access those assets and services.

## Build Status

 - Deployed dummy smart contract and asset
 - Still developing Bootstrap.java 

## Technologies Used

- Java
- Apache Maven
- IBM Hyperledger Fabric
- Docker

## Installation Instructions

 1. Run `git clone https://github.com/Emory-HITI/Viseu.git` to clone this repository.
 2. Next cd into the smart-contracts module `cd module/smart-contracts/`
 3. Install necessary dependencies `mvn clean install`
 4. Bootstrap the network `javac Bootstrap.java` ->  `java Bootstrap` 

## Viseu-Network
The viseu-network directory is a modified version of the hyperledger favric test network which can be used for proof of concept. To instantiate the network from the smart contracts module: 
- `cd viseu-network` 
- `./network.sh up createChannel`

A local hyperledger fabric is now started on your machine.

