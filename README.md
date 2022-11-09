# KAFKA TRAINING

## Tabla de contenidos
1. [¿Qué es Kafka training?](#qué-es-kafka-training)
2. [Configuracion cluster kafka confluent](#configuracion-cluster-kafka-confluent)
3. [Configuracion proyecto Springboot](#configuracion-proyecto-springboot)
4. [Variables de entorno](#variables-de-entorno)
5. [Clases configuracion proyecto](#clases-configuracion-proyecto)
7. [Testear funcionalidades](#testear-funcionalidades)
8. [Kafka basic](#kafka-basic)
9. [Kafka filter](#kafka-filter)

## Qué es kafka training
Proyecto de pruebas en Springboot para probar kafka y todas sus funcionalidades
DISCLAIMER: La finalidad de este proyecto es SOLO trabajar con las funcionalidades de kafka y Spring. 
No se aplican buenas prácticas, patrones, arquitecturas limpias, testing, etc

## Configuracion cluster kafka confluent
Para poder trabajar con el proyecto tenemos que levantar con docker un cluster de kafka confluent.
En el siguiente enlace seguir los pasos para ello: https://docs.confluent.io/platform/current/platform-quickstart.html#quick-start-for-cp

## Configuracion proyecto Springboot
Este proyecto se ha realizado en Java 17 y maven.
Se recomienda el IDE intelliJ para desarrollar.
Los pasos para comenzar a trabajar en local son los siguientes:
- Descargar el proyecto del repositorio
- Abrir el proyecto con intelliJ, configurar el IDE para que utilice JDK 17 y ejecutar clean install desde la pestaña maven o mvn clean install desde la consola
- En la configuración de Spring boot añadir las variables de entorno del siguiente apartado y arrancar el proyecto pulsando Run o Debug

## Variables de entorno
``
kafka.groupId=test-training;kafka.server=localhost:9092,localhost:9101
``

## Clases configuracion proyecto
Dentro del paquete config se encuentran todas las clases de configuracion necesarias para kafka: consumidores, productores, etc

## Testear funcionalidades
Dentro de resources se incluye el proyecto de postman para probar las funcionalidades. Solo hay que importar el fichero en un postman

## Kafka basic
Módulo para probar un productor y un consumidor en Kafka
Mediante un API Rest enviamos un mensaje y un topic que se publica. Tras esto un consumidor lo lee

## Kafka filter
Lee mensajes filtrados