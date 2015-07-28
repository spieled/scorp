#!/bin/bash
docker rmi docker.56qq.cn:5000/tomcat7.0.63:v2015
docker build --no-cache -t "docker.56qq.cn:5000/tomcat7.0.63:v2015" .
docker push docker.56qq.cn:5000/tomcat7.0.63:v2015
