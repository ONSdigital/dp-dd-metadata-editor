#!/usr/bin/env bash

mvn clean install

java $JAVA_OPTS \
    -Dpostgres.username=data_discovery \
    -Dpostgres.password=password \
    -Dpostgres.host=localhost \
    -Dpostgres.port=5432 \
    -jar target/dp-dd-metadata-editor-1.0-SNAPSHOT.jar
