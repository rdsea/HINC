# Testbed

Simulate a large number of resources with many 
HINC local managers and 1 DummyProvider

On one machine, we have

 - One DummyProvider generate random metadata of temperature sensors. There is only metadata, no real or emulated data is created.
 - Many HINC local daemons, query one time the DummyProvider to get data.

This setup is suitable to test performance and communication between HINC local and HINC global.
The performance inside HINC local is minor because provider actively update information to HINC.

# How to use




