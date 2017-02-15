#!/bin/bash -eux

pushd dp-dd-metadata-editor
  mvn -U clean package -Dmaven.test.skip
popd

cp -r dp-dd-metadata-editor/target/* target/
