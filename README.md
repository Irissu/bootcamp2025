# Proyecto catálogo de peliculas

## Backend
El backend se encuentra en la carpeta catalogo-microservicios. Para levantar la base de datos es necesario tener un contenedor mysql en el puerto 3306
Podemos crear el contenedor a través de Podman con el siguiente comando:

```podman run -d --name mysql-sakila -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 jamarton/mysql-sakila```


## Frontend
La parte frontend principal se encuentra en la carpeta sakila. Para levantarla debémos hacerlo a través del proxy con el siguiente comando:

```ng serve --proxy-config proxy.conf.json```

