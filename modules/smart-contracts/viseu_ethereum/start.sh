#!/bin/bash




if [ -d "$1" ]; then
    rm -rf $1
fi

GENESIS=./genesis.json
if [ ! -f "$GENESIS" ]; then
    echo "genesis.json does not exist" && exit
fi

mkdir $1

geth --datadir $1 init genesis.json

FILE1=./node_log.out
if [ -f "$FILE1" ]; then
    rm $FILE1
fi

fuser -k 30303/tcp
fuser -k 30323/tcp

nohup geth --identity $1 --rpc --rpcport "8000" --rpccorsdomain "*" --datadir $1 --port "30303" --nodiscover --rpcapi "db,eth,net,web3,personal,miner,admin" --networkid 1900 --nat "any" > node_log.out &
