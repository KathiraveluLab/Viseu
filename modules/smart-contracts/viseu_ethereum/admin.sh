echo "what node do you want to start? "

read node



geth --identity $node --rpc --rpcport "8000" --rpccorsdomain "*" --datadir $node --port "30303" --nodiscover --rpcapi "db,eth,net,web3,personal,miner,admin" --networkid 1900 --nat "any"

