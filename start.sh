# script builds jars from source & deploys them into virtual containers

mvn clean install

# to use existing containers, replace line below with: docker-compose up --no-recreate (for images: docker-compose up --no-build)
docker compose up --build



