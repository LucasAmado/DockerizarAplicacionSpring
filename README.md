# DockerizarAplicacionSpring - Contraoferta

 ## Contenedor e imagen de postgres

 En este caso vamos a crear un contenedor de postgres con la versión 10, que nos servirá para alojar la base de datos. Pero para eso primero necesitamos descargar la imagen de postgres:

 ```
 docker pull postgres:10
 ```
 
 Creamos el contenedor:

 ```
 docker run --name contraoferta -p 5432:5432 -e POSTGRES_PASSWORD=postgresql -d postgres:10
 ```
 
 Para las ocasiones futuras la forma de arrancar el conetenedor será con el siguiente comando:

 ```
 docker start contraoferta
 ```

## Añadir postgres al proyecto 
 
Dentro del archivo application.properties debemos añadir lo siguiente:
 ```
server.port=9000

# URL jdbc de conexión a la base de datos
# cambiar la ip por la de tu ordenador
spring.datasource.url=jdbc:postgresql://192.168.1.1:5432/postgres

spring.datasource.username=postgres

spring.datasource.password=postgresql

# Le indicamos a JPA/Hibernate que se encargue de generar el DDL
spring.jpa.hibernate.ddl-auto=create-drop

# Habilitamos la consola de H2
# http://localhost:{server.port}/h2-console
# En nuestro caso http://localhost:9000/h2-console
spring.h2.console.enabled=true

# Habilitamos los mensajes sql en el log
spring.jpa.show-sql=true


spring.thymeleaf.cache=false


spring.datasource.initialization-mode=always


spring.datasource.tomcat.connection-properties=useUnicode=true;characterEncoding=utf-8;

spring.profiles.active=prod
 ```

 No podemos olvidar irnos al fichero pom.xml y añadir la dependencia de postgres
 ```
 <dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
</dependency>
 ```

## Fichero .jar 
 
Lo siguiente será hacer un maven clean sobre el proyecto. Después ejecutaremos un maven install, pero antes hay que asegurarse de que en JRE System Library tenemos un jdk.

En caso de no tener alguna versión de jdk haz click derecho sobre la carpeta, properties > alternative JRE > installed JRE > Search > Disco Local (C:) > Archivos de Programa (x86) > Java > Jdk
 
Dentro de target crearemos la carpeta dependency. Después entraremos en ella desde la terminal y ejecutaremos el siguiente comando:
 ```
 jar -xf ../*.jar
 ```

## Dockerfile
 
 Deebemos crear el siguiente archivo Dockerfile:
 ```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.salesianostriana.dam.proyectocontraoferta.ProyectoContraofertaApplication"]
 ```
 
Creamos la imagen
 ```
 docker build -t user/contraoferta .
 ```
 
 ## Docker-Compose

 Vamos a crear un archivo docker-compose.yml en la raíz del proyecto para poder enlazar la aplicación de spring con postgres.
``` 
version: '2'
services:
    app:
        build: .
        image: user/contraoferta
        ports: 
            - "9000:9000"
        depends_on: 
            - db
        environment: 
            - SPRING_PROFILES_ACTIVE=prod
    
    db:
        image: postgres:10
        ports: 
            - "5432:5432"
        environment: 
            - POSTGRES_PASSWORD=postgresql
            - POSTGRES_USER=postgres
            - POSTGRES_DB=postgres
 ```

Ahora podemos volver al archivo application.properties y modicarlo:
 
 ```
 spring.datasource.url=jdbc:postgresql:db:5432/postgres
 ```
 
Por último habría que ejecutar el siguiente comando:
```
docker-compose up -d
```