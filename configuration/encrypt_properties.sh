#!/usr/bin/env bash

ENC_KEY=$1
PROP_ENV=$2

cd "$(dirname "$0")"

aws kms encrypt \
    --key-id ${ENC_KEY} \
    --plaintext fileb://plaintext/${PROP_ENV}.properties \
    --output text \
    --query CiphertextBlob | base64 --decode > src/main/resources/enc-${PROP_ENV}.properties
