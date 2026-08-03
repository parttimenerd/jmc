#!/bin/sh -l
set -e

echo "======== Building p2 repo ==================="
cd releng/third-party
sh -c "mvn ${MAVENPARAMS} p2:site"
echo "======== Starting p2 repo ==================="
sh -c "nohup mvn ${MAVENPARAMS} jetty:run &"
echo "======== Waiting for p2 repo to become available ==================="
until curl --silent --fail http://localhost:8080/site/content.jar > /dev/null 2>&1; do
  sleep 2
done
echo "======== Done ==============================="
