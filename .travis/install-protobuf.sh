#!/bin/sh
set -e
# check to see if protobuf folder is empty
if [ ! -d "$HOME/protobuf/bin" ]; then
  VER="3.4.0"
  wget "https://github.com/google/protobuf/releases/download/v3.4.0/protoc-${VER}-linux-x86_64.zip"
  unzip -d "${HOME}/protobuf" "protoc-${VER}-linux-x86_64.zip"
else
  echo "Using cached directory."
fi

