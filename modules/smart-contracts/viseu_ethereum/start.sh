#!/bin/bash

echo "What node is this?"
echo "Type in the form 'node<number>' where number is of the form 0<number> if num<10"
read nodenum


if [ -d "$nodenum" ]; then
    rm -rf $FILE
fi

GENESIS=./genesis.json
if [ ! -f "$GENESIS" ]; then
    echo "genesis.json does not exist" && exit
fi

mkdir $nodenum

geth --datadir $nodenum init genesis.json

FILE1=./node_log.out
if [ -f "$FILE1" ]; then
    rm $FILE1
fi

fuser -k 30303/tcp
fuser -k 30323/tcp

nohup ./cmd.sh > node_log.out &
