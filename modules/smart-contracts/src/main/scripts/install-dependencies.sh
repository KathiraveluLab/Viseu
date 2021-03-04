#!/bin/bash
echo "Creating viseu-net directory"

mkdir viseu-net

echo "pulling resources"

curl -sSL https://bit.ly/2ysbOFE | bash -s

cp -r ./fabric-samples/test-network/* ./viseu-net/

cp -r ./fabric-samples/bin ./viseu-net/

cp -r ./fabric-samples/config ./viseu-net/

rm -rf ./fabric-samples/
pwd
rm ./viseu-net/network.sh

rm ./viseu-net/scripts/createChannel.sh

cp ./src/main/scripts/network.sh viseu-net/
cp ./src/main/scripts/createChannel.sh viseu-net/scripts/

chmod +x ./viseu-net/network.sh
chmod +x ./viseu-net/scripts/createChannel.sh

echo "finished building"
