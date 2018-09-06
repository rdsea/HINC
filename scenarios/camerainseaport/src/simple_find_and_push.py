import requests
import sys
import os
import json
import pycurl
from urllib.parse import urlparse
'''
This shows a simple example of dealing with protocol interoperability with camera.

1)A customer searches for cameras in a location
2)The consumers finds latest video from cameras
3)The consumers faces two situations:
- if the service understands the destination given by the consumer, e.g.,
Google Storage, the consumer might ask the service to push the data to the Google.
- if the service cannnot deal with that but provides the URL so the consumer can call
an external bridge do the pull and push protocol interoperability pattern.
 One example of the bridge is https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/datastorageArtefact
'''
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('--provider_url', default='http://localhost:3000/camera', help='URL of the IoT Camera Provider')
parser.add_argument('--lon', default='108.1494449', help='longitude')
parser.add_argument('--lat', default='16.0723458', help='latitude')
parser.add_argument('--distance', default='10000', help='default in meters')
parser.add_argument('--bridge_url',default='http://daredevil.infosys.tuwien.ac.at:8085/datastorageArtefact/dataurl',help='URL of an interoperability bridge')
parser.add_argument('--user_email',help='Given an user emails indicate that the customer wants to use service pull_push')
args = parser.parse_args()

def get_filename(source_url):
    #create local file name
    url_detail = urlparse(source_url)
    filename=os.path.basename(url_detail.path)
    return filename

def service_pull_push (video, camera_id, user_email, cameraprovider_url):
    print("The IoT Camera Provider will do the data transfer")
    payload = {"videoName":source_url,"email":user_email}
    headers = {
        'Cache-Control': "no-cache",
        'Content-Type': "application/json"
    }
    service_url =cameraprovider_url+"/"+camera_id
    response = requests.request("POST", service_url, data=json.dumps(payload), headers=headers)
    print(response.text)


def external_pull_push(source_url,timestamp,bridge_url):
    print("We will call an external program to store ",)
    payload = {"dataurl":source_url}
    headers = {
        'Cache-Control': "no-cache",
        'Content-Type': "application/json"
    }
    response = requests.request("POST", bridge_url, data=json.dumps(payload), headers=headers)
    print(response.text)

def external_pull_local(source_url, timestamp):
    #create local file name
    filename=get_filename(source_url)
    #we assume we store in the current directory
    local_filename=timestamp+"_"+filename
    fp = open(local_filename, "wb")
    curl = pycurl.Curl()
    ##one can optimize parameter
    curl.setopt(pycurl.URL, source_url)
    curl.setopt(pycurl.WRITEDATA, fp)
    try:
        curl.perform()
    except:
        print("Some error")
    curl.close()
    fp.close()

'''
Using camera ID to look for the latest video
'''
def camera_data_handle(camera):
    #find the latest video
    url = args.provider_url+"/"+camera['id']+"/list/now"
    headers = {
    'Cache-Control': "no-cache"
    }
    response = requests.request("GET", url, headers=headers)
    '''
    Get url and timestamp
    '''
    print(response.text)
    #should check response
    url =response.json()['name']
    timestamp=response.json()['timestamp']

    '''
    Here we assume that the consumer knows its destination and also know the
    service capabilities so we just do a simple check

    if the consumer gives a google email, then the service can push the video
    to Google Storage of the user.

    if not, an external program will be executed.

    '''
    ''' What does it mean our support for IoT Interoperability
        The key point is that based on various metadata, the consumer can decide to do
        one of the following tasks
    '''
    ## Random choice is just for demonstration
    ## CASE 1:
    try:
        external_pull_local(url,timestamp)
    except:
        print("Exception in pull_local")

    ### CASE 2: here one also uses our rsi to find suitable bridge
    ### If the bridge is not available, one can deploy on-demand, we dont show here
    try:
        external_pull_push(url,timestamp,args.bridge_url)
    except:
        print("Exception in pull_push with an external bridge")
    ### CASE 3: Other choice
    try:
        if (args.user_email != None):
            service_pull_push(get_filename(url),camera['id'],args.user_email, args.provider_url)
    except:
        print("exception with service_push")


# Search for cameras close to a location
##TODO check values
querystring = {"lon":args.lon,"lat":args.lat,"distance":args.distance}

headers = {
    'Cache-Control': "no-cache"
}
url=args.provider_url+"/list/location"
response = requests.request("GET", url, headers=headers, params=querystring)
#print(response.text)
list_of_cameras =response.json()
for camera in list_of_cameras:
    camera_data_handle(camera)
