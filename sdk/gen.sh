#!/bin/bash
if [ -d "src/main/java/cn/tangxinyao/thrift/sdk" ];
then
    rm -rf "src/main/java/cn/tangxinyao/thrift/sdk"
fi

thrift --out "./src/main/java" -r --gen java "./thrift/auth.thrift"