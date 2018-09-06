# Detail Configuration for the scenario Camera in SeaPort

## Simple find and push

Assume that a consumer needs camera data, it searches the camera provider and gets required list of cameras. The Camera Provider offers a simple push of video to a global space. However, under different situations and different types of consumers, we could have different cases

- consumer pulls data and store the data locally: this might not cause any interoperability issue, except the network functions might prevent the customer takes the data

- consumer wants the service push the video to a space (e.g., google storage) of the consumer.

- consumer needs a bridge to pull and push data to deal with interoperability of protocol (also possible with data interoperability and service portability)

the src/simple_find_and_push.py shows the above-mentioned scenarios. The idea is that by providing different utilities and combine with searches and dynamic provisioning, bridges or service features can be used to support interoperability.
