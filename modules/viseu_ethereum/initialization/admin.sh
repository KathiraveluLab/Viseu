echo "starting admin node..."

if [ -d "$1" ]; then
    rm -rf $1
fi


DATADIR="$1"

geth --datadir $DATADIR init genesis.json

geth --identity admin --http --http.port "8000" --http.corsdomain "*" --datadir $DATADIR --port "30303" --nodiscover --http.api "eth,net,web3,personal,miner,admin" --networkid 1900 --nat "any"

