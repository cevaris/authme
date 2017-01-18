#!/usr/bin/env bash


JAR_PATH=$1
if [ -z "$JAR_PATH" ]; then
    echo "missing jar path argument"
fi

AWS_PATH=authme
AWS_BUCKET_NAME=aws.lambda.jars
AWS_BUCKET=s3://${AWS_BUCKET_NAME}/${AWS_PATH}
AWS_JAR_PATH=$(basename ${JAR_PATH})
AWS_UPLOAD_PATH=${AWS_BUCKET}/${AWS_JAR_PATH}

if [ $(aws s3 ls ${AWS_BUCKET} | wc -l) = 0 ]; then
    echo "${AWS_BUCKET} does not exist, wrong account?"
    exit -1
fi


read -p "upload ${JAR_PATH} to ${AWS_UPLOAD_PATH} (y/N)? " CHOICE
CHOICE=$(echo "${CHOICE}" | tr '[:upper:]' '[:lower:]')
case "${CHOICE}" in
  y|yes)
    echo "starting upload..."
    aws s3 cp ${JAR_PATH} ${AWS_UPLOAD_PATH}
    echo "https://s3.amazonaws.com/${AWS_BUCKET_NAME}/${AWS_PATH}/${AWS_JAR_PATH}"
    echo "done uploading"
    ;;
  *)
    echo $CHOICE
    echo "choose not to upload, exiting..."
    exit -2
    ;;
esac
