# LottoGame - backend

## Frontend
https://github.com/karolkraw/LottoGame-frontend

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Specification](#specification)
* [Screenshots](#screenshots)
* [Setup](#setup)
* [REST-API Endpoints](#rest-api-endpoints)




## General Information
LottoGame is an implementation of the well-known Lotto Lottery. 
The user provides six distinct numbers from range 1-99 and receives ticket with unique ID and draw date.
Results can be checked right after the draw date.
The winning numbers are generated based on configured scheduler that runs every Saturday at 12 p.m.
The user can become a lottery winner if he hit at least three numbers.


## Specification

- Java with Spring Boot for Backend
- Angular for Frontend
- Modular monolith hexagonal architecture with one module extracted as microservice
- Netflix-Eureka server used as discovery service
- Spring Data MongoDB used for storing tickets and lottery results
- Facade design pattern
- Unit Tests with Junit 5 and Mockito
- Integration Tests with TestContainers, TestRestTemplate and WireMock
- All modules containerized in Docker
- Spring Boot Scheduler used for generating winning numbers



## Technologies Used

Backend: <br>
<img src="https://img.shields.io/badge/17-Java-orange?style=for-the-badge"> &nbsp;
<img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot"> &nbsp;
<img src="https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white"> &nbsp;
<img src="https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"> &nbsp;
</h2>

Frontend:<br>
<img src="https://img.shields.io/badge/angular-%23DD0031.svg?style=for-the-badge&logo=angular&logoColor=white"> &nbsp;
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> &nbsp;
<img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"> &nbsp;

Tests:<br>
<img src="https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"> &nbsp;
<img src="https://img.shields.io/badge/Mockito-78A641?style=for-the-badge"> &nbsp;
<img src="https://img.shields.io/badge/Testcontainers-9B489A?style=for-the-badge"> &nbsp;

DevOps: <br>
<img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white"> &nbsp;



## Screenshots

![2023-02-09 19_29_50-Settings](https://user-images.githubusercontent.com/71384877/217914830-7e68a821-6164-41ae-ad3a-7f2a4d02f39d.png)
![mainPageLotto](https://user-images.githubusercontent.com/71384877/217914971-7aa9893f-4130-499e-965e-1f1fad5c98ba.png)
![2023-02-09 19_48_05-Settings](https://user-images.githubusercontent.com/71384877/217914840-417cc7ea-dfe8-4301-b7e9-06f1ecaf8a64.png)
![2023-02-09 20_20_24-lotto png ‎- lost](https://user-images.githubusercontent.com/71384877/217915957-231117c9-6d2a-4f7c-9860-3abe4d0508bd.png)


## Setup

### Requirements:
- Docker
- Two files placed in the same folder: **[docker-compose.yml](https://github.com/kalqa/LottoExcelentKarol/blob/master/docker-compose.yml)** 
and **[init-mongo.js](https://github.com/kalqa/LottoExcelentKarol/blob/master/init-mongo.js)** <br>

### To run the application:
- Just execute the following command in the folder mentioned above <br>
``
docker compose up
`` (all images will be pulled from Docker Hub and the application will start)


## Rest-API Endpoints

Service url: http://localhost:9090

| HTTP METHOD | Endpoint           | Action                                   |
|-------------|--------------------|------------------------------------------|
| POST        | /numbers           | To input 6 distinct numbers              |
| GET         | /announcement/{id} | To retrieve lottery results for given ID |
