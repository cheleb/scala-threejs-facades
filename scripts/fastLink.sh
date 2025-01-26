#!/usr/bin/env bash

. ./scripts/env.sh

echo "Waiting for npm dev server to start."
until [ -e $NPM_DEV_STARTED ]; do
    echo -n "."
    sleep 2
done

echo "Waiting for $BUILD_ENV_FILE to be generated."
until [ -e $BUILD_ENV_FILE ]; do
    echo -n "."
    sleep 2
done

. $BUILD_ENV_FILE

filename=examples/demo/target/scala-$SCALA_VERSION/demo-fastopt/main.js

echo "Waiting for demo-fastopt/main.js to be compiled..."
until [ -e $filename ]; do
    sleep 1
    echo -n "."
done

echo "Watching demo-fastopt/main.js for changes..."

sleep 3

DEV=1 sbt '~demo/fastLinkJS'
