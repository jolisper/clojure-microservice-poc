#!/bin/bash

sed -i "s/PROJECT_CATALOG_API_PORT/${PROJECT_CATALOG_API_PORT_5000_TCP_PORT}/" /etc/nginx/nginx.conf

exec "$@"
