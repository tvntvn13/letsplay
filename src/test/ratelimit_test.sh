#!/bin/bash

echo "sending 15 consecutive requests - some should fail"
sleep 1
for i in {1..15}; do
	curl --head --silent -k -X GET https://localhost:443/api/products | awk '/HTTP/{print $2}' &
done

wait
sleep 1
echo ""
echo "sending 15 requests in batches of 5 with 1 second wait between - should not fail"
sleep 1
for i in {1..5}; do
	curl --head --silent -k -X GET https://localhost:443/api/products | awk '/HTTP/{print $2}' &
done
sleep 1
for i in {1..5}; do
	curl --head --silent -k -X GET https://localhost:443/api/products | awk '/HTTP/{print $2}' &
done
sleep 1
for i in {1..5}; do
	curl --head --silent -k -X GET https://localhost:443/api/products | awk '/HTTP/{print $2}' &
done

wait
