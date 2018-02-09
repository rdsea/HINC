from flask import Flask
from flask import jsonify
import os
import socket


app = Flask(__name__)

@app.route("/")
def hello():
   

    data = {"data": "testdata1", "data2": "testdata2", "data3": 3}

    return jsonify(data);
    

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=80)
