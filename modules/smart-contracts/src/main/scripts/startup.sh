#!/bin/bash

echo "========Starting Network========="


#start network
source ../../../viseu-net/network.sh up createChannel
wait

#install chaincode



echo "========Stopping Network========="

./network.sh down

