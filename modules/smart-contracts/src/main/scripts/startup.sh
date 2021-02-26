#!/usr/bin/sh

echo "========Starting Network========="
# shellcheck disable=SC2164
cd ../../../visue-net
./network.sh up createChannel

