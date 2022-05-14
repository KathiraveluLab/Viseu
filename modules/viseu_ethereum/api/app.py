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

app = Flask(__name__)
app.secret_key = 'secret'

@app.route('/isAlive')
def index():
    return "true"

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
   
if __name__ == '__main__':
    build_imagenet()


    app.config['ALLOWED_EXTENSIONS'] = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])
    app.run(port=5001,host='0.0.0.0')        
    #app.run(debug=True)