# Viseu Overlay Module

This is the core module of the Viseu framework, responsible for Peer-to-Peer (P2P) networking, resource monitoring, and task orchestration.

## Key Features
- **P2P Overlay**: Manages node discovery and communication across the decentralized network.
- **Resource Telemetry**: Gathers real-time hardware metrics (CPU, RAM) and network latency.
- **Workflow Engine**: A high-performance, DAG-based engine that manages complex task dependencies.
- **Adaptive Scheduler**: Scores and selects peers based on real-time load and Proof of Trust (PoT).

## Architecture
- **OverlayManager**: The central singleton that maintains the node's presence in the network.
- **WorkflowEngine**: Identifies "ready" tasks and dispatches them to optimal peers.
- **PeerRegistry**: Tracks peer health and reputation scores.

## Usage
This module is built as part of the main Maven project. You can run benchmarks using the following command from the root:
```bash
mvn exec:java -Dexec.mainClass="edu.emory.viseu.overlay.util.ViseuBenchmark"
```
