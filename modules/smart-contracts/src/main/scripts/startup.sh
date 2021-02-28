#!/bin/bash

echo "========Starting Network========="
# shellcheck disable=SC2164

cd ../../../viseu-net/ ; ./network.sh up createChannel

echo "========Stopping Network========="

./network.sh down

