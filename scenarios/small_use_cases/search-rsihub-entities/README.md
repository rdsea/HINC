# Develop Interoperability Solutions - Protocol Interoperability with Camera Data

We show one example of protocol interoperability with camera data

## Camera Data

In this example, we assume that we know there is an IoTCameraProvider which offers access to many cameras (as resources), each camera has its own capabilities. We have one example of such IoTCamera in (https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/IoTCameraDataProvider)

### For deployment

### For this tutorial

We deploy one IoTCameraProvider in Google. Check with the organizer to obtain the URL.

## Developers/User Story

### Want to obtain data directly.

In this situation: assume that the camera provider has APIs for obtain the camera. Check the IoTCameraProvider code to see how.


### The data should be pushed to a specific place.

Due to some specific conditions, we need pull-push w.r.t protocol to act as a bridge that asks the IoTCameraProvider to provide data and send to a specific location. Solutions:

* Develop own code
* Find if there is a component doing this

#### Search component

By searching through rsiHub, we find a component doing this (software-artifact) that can be deployed. Such a component might be also deployed already so available as a resource so we could just use it.

#### Deploy the component

One example of such pull-push bridge is (https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/datastorageArtefact) for REST GET API to Google Storage.
One can do the self-deployment, ask a specific provider to do this or call rsiHub to create a docker for pull-push

```
$docker pull rdsea/http2datastorage
```

We have one deployment and the data will be stored into the bucket "userexchangedata"

#### Run an example

See some python examples in [rsiHub scenarios](https://github.com/SINCConcept/HINC/tree/master/scenarios/camerainseaport)

```
$python3 src/simple_find_and_push.py --provider_url http://104.155.93.219:3000/camera

{"name":"http://4co2.vp9.tv/chn/DNG57/v204117.ts","timestamp":"1537981980"}
We will call an external program to store
https://storage.googleapis.com/userexchangedata/1537956804387_v204117.ts

```



#### Notes
-why do we need it?
-search api
-preconditions
-how to run?
    - run ...
    - create docker swarm
    - run docker swarm deploy