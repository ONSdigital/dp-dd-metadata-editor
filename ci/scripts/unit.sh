#!/bin/bash -eux

pushd dp-dd-metadata-editor
  mvn clean surefire:test
popd
