#!/bin/bash

for i in {1..10} ; do
  DIR=/mnt/resource/pv${i}
  oc delete pv/pv${i}
  rm -rf /mnt/resource/pv${i}
done
