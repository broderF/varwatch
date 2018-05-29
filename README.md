# varwatch

# Download project

- download the project with: git clone https://github.com/broderF/varwatch.git

# Building Project

- jump into the varwatch folder
- mvn install clean

# Starting VarWatch

- jump into the docker folder
- docker-compose build
- docker compose up

Now you can use the varwatch website in your browser: localhost:5000

Path to the database /tmp/mysql_data
Path to the worker log /tmp/worker_data

Release VarWatch: docker-compose down
Rebuild: docker-compose down && docker-compose build --force-rm && docker-compose up

If you want to throw away the current database you have to delete the /tmp/mysql_data and /tmp/worker_data folder.

Access the database: mysql -h 127.0.0.1 -P 3306 -u demo -pDemo

Backup the database:

Restore the database: 
