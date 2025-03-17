# script builds jars from source & deploys them into virtual containers
mvn clean package
docker compose up --build



