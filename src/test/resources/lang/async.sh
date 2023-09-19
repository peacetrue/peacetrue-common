#!/bin/bash
for i in {1..5}; do
    sleep 1
    echo "read: $(cat async.log)"
done
