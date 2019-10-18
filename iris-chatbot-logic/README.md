#Backend

To start the Docker as demon
docker run -d --net=host -p 127.0.0.1:8080:8080 iris/backend:latest

To start the docker on foreground
docker run --net=host -p 127.0.0.1:8080:8080 iris/backend:latest