# Viseu Docker Deployment

This module provides the necessary configuration for containerized deployment and orchestration of Viseu nodes.

## Contents
- **Dockerfile**: Defines the environment for a Viseu node (Java, Python, Geth).
- **docker-compose.yml**: Orchestrates a local cluster of Viseu nodes for testing.
- **genesis.json**: The Ethereum genesis configuration used by the `viseu_ethereum` module.

## Usage
To build the Docker image:
```bash
docker build -t viseu-node .
```

To spin up a local network of nodes:
```bash
docker-compose up -d
```
