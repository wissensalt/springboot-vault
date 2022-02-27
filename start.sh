#!/usr/bin/env bash

docker-compose down

docker-compose up --build -d vault

sh ./setup-vault.sh

./gradlew clean build

docker-compose up --build -d service

exit 1
