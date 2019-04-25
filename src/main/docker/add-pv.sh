#!/bin/bash

for i in {1..10} ; do
DIR=/mnt/resource/pv${i}
mkdir -p $DIR
chmod 777 $DIR
chcon -R unconfined_u:object_r:svirt_sandbox_file_t:s0 $DIR
cat <<EOF | oc create -f - --as=system:admin
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv${i}
spec:
  accessModes:
  - ReadWriteOnce
  capacity:
    storage: 3Gi
  hostPath:
    path: /mnt/resource/pv${i}
  persistentVolumeReclaimPolicy: Retain
EOF
done
