![](docker/images/varwatch_logo.png?raw=true)

# What VarWatch is

VarWatch is a tool for professional human geneticists, genetic counselors and researchers working with genetic data from patients. High-throughput sequencing is uncovering an increasing number of genomic variants with a suspected link to an observed clinical phenotype. However, substantiating the clinical relevance of these variants-of-unknown-significance (VUS) typically requires one or more independent observations. VarWatch offers a non-commercial platform where such VUS can be registered to be continuously monitored for similar findings in external databases or the VarWatch register itself. Potential matches to other case descriptions will be forwarded to the variant owners to aid in the finding of a diagnosis for the affected patient(s).

![](https://github.com/broderF/varwatch/blob/master/varwatch_gui_variant.png?raw=true)

# Install VarWatch

VarWatch is built and deployed by Docker, which is based on the idea of that you can package your code with dependencies into a deployable unit called a container.

## Prerequisites

- install docker and docker-compose: https://docs.docker.com/compose/install/
- install java8: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
- install maven: https://maven.apache.org/install.html

## Download project

- download the project with: ```git clone https://github.com/broderF/varwatch.git```

## Build Project

- jump into the varwatch folder: ```cd varwatch```
- build the varwatch modules and execute the tests: ```mvn install clean```
- build the varwatch service: ```mvn --also-make --projects varwatch-service install```

## Start VarWatch

- jump into the docker folder: ```cd docker```
- build the docker files: ```sudo docker-compose build```
- start all docker services: ```sudo docker compose up```
- Now you can use the varwatch website in your browser: ```localhost:5000```

## Comments 

Path to the database: ```/tmp/mysql_data```
Path to the worker log: ```/tmp/worker_data```

Release VarWatch: ```sudo docker-compose down```
Rebuild: ```sudo docker-compose down && sudo docker-compose build --force-rm && sudo docker-compose up```

If you want to throw away the current database you have to delete the ```/tmp/mysql_data``` and ```/tmp/worker_data``` folder.

Access the database: ```mysql -h 127.0.0.1 -P 3306 -u demo -pDemo```

Backup the database:

Restore the database: 

Inspect a docker container: 

## Configuration

### Database configuration

If you want to change the database connection you have to change the parameter for the service and the parameter for the worker and beekeeper. In order to do this change the property db in the file varwatch-service/src/main/webapp/WEB-INF/classes/server.properties and in the file docker/beekeeper/config_worker.txt. Both values should always be the same.

The database is stored persistently on the host server in the folder '/tmp/mysql_data'. To change the path open the docker-compose.yml file and change the volumes configuration from the vardb service. 
