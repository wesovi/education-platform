#!/bin/bash

username=$1
password=$2
firstName=$3
lastName=$4

body='{"email":"'$username'","password":"'$password'","firstName":"'$firstName'","lastName":"'$lastName'"}'

clear

curl \
    -X POST "http://127.0.0.1:8878/api/v1/students" \
    -i \
    -H "Content-Type:application/json" \
    -H "Accept:application/json" \
    -d $body

echo "\n"

