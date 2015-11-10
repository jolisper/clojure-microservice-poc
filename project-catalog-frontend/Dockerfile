FROM nginx

COPY nginx.conf /etc/nginx/nginx.conf

COPY docker-entrypoint.sh /entrypoint.sh

CMD ["nginx", "-g", "daemon off;"]

ENTRYPOINT ["/entrypoint.sh"]
