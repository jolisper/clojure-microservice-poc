#!/usr/bin/env bash

docker build -t project-catalog-frontend project-catalog-frontend/

docker build -t project-catalog-api project-catalog-api/

docker build -t project-catalog-db project-catalog-db/
