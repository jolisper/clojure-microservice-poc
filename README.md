# Clojure Microservice POC

This project is a simple dockerized REST API written in Clojure.

    .--------------.      .--------------.      .--------------.
    |              |      |              |      |              |
    |  Front-End   |      |     API      |      |      DB      |
    |   (nginx)    |----->|  (Clojure)   |----->|    (Mongo)   |
    |              |      |              |      |              |
    '--------------'      '--------------'      '--------------'

## Up and Running

```
$ git clone https://github.com/jolisper/clojure-microservice-poc.git
```

```
$ cd clojure-microservice-poc
```

```
$ bash build-images.sh
```

```
$ docker-compose up
```

## Test API

```
$ curl -X POST -H "Content-Type: application/json" -H "X-Catalog-Token: o brave new world" -d '{ "project-name": "Posted project", "description": "Is only the beginning"}' 'http://localhost/projects' && echo
```

```
$ curl -X GET -H "Content-Type: application/json" -H "X-Catalog-Token: o brave new world" 'http://localhost/projects' && echo
```

Or use the Postman (3.1.5) collection in tools/postman. 

## Requirements

This POC requires Docker 1.9.0+ and Docker Compose 1.4.2+, please refer to the following links for installation instructions:

https://docs.docker.com/linux/step_one/

https://docs.docker.com/compose/install/

