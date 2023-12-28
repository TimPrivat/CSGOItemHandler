#!/bin/bash
dockerID=$1
serverPort=$2
offset=$3
docker exec -it $dockerID /bin/bash -c \"curl -X POST 127.0.0.1:$serverPort/setIndex?index=$offset\"