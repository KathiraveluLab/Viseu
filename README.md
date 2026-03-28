# Viseu: <ins>V</ins>irtual <ins>I</ins>nternet <ins>S</ins>ervices at the <ins>E</ins>dge

Viseu is a blockchain-based decentralized framework designed to offer virtual services at the Internet-scale in an untrusted environment. It leverages Peer-to-Peer (P2P) networking, real-time resource monitoring, and hybrid cloud-edge orchestration to deliver high-availability, low-latency computational offloading.

---

## Key Features

### Decentralized Workflow Engine
A high-performance, DAG-based orchestration engine that manages complex task dependencies. It automatically identifies "ready" tasks and dispatches them to optimal peers across the network.

### Adaptive Latency-Aware Scheduler
An intelligent scheduling logic that scores peers based on:
- Real-time CPU and RAM load (via Java MXBean telemetry).
- Network Latency (integrated with RIPE Atlas measurements).
- Proof of Trust (PoT) reputation scores.

### Hybrid Cloud-Bursting Bridge
Ensures 99.9% availability by monitoring edge cluster congestion. If a task exceeds local latency thresholds or no suitable edge peer is available, the bridge seamlessly "bursts" the workload to the cloud.

### Real-time Resource Monitoring
Distributed telemetry gathering that captures actual hardware metrics. Heartbeats propagate node health across the P2P overlay, ensuring load-balanced task distribution.

### Benchmarking and Performance Suite
An automated testing suite to validate framework throughput and latency reduction.

---

## Architecture

The framework is organized into specialized modules:
- modules/overlay: Core P2P networking, OverlayManager (Singleton), and P2P protocol logic.
- modules/viseu_ethereum: Blockchain-based trust and lease management integration.
- modules/docker: Containerized deployment orchestration.

---

## Getting Started

### Prerequisites
- Java 8+ (Maven-based build)
- Python 3.x (Flask-based API and RIPE measurements)

### Build and Installation
```bash
# Initialize dependencies and smart contracts
./modules/viseu_ethereum/initialization/setup.sh

# Build the Maven project
mvn clean install
```

### Running Benchmarks
```bash
mvn exec:java -Dexec.mainClass="edu.emory.viseu.overlay.util.ViseuBenchmark"
```

## Citing Viseu

If you use Viseu in your research, please cite the below paper:

* Kathiravelu, P., Zaiman, Z., Gichoya, J., Veiga, L., & Banerjee, I. (2022). **Towards an internet-scale overlay network for latency-aware decentralized workflows at the edge.** Computer networks, 203, 108654.
