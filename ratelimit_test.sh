#!/bin/bash

for i in {1..20}; do
	curl --head --silent -X GET http://localhost:8080/api/products | awk '/HTTP/{print $2}' &
	# http --headers :8080/api/products &
done

wait
