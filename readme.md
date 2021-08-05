## Build docker image
```docker build -t asterle/backend-docker .```

## Run docker container
```docker run -p 8080:8080 --rm --name backend asterle/backend-docker```