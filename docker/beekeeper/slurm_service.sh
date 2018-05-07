#!/bin/bash

/etc/init.d/slurmctld start
/etc/init.d/slurmd start
/etc/init.d/munge start
ps
sinfo
squeue
java -jar varwatch-beekeeper-1.0-SNAPSHOT-jar-with-dependencies.jar  --config-path=config_worker.txt
sbatch /worker_slurm.sh

squeue
