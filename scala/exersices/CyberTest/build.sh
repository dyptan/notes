#!/bin/bash
mvn clean package -DskipTests
docker build . --rm -t diptan/cybertest:latest
docker push diptan/cybertest