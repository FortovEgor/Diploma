# script builds jars from source & deploys them into virtual containers
mvn clean install
docker compose up --build



