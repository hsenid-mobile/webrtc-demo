# WebRTC App

WebRTC Application for Demo.

## Build Requirement

* [ JDK 7 ](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
* [ Maven 3.0.5 ](http://mirror.nus.edu.sg/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.zip)
* [ Mysql 5.5.40](https://dev.mysql.com/downloads/mysql/5.5.html)
* [ Tomcat 7.0.42](https://tomcat.apache.org/download-70.cgi)


## Environment Setup

* [ Install and Setup JDK 7 ](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
* [  Install and Setup Maven 3 ](http://mirror.nus.edu.sg/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.zip)
* [  Install and Setup Mysql 5 ](https://dev.mysql.com/downloads/mysql/5.5.html)
* [  Install and Setup Tomcat 7 ](https://tomcat.apache.org/download-70.cgi)
* Create Mysql Database webrtc_app.
* Source webrtc-app/dump/webrtc_app_dump.sql

### How to Build

#### Building the WebRTC App

Execute the following from the parent level of the project

    mvn clean install

Webrtc-app WAR file can be found at

    webrtc-app/target/webrtc-app.war

## How to Deploy

#### WebRTC App  Setup

1) Deploy the WebRTC App WAR file on Apache Tomcat

2) Access the Web Page - http://localhost:8080/webrtc-app/


