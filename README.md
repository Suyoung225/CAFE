# CAFE
- 메뉴 조회와 포인트로 주문 및 결제가 가능한 커피 주문 시스템입니다.
- 동시성 이슈, 데이터 일관성과 애플리케이션이 다수의 서버에서 동작하는 것을 고려하여 구현하였습니다. 
- 모든 기능에 대한 단위테스트를 작성하고 있습니다.

## 📣구현 기능
- 전체 메뉴 조회
- 지난 7일 간 인기메뉴 목록 조회
- 포인트 충전하기 
- 커피 주문 및 결제 
- 주문 내역을 데이터 수집 플랫폼으로 실시간 전송

## ⛓ERD
<details>
<summary><strong> OPEN </strong></summary>
<div markdown="1">       
</br>
<img width="759" alt="2023-01-22 (1)" src="https://user-images.githubusercontent.com/87157566/213910980-e5baf954-294f-495b-ade0-b68803465841.png">

</div>
</details>
</br>

### DDL

<details>
<summary><strong> OPEN </strong></summary>
<div markdown="1">       
</br>

````sql

CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb`;

CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nickname` VARCHAR(20) NOT NULL,
  `point` BIGINT NOT NULL,
  `created_time` TIMESTAMP(3) NULL,
  `modified_time` TIMESTAMP(3) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `mydb`.`point_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(50) NOT NULL,
  `point` BIGINT NOT NULL,
  `created_time` TIMESTAMP(3) NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_point_history_user_idx` (`user_id` ASC)) ;

CREATE TABLE IF NOT EXISTS `mydb`.`menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `price` BIGINT NOT NULL,
  `created_time` TIMESTAMP(3) NULL,
  `modified_time` TIMESTAMP(3) NULL,
  PRIMARY KEY (`id`));
  
  CREATE TABLE IF NOT EXISTS `mydb`.`orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount` BIGINT NOT NULL,
  `created_time` TIMESTAMP(3) NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `mydb`.`order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `price` BIGINT NOT NULL,
  `number` INT NOT NULL,
  `created_time` TIMESTAMP(3) NULL,
  `menu_id` BIGINT NOT NULL,
  `order_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_order_item_orders1_idx` (`order_id` ASC));


````
</div>
</details>
</br>

## 🧬API
https://transparent-overcoat-20e.notion.site/21f006da338c4ef59c27d45cc34e7171?v=83832c529a204a76b2e2994ebc2dc3f5

## 🕹 Tech Stack
<img src ="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/></a>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"></a>
<img src="https://img.shields.io/badge/JPA-999933?style=for-the-badge&logo=JPA&logoColor=white"></a>
<img src ="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/></a>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
<img src ="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/></a>


## 📌 설계 내용 및 이유
### 엔티티 설계
데이터 타입, fk

### 주문
cascade로 order_item까지 한 번에 저장

### 데이터 수집 플랫폼으로 데이터 전송 - SSE
- 서비스 요구사항: 단방향 통신만 필요하고, 이벤트(주문)가 발생했을 때만 데이터를 전송하고, 실시간으로 플랫폼에 데이터가 보내져야 한다. 
- SSE(Sever-Sent-Events) 는 이벤트가 서버에서 클라이언트 방향으로만 단방향 통신이며 HTTP 프로토콜만으로 사용이 가능하며, 클라이언트가 한 번 서버에 연결(구독)을 하면 주기적인 요청없이 서버에서 해당 클라이언트로 실시간으로 데이터를 보낼 수 있다.  
- 또한 Spring Framework 4.2부터 SSE 통신을 지원하는 SseEmitter 클래스가 생겨 spring에서 손쉽게 구현이 가능하여 SSE를 사용하여 구현하였다.

### 인기메뉴 조회
querydsl, order_item 테이블에서 dto로 인기메뉴 id와 주문량 select <br>
Redis Cache에 하루에 한 번 캐싱

### 테스트
controller, service, repository 단위테스트<br>
멀티쓰레드 동시성 테스트, 인기메뉴 조회 시간 변경

## 💡 문제해결 전략 및 분석
#### 주문 및 포인트 결제 시 동시성 제어 위해 Redisson 분산락 이용

#### 인기메뉴 캐시 업데이트 한 번만 일어나도록 스케줄러와 Redisson 분산락 이용

