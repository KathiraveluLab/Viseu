#!/bin/bash

# Viseu Local Setup Script
# This script initializes the project from local sources.

set -e

echo "----------------------------------------------------"
echo "Initializing Viseu from local sources..."
echo "----------------------------------------------------"

# 0. Install Geth if missing
if ! command -v geth &> /dev/null; then
    echo "[0/3] Geth not found. Attempting to install..."
    if command -v apt-get &> /dev/null; then
        echo "Updating package list and adding Ethereum PPA..."
        sudo apt-get update && sudo apt-get install -y software-properties-common
        sudo add-apt-repository -y ppa:ethereum/ethereum
        sudo apt-get update
        sudo apt-get install -y ethereum
    else
        echo "[!] Error: apt-get not found. Cannot install Geth automatically."
        echo "Please install Geth manually: https://geth.ethereum.org/docs/install-and-build/installing-geth"
        exit 1
    fi
else
    echo "[0/3] Geth is already installed."
fi

# 1. Prepare Ethereum Initialization
# The Ethereum module scripts expect genesis.json in their directory.
if [ -f "modules/docker/genesis.json" ]; then
    echo "[1/3] Copying genesis.json to Ethereum module..."
    cp modules/docker/genesis.json modules/viseu_ethereum/initialization/
else
    echo "[!] Warning: modules/docker/genesis.json not found. Ethereum setup might fail."
fi

# 2. Build the Maven Project
echo "[2/3] Building Maven project (skipping tests for speed)..."
mvn clean install -DskipTests

echo "----------------------------------------------------"
echo "Setup complete! You can now start the services."
echo "Example: cd modules/viseu_ethereum/initialization && ./admin.sh <datadir>"
echo "----------------------------------------------------"
