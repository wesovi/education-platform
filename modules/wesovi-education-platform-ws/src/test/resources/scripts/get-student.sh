#!/bin/bash

clear
studentId=$1

url="http://127.0.0.1:8878/api/v1/students/$studentId"

curl \
    -X GET $url \
    -i \
    -H "Accept:application/json" \

echo "\n"

