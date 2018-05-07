# varwatch

# Download project

- download the project with: git clone https://github.com/broderF/varwatch.git

# Building Project

- jump into the varwatch folder
- mvn install clean

# Starting VarWatch

- copy beekeeper and worker into the docker/beekeeper folder
- copy service into the docker/data folder

- docker-compose build
- docker compose up

Now you can use the website localhost:5000

Path to the database /tmp/mysql_data
Path to the worker log /tmp/worker_data

Release VarWatch: docker-compose down
Rebuild everything: docker-compose down && docker-compose build --force-rm && docker-compose up
