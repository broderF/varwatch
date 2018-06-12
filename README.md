# Install VarWatch

## Prerequisites

- install docker,docker-compose, maven and java8

## Download project

- download the project with: ```git clone https://github.com/broderF/varwatch.git```

## Build Project

- jump into the varwatch folder: ```cd varwatch```
- build the varwatch modules and execute the tests: ```sudo mvn install clean```

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
