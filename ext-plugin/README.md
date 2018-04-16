# HINC Adaptors

These adaptors are responsible for interfacing with different providers to retrieve resource and provider information as well as send controls to the providers to provision/configure resources

# How to write

If you want to write an adaptor, please refer to the existing adaptors as a referece. An adaptor should communicate with a Local Management Service through AMQP. For information about the message types 
take a look at the sinc.hinc.common.communication package.
