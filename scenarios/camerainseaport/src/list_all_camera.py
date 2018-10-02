import requests
import sys
import os
import json
import pycurl
from urllib.parse import urlparse
'''
This shows a simple example of dealing with protocol interoperability with camera.

1)A customer searches for cameras in a location

'''
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('--provider_url', default='http://localhost:3000/camera', help='URL of the IoT Camera Provider')
parser.add_argument('--lon', default='108.1494449', help='longitude')
parser.add_argument('--lat', default='16.0723458', help='latitude')
parser.add_argument('--distance', default='10000', help='default in meters')

args = parser.parse_args()


'''
Using camera ID to look for the latest video
'''
def camera_data_handle(camera):
        
    print(camera)

# Search for cameras close to a location
##TODO check values
headers = {
    'Cache-Control': "no-cache"
}
url=args.provider_url+"/list"
response = requests.request("GET", url, headers=headers)
#print(response.text)
list_of_cameras =response.json()
for camera in list_of_cameras:
    camera_data_handle(camera)
