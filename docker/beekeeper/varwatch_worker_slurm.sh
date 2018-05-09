#!/bin/bash
#SBATCH -t 1:00:00

echo "varwatch 2.0"
echo $2  #path to config file
echo $1 # path to worker jar
echo $3 #worker id
echo $PWD
java -jar $1 --config-path=$2 --worker-id=$3
echo "worker finished"