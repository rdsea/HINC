## Dummy provider and adaptor

This package contains two parts:

1. DummProvider: 
  1. Generates a set of random data points (sensors)
  2. Provides REST interface to query such sensors
  3. Provides API to set the changing rate of the resources (how many sensors appear and disapear in a time)
2. Adaptor for DummyProvider: intergrate information into HINC.

 
The dummy provider uses memory-based storage, that means new set of resources will be generated when restarting.

This package requires no settings in source.conf.