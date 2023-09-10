#!/bin/bash
echo "preparing to send http requests.."
sleep 1
echo ""
echo "STATUS CODES:"
echo "200 - OK"
echo "429 - TOO MANY REQUESTS"
echo ""
sleep 2
echo "sending 100 consecutive requests - most should fail"
echo ""
sleep 1
for i in {1..100}; do
	curl --head --silent -k -X GET https://localhost:443/api/products | awk '/HTTP/{print $2}' &
done
sleep 1
echo ""

echo "sending 60 requests in batches of 20 with 1 second wait between - should not fail"
echo ""
sleep 1
for i in {1..20}; do
	curl --head --silent -k -X GET https://localhost:443/api/products | awk '/HTTP/{print $2}' &
done
sleep 1
for i in {1..20}; do
	curl --head --silent -k -X GET https://localhost:443/api/products | awk '/HTTP/{print $2}' &
done
sleep 1
for i in {1..20}; do
	curl --head --silent -k -X GET https://localhost:443/api/products | awk '/HTTP/{print $2}' &
done

wait
