#!/bin/sh
set -e
# check to see if protobuf folder is empty
if [ ! -d "$HOME/protobuf/lib" ]; then
  VER="3.4.0"
  wget "https://github.com/google/protobuf/archive/v${VER}.tar.gz"
  tar -xzvf "v${VER}.tar.gz"
  cd "protobuf-${VER}" && ./configure --prefix=$HOME/protobuf && make && make install
else
  echo "Using cached directory."
fi

