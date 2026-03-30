from flask import Flask
from flask import request
from flask import Flask, flash, request, redirect, url_for
from werkzeug.utils import secure_filename
import pandas as pd
from tensorflow.keras.models import load_model
from tensorflow.keras.applications.densenet import DenseNet121, decode_predictions, preprocess_input
import pickle
import numpy as np
from PIL import Image
from flask import jsonify
import ast
import os
import cv2
import io

def build_imagenet():

    model = DenseNet121()
    model.save_weights('imagenet.hdf5')

    # with open('densenet121_imagenet.pkl', 'wb') as f:
    #     pickle.dump(model, f)
#todo
def build_ct_classifier():

    pass

from measurement_client import MeasurementClient

app = Flask(__name__)
app.secret_key = 'secret'
measurement_client = MeasurementClient()

@app.route('/isAlive')
def index():
    return "true"

@app.route('/viseu/api/latency', methods=['POST'])
def measure_latency():
    data = request.get_json()
    target_ip = data.get('target_ip')
    if not target_ip:
        return jsonify({"error": "target_ip is required"}), 400
    
    result = measurement_client.measure_latency(target_ip)
    return jsonify(result)

@app.route('/viseu/api/latency/<measurement_id>', methods=['GET'])
def get_latency_result(measurement_id):
    result = measurement_client.get_measurement_result(measurement_id)
    return jsonify(result)

@app.route('/viseu/api/imagenet', methods=['POST'])
def get_prediction():

    if request.method == 'POST':
        # check if the post request has the file part
        if 'file' not in request.files:
            flash('No file part')
            return 'failed'
        file = request.files['file']
        # if user does not select file, browser also
        # submit an empty part without filename
        if file.filename == '':
            flash('No selected file')
            return redirect(request.url)
        if file:
            filename = secure_filename(file.filename)
            
            img = np.array(Image.open(file))
            img = cv2.resize(img, (224, 224))
            img = np.expand_dims(img, 0)

            loaded_model = DenseNet121()
            prediction = loaded_model.predict(preprocess_input(img))
            return jsonify({"result" : str(decode_predictions(prediction))})
try:
    from web3 import Web3
    from dotenv import load_dotenv
    DEPENDENCIES_OK = True
except ImportError:
    DEPENDENCIES_OK = False
    print("Warning: [web3] or [python-dotenv] not installed. Blockchain synchronization will be disabled.")

# Load blockchain configuration from .env
if DEPENDENCIES_OK:
    load_dotenv()

ETH_PROVIDER_URL = os.getenv('ETH_PROVIDER_URL', 'http://localhost:8000')
ETH_PRIVATE_KEY = os.getenv('ETH_PRIVATE_KEY')
ETH_CONTRACT_ADDRESS = os.getenv('ETH_CONTRACT_ADDRESS')

# Initialize Web3
w3 = None
if DEPENDENCIES_OK:
    try:
        w3 = Web3(Web3.HTTPProvider(ETH_PROVIDER_URL))
    except Exception as e:
        print(f"Warning: Failed to connect to Web3 provider: {str(e)}")

# Minimal ABI for ViseuPoT contract (as per Step 1141)
VISEU_POT_ABI = [
    {
        "inputs": [
            {"internalType": "address", "name": "_peer", "type": "address"},
            {"internalType": "uint256", "name": "_newReputation", "type": "uint256"}
        ],
        "name": "updateReputation",
        "outputs": [],
        "stateMutability": "nonpayable",
        "type": "function"
    },
    {
        "inputs": [{"internalType": "address", "name": "_peer", "type": "address"}],
        "name": "calculateTrust",
        "outputs": [{"internalType": "uint256", "name": "", "type": "uint256"}],
        "stateMutability": "view",
        "type": "function"
    }
]

def get_contract():
    if not DEPENDENCIES_OK or w3 is None or not ETH_CONTRACT_ADDRESS:
        return None
    try:
        return w3.eth.contract(address=Web3.to_checksum_address(ETH_CONTRACT_ADDRESS), abi=VISEU_POT_ABI)
    except Exception:
        return None

@app.route('/viseu/api/reputation', methods=['POST'])
def update_reputation():
    """
    Full implementation of reputation synchronization with the ViseuPoT smart contract.
    """
    if not DEPENDENCIES_OK or w3 is None:
        return jsonify({"status": "error", "message": "Blockchain dependencies (web3/dotenv) missing"}), 501

    data = request.get_json()
    peer_address = data.get('peer_id')
    new_reputation = data.get('new_reputation')
    
    if not peer_address or new_reputation is None:
        return jsonify({"error": "peer_id (address) and new_reputation are required"}), 400

    if not ETH_PRIVATE_KEY or not ETH_CONTRACT_ADDRESS:
        return jsonify({"error": "Blockchain configuration (Private Key/Contract) missing in .env"}), 500

    try:
        contract = get_contract()
        if contract is None:
             return jsonify({"error": "Failed to initialize ViseuPoT contract instance"}), 500

        account = w3.eth.account.from_key(ETH_PRIVATE_KEY)
        
        # Scale reputation (double 0.1-10.0 -> uint256 for PoT contract)
        scaled_reputation = int(float(new_reputation) * 100)

        # Build transaction logic
        nonce = w3.eth.get_transaction_count(account.address)
        txn = contract.functions.updateReputation(
            Web3.to_checksum_address(peer_address), 
            scaled_reputation
        ).build_transaction({
            'chainId': 1900, # Viseu Network ID
            'gas': 200000,
            'gasPrice': w3.eth.gas_price,
            'nonce': nonce,
        })

        # Sign and send transaction
        signed_txn = w3.eth.account.sign_transaction(txn, private_key=ETH_PRIVATE_KEY)
        tx_hash = w3.eth.send_raw_transaction(signed_txn.raw_transaction)

        print(f"Blockchain Sync: Updated {peer_address} to {new_reputation}. TX: {tx_hash.hex()}")
        return jsonify({
            "status": "success",
            "transaction_hash": tx_hash.hex(),
            "peer_address": peer_address,
            "scaled_reputation": scaled_reputation,
            "blockchain_status": "synced_and_anchored"
        })

    except Exception as e:
        print(f"Blockchain Error: {str(e)}")
        return jsonify({"status": "error", "message": str(e)}), 500

if __name__ == '__main__':
    build_imagenet()


    app.config['ALLOWED_EXTENSIONS'] = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])
    app.run(port=5001,host='0.0.0.0')        
    #app.run(debug=True)