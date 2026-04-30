# Viseu Ethereum Module

This module handles the blockchain-based trust and lease management for Viseu using an Ethereum private network. It includes the smart contracts, API integration, and initialization scripts.

## Technologies Used
- Ethereum (Private Network)
- Go-Ethereum (GETH)
- Ethereum Brownie (for deployment)

## Setup
Geth is required for this module. You can install it and initialize the project dependencies by running the root-level setup script:
```bash
./setup.sh
```

## Modifying genesis.json
1. The `genesis.json` file (in `initialization/`) includes dummy accounts for testing.
2. To create a new account:
   ```bash
   geth --datadir node01/ account new
   ```
3. Copy the output address (without the `0x` prefix) and update `genesis.json` accordingly.
4. Run `admin.sh` to initialize and start the admin node.

## Node Management
### Creating a Worker Node
1. Copy `genesis.json` from the admin node to the worker node.
2. Run `./start.sh <nodename>`
3. Monitor logs in `node_log.out`.

### Connecting Nodes
1. Attach to the admin node: `geth attach http://127.0.0.1:<port>`
2. Get the enode: `admin.nodeInfo.enode`
3. Replace `127.0.0.1` with the actual IP of the admin machine.
4. On the worker node console, add the peer:
   ```javascript
   admin.addPeer("<enode_string>")
   ```
5. Confirm connection: `net.peerCount`
