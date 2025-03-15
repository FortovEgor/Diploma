# script builds jars from source & deploys in into virtual containers
mvn clean package
docker compose up --build
