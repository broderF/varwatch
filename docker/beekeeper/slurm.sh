#!/bin/bash
# file slurm_example.sh
# run with sbatch -o slurm-%A_%a.out slurm_example.sh
#SBATCH --job-name=slurm_example
#SBATCH --output=slurm_example.txt
#SBATCH --array=1-2000:20
#SBATCH --ntasks=20

echo "hallo"
