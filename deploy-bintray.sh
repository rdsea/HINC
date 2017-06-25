#!/bin/bash

# deploy only needed artifact to bintray

cd global-management-service/
mvn deploy
cd ..

cd local-management-service/
mvn deploy
cd ..

cd ext-pom/DummyProvider/
mvn deploy
cd ../..

cd ext-pom/TEITPlugin/
mvn deploy
cd ../..
