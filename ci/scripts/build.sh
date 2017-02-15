#!/bin/bash -eux

pushd dp-dd-metadata-editor
  mvn -U clean package -DskipTests=true
popd

cp -r dp-dd-metadata-editor/target/* target/
