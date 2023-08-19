#!/bin/bash

for i in {1..5}; do
	curl --head --silent -X GET http://localhost:8080/api/products | awk '/HTTP/{print $2}' &
done
sleep 1
for i in {1..5}; do
	curl --head --silent -X GET http://localhost:8080/api/products | awk '/HTTP/{print $2}' &
done
sleep 1
for i in {1..5}; do
	curl --head --silent -X GET http://localhost:8080/api/products | awk '/HTTP/{print $2}' &
done

wait
