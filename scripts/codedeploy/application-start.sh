#!/bin/bash

AWS_REGION=
CONFIG_BUCKET=
ECR_REPOSITORY_URI=
GIT_COMMIT=

INSTANCE=$(curl -s http://instance-data/latest/meta-data/instance-id)
CONFIG=$(aws --region $AWS_REGION ec2 describe-tags --filters "Name=resource-id,Values=$INSTANCE" "Name=key,Values=Configuration" --output text | awk '{print $5}')

CONFIG_DIRECTORY=publishing

(aws s3 cp s3://$CONFIG_BUCKET/dp-dd-metadata-editor/$CONFIG_DIRECTORY/$CONFIG.asc . && gpg --decrypt $CONFIG.asc > $CONFIG) || exit $?

source $CONFIG && docker run -d         \
  --env=DB_DRIVER=$DB_DRIVER            \
  --env=DB_PASSWORD=$DB_PASSWORD        \
  --env=DB_URL=$DB_URL                  \
  --env=DB_USER=$DB_USER                \
  --env=SERVER_PORT=$SERVER_PORT        \
  --name=dp-dd-metadata-editor \
  --net=$DOCKER_NETWORK                 \
  --restart=always                      \
  $ECR_REPOSITORY_URI/dp-dd-metadata-editor:$GIT_COMMIT
