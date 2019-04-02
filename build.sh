#!/usr/bin/env bash

if ! [[ -x "$(command -v docker)" ]]; then
  echo 'Error: docker is not installed.' >&2
  exit 1
fi

docker build -t text-editor .