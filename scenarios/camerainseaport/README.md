# Detail Configuration for the scenario Camera in SeaPort

## Initial scenario

Initially, a seaport/city has an IoTCameraProvider which manages various cameras as resources. Each camera has its own way of storing its data.

### Setup

To setup this initial scenario we use the code from:

https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/IoTCameraDataProvider


A sample of dataset is available for the City of Da Nang. Pls. contact us if you want to see.

## Simple find and push

Assume that a consumer needs camera data, it searches the camera provider and gets required list of cameras. The Camera Provider offers a simple push of video to a global space. However, under different situations and different types of consumers, we could have different cases

- consumer pulls data and store the data locally: this might not cause any interoperability issue, except the network functions might prevent the customer takes the data

- consumer wants the service push the video to a space (e.g., google storage) of the consumer.

- consumer needs a bridge to pull and push data to deal with interoperability of protocol (also possible with data interoperability and service portability)

the src/simple_find_and_push.py shows the above-mentioned scenarios. The idea is that by providing different utilities and combine with searches and dynamic provisioning, bridges or service features can be used to support interoperability.

## Create slices for exchanging IoT data between platforms.

### Setting up rsiHub

### Slice --
