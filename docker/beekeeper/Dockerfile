FROM ubuntu:16.04

RUN apt-get update && apt-get install -y locales && rm -rf /var/lib/apt/lists/* \
    && localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8
ENV LANG en_US.utf8

RUN apt-get update && apt-get install -y \
    munge \
    libmunge2 \
    slurm-wlm \
    slurm-wlm-basic-plugins \
    default-jre

COPY ./slurm.conf /etc/slurm-llnl/slurm.conf

#RUN chown root /var/log/slurm-llnl
#RUN chown root /var/lib/slurm-llnl/slurmctld
#RUN chown root /var/run/slurm-llnl
RUN mkdir log
COPY images images 
COPY ./slurm_service.sh slurm_service.sh
COPY ./varwatch-beekeeper-1.0-SNAPSHOT-jar-with-dependencies.jar varwatch-beekeeper-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY ./ancestors_path_newHpo.txt ancestors_path_newHpo.txt
COPY ./varwatch-worker-1.0-SNAPSHOT-jar-with-dependencies.jar varwatch-worker-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY config_worker.txt config_worker.txt
COPY varwatch_worker_slurm.sh varwatch_worker_slurm.sh

CMD /slurm_service.sh