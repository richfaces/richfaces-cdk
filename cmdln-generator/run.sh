#!/bin/sh

TARGET=$(dirname $0)"/target"
UI=/home/lfryc/workspaces/richfaces/sandbox/hot-deployment/ui
java -cp $TARGET/classes:$TARGET/dependency:$UI/target/dependency org.richfaces.cdk.GenerateMain $*
