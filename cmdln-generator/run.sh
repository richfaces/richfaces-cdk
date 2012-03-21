#!/bin/sh

TARGET=$(dirname $0)"/target"
java -jar $TARGET/cdk-cmdline-generator.jar
