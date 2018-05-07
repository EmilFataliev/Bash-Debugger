#!/usr/bin/env bash
set -o xtrace
trap 'read' DEBUG
index=0
while [ "$index" -lt 10 ]
do
 echo "index: $index"
   let "index += 1"
done