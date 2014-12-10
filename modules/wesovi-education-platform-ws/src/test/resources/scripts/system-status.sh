#!/bin/bash
curl \
    -X GET "http://127.0.0.1:8878/api/v1/system/status" \
    -i
echo "\n"