#!/bin/bash

clear
username=$1
password=$2

body='{"username":"'$username'","password":"'$password'"}'

curl \
    -X POST "http://127.0.0.1:8878/api/v1/students/authentication" \
    -i \
    -H "Content-Type:application/json" \
    -H "Accept:application/json" \
    -d $body

echo "\n"

