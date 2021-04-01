echo "starting admin node..."

geth --identity admin --rpc --rpcport "8000" --rpccorsdomain "*" --datadir admin/ --port "30303" --nodiscover --rpcapi "db,eth,net,web3,personal,miner,admin" --networkid 1900 --nat "any"

