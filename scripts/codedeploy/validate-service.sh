#!/bin/bash

if [[ $(docker inspect --format="{{ .State.Running }}" dp-dd-metadata-editor) == "false" ]]; then
  exit 1;
fi
