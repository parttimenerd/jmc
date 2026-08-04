#!/bin/sh -l
set -e

echo "======== Building p2 repo ==================="
cd releng/third-party
sh -c "mvn ${MAVENPARAMS} p2:site"
echo "======== Starting p2 repo ==================="
sh -c "nohup mvn ${MAVENPARAMS} jetty:run &"
echo "======== Waiting for p2 repo to become available ==================="
i=0
while [ $i -lt 60 ]; do
  STATUS=$(curl --silent --output /dev/null --write-out "%{http_code}" http://localhost:8080/site/content.jar 2>/dev/null || echo "000")
  echo "  attempt $i: HTTP $STATUS"
  if [ "$STATUS" = "200" ]; then
    break
  fi
  i=$((i+1))
  sleep 2
done
if [ $i -ge 60 ]; then
  echo "Timed out waiting for p2 site" >&2
  exit 1
fi
echo "======== Done ==============================="
