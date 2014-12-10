#!/bin/bash

clear

studentId=$1
firstName=$2
lastName=$3
bornDate=$4

body='{"firstName":"'$firstName'","lastName":"'$lastName'","bornDate":"'$bornDate'"}'

url="http://127.0.0.1:8878/api/v1/students/$studentId/person"

curl \
    -X PUT $url \
    -i \
    -H "Content-Type:application/json" \
    -H "Accept:application/json" \
    -d $body

echo "\n"

