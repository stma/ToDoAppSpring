#!/usr/bin/env bash

export JAVA_HOME=/home/stma/.jdks/openjdk-19.0.2/
./mvnw install

# ./gradlew build

docker build --build-arg JAR_FILE=target/*.jar -t progmatic/todo-app .

docker save -o target/todo-app.tar progmatic/todo-app

scp -o "User progmatic" .env progmatic-test:/home/progmatic/.todo-app.env
scp -o "User progmatic" target/todo-app.tar progmatic-test:/home/progmatic/
rm -f target/todo-app.tar

ssh -o "User progmatic" progmatic-test "docker rm -fv todo-app"
ssh -o "User progmatic" progmatic-test "docker load -i /home/progmatic/todo-app.tar; rm -f /home/progmatic/todo-app.tar;"
ssh -o "User progmatic" progmatic-test "docker run -d -p 8080:8080 --env-file /home/progmatic/.todo-app.env --name todo-app progmatic/todo-app"
