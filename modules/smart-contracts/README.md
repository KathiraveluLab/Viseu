## Smart Contracts Module

This module contains the code to the Smart Contracts that will govern how nodes interact with Viseu. The contracts will include available assets and services and how a peer will access those assets and services. This module also includes how to create an admin node, a peer, and connect them all on an ethereum private blockchain network. Note: make sure your machines are running Ubunutu. 


## Technologies Used

- Ubuntu  
- Ethereum  
- Go-Ethereum (GETH)  
- Ethereum Brownie  

## Install GETH and Ethereum
1. `sudo add-apt-repository -y ppa:ethereum/ethereum`  
2. `sudo apt-get install ethereum`


## Creating a Node

 1. Create a directory for your node e.g. `mkdir node01`  
 2. Next cd into your node `cd node01`  
 3. Make sure your genesis.json file is modified (see modifying genesis.json section)
 4. run `geth --datadir <path to node dir> init genesis.json`  
 5. Next switch the `cmd.sh` file to reflect the name and filepath of your intialized node  
 6. Run `./cmd.sh`
 7. Your node should be started
 
## Modifying genesis.json
1. Included in the `genesis.json` file are dummy accounts with wei for testing with unique account identifier numbers
2. These account identifiers must be changed to reflect your current network iteration's account
3. To create a new account run `geth --datadir <path to admin node> account new`
4. Set and memorize your password
5. Copy and paste the output hash without the hexadecimal prefix into the genesis.json file instead of the given account hash

## Connecting Multiple Nodes  
 
 1. In a new terminal window on the machine of arbitrarily selected admin node run `geth attach http://127.0.0.1:<port of node>`  
 2. In the GETH console run `admin.nodeInfo.enode`  
 3. Copy the full output and change the localhost IP address from `127.0.0.1` to either the private IP address of your admin node's machine or its public IP address  
 4. Run `geth attach http://127.0.0.1:<port of node>` to interact with your other nodes
 5. In each terminal of your other nodes run `admin.addPeer(<enode of admin node with proper IP address>)`  
 6. Run `net.peerCount` on the GETH console of the admin node to confirm that all your started nodes are connected to the admin  
 
Now you can use your GETH console to interact with the nodes in your network, deploy smart contracts to your network using ethereum brownie, and transfer assets between the nodes in your network.
 


