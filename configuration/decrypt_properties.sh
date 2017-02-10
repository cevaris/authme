#!/usr/bin/env bash

PROP_ENV=$1

cd "$(dirname "$0")"

aws kms decrypt \
    --ciphertext-blob fileb://src/main/resources/enc-${PROP_ENV}.properties \
    --output text \
    --query Plaintext | base64 --decode
