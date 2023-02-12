# ☕ CAFE
메뉴 조회와 포인트로 주문 및 결제가 가능한 커피 주문 시스템입니다. <br>

동시성 이슈, 데이터 일관성과 애플리케이션이 다수의 서버에서 동작하는 것을 고려하여 구현하였습니다. <br>

모든 기능에 대한 단위 테스트를 작성하였습니다.


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

## 🧬API
[API 명세서](https://transparent-overcoat-20e.notion.site/21f006da338c4ef59c27d45cc34e7171?v=83832c529a204a76b2e2994ebc2dc3f5)

<br>

## 🕹 Tech Stack
<img src ="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/></a>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"></a>
<img src="https://img.shields.io/badge/JPA-999933?style=for-the-badge&logo=JPA&logoColor=white"></a>
<img src ="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/></a>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
<img src ="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/></a>

- JAVA 11
- Spring Boot 2.7.7
- QueryDSL 5.0.0
- JPA
- JUnit5
- MySQL 8.0.31
- Redis 3.0.504
- Redisson 3.19.1 

<br>


## 📌 설계 내용 및 이유

### ✨ 엔티티 설계
- Entity
  - **포인트 이력 (point_history)**: enum 클래스로 충전 혹은 결제 타입을 저장하고, user 테이블과 다대일 연관관계를 맺었습니다. 사용자가 포인트를 이용해 결제를 하기 때문에 충전, 사용 기록을 저장하는 테이블을 만들어 데이터 일관성을 유지하였습니다
  - **제품별 주문(order_item)**: 메뉴 가격이 변경될 수 있으니 메뉴 id, 해당 메뉴 주문 수량과 결제한 메뉴 가격을 모두 저장하였고, 주문 테이블과 다대일 양방향 연관관계를 맺었습니다.
  - **주문(order)**: 주문한 사용자 id, 총 주문 가격, 주문 시간을 담은 테이블입니다. <br>

- Data Type
  - **TIMESTAMP**: DATETIME보다 저장공간을 더 적게 차지하며, 여러 타임존에서도 같은 시간을 보장하기 때문에 선택하였습니다.
  - **BIGINT**: 포인트, 가격, id는 모두 int 범위를 넘을 가능성이 조금이나마 있으므로 BIGINT로 설정하였고, int 범위를 벗어날 가능성이 없는 주문 수량만 INT로 데이터 타입을 설정하였습니다.
  - **VARCHAR**: 문자 길이를 제한하여 불필요한 메모리 낭비 방지를 위해 유저 닉네임, 포인트 이력 타입, 메뉴 이름 모두 필요한 만큼의 varchar 길이를 설정하였습니다.
  
<details>
<summary><strong> DDL </strong></summary>
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


### ✨ 인기메뉴 조회
- QueryDSL을 이용해 order_item 테이블과 menu 테이블을 innerjoin 하고 집계한 날 8일 전부터 전날까지의 주문 데이터에서 가장 주문량이 많은 세 가지 메뉴의 메뉴 id, 이름, 1주일 간의 주문량을 select합니다.  
<details>
<summary><strong> Code </strong></summary>
<div markdown="1">       
</br>

````java
public List<PopularMenuDto> popularMenus() {
    LocalDate weekBefore = LocalDate.now().minusDays(7);
    LocalDate yesterday = LocalDate.now();

    return queryFactory.select(Projections.constructor(PopularMenuDto.class,
                    orderItem.menuId, menu.name, orderItem.number.sum()))
            .from(orderItem)
            .innerJoin(menu).on(orderItem.menuId.eq(menu.id))
            .where(orderItem.createdTime.between(weekBefore.atStartOfDay(), yesterday.atStartOfDay()))
            .groupBy(orderItem.menuId)
            .orderBy(orderItem.number.sum().desc())
            .limit(3)
            .fetch();
}
````
</div>
</details>
</br>

- 하루에 한 번 밤 12시에 위와 같은 방법으로 연산한 주간 인기 메뉴 연산 결과를 **Redis에 캐시**로 저장합니다. 매일 주간 인기 메뉴를 업데이트 할 때 캐시가 비워지지 않게 하기 위해 캐시 유효기간(ttl)을 2일로 설정하였습니다. 
- **@Cacheable** 어노테이션을 사용하여 이용자가 인기 메뉴를 조회할 때 캐싱된 데이터를 리턴하며, Redis 서버가 작동하지 않거나 캐시가 유실됐을 경우에는 다시 위와 같은 연산을 하여 이용자에게 리턴하고 그 결과를 Redis에 캐시로 저장합니다.

<br>

### ✨ 데이터 수집 플랫폼으로 데이터 전송
- 서비스 요구사항: 단방향 통신만 필요하고, 이벤트(주문)가 발생하고 성공했을 때만 데이터를 전송하고, 실시간으로 플랫폼에 데이터가 보내져야 합니다. 
- **Event listener**를 사용해 주문 **트랜잭션이 성공한 뒤**에 **비동기**로 주문 데이터를 전송하는 로직 구현
<details>
<summary><strong> Code </strong></summary>
<div markdown="1">       
</br>

````java
// OrderEventListener
@Async
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handle(OrderService.OrderEvent event) {
    dataTransferService.sendOrderData(event.getOrderData()); 
}


// OrderService
@Transactional
public OrderResponseDto orderMenu(Long userId, List<OrderDto> orderList) {

  ...
  orderRepository.save(order);

  eventPublisher.publishEvent(new OrderEvent(new OrderDataDto(order)));
  ...
}
  
public static class OrderEvent{
    @Getter
    private OrderDataDto orderData;

    public OrderEvent(OrderDataDto orderData) {
        this.orderData = orderData;
    }
}
````

[OrderService](https://github.com/Suyoung225/CAFE/blob/main/src/main/java/com/sy/cafe/service/OrderService.java) <br>
[OrderEventListener](https://github.com/Suyoung225/CAFE/blob/main/src/main/java/com/sy/cafe/service/OrderEventListener.java) <br>

</div>
</details>
</br>

- **SSE(Sever-Sent-Events)** 는 이벤트가 서버에서 클라이언트 방향으로만 **단방향 통신**이며 **HTTP 프로토콜**만으로 사용이 가능하며, 클라이언트가 한 번 서버에 연결(구독)을 하면 **주기적인 요청없이** 서버에서 해당 클라이언트로 **실시간**으로 데이터를 보낼 수 있습니다. 또한 **Spring Framework 4.2**부터 SSE 통신을 지원하는 **SseEmitter** 클래스가 생겨 Spring에서 손쉽게 구현이 가능하여 SSE를 사용하여 구현하였습니다.
- 클라이언트(데이터 수집 플랫폼)는 "/connect" url로 서버와 연결 요청을 보면 Timeout이 되는 시간까지 추가적인 요청 없이 주문 데이터를 실시간으로 수집할 수 있습니다.
- 어떤 플랫폼 서버에 연결되었는지 알기 위해 Emitter 정보를 저장하고 삭제해야하기 때문에 **Emitter Repository**를 추가적으로 구현하였습니다. 멀티쓰레드에서 동기화을 고려해 **ConcurrentHashMap**를 이용해 데이터 수집 플랫폼 이름과 생성 시간으로 구성된 Emitter id를 key, SseEmitter를 value로 emitter 정보를 저장하였습니다.

<details>
<summary><strong> Code </strong></summary>
<div markdown="1">       
</br>

[EmitterRepository](https://github.com/Suyoung225/CAFE/blob/main/src/main/java/com/sy/cafe/repository/EmitterRepository.java) <br>
[EmitterRepositoryImpl](https://github.com/Suyoung225/CAFE/blob/main/src/main/java/com/sy/cafe/repository/EmitterRepositoryImpl.java) <br>
[DataTransferService](https://github.com/Suyoung225/CAFE/blob/main/src/main/java/com/sy/cafe/service/DataTransferService.java) <br>

</div>
</details>
</br>

### ✨ 테스트
- **객체 지향적인 개발**과 **리팩토링의 용이성**을 위해 Controller, Service, Repository 코드에 대한 **기능별 단위테스트**를 모두 작성하였습니다.
- Redisson 분산락을 사용한 동시 충전, 주문 코드 테스트는 **멀티 쓰레드에서의 동시성 테스트**를 별도로 작성하였습니다.
- 주간 인기 메뉴 조회 Repository 테스트 시에는 **DateTimeProvider**와 **AuditingHandler**를 각각 **MockBean**과 **SpyBean**으로 주입 받아 현재 시간을 수정하여 조회 결과를 확인하였습니다.
- **테스트 profile**을 설정하여 테스트 DB는 local MySql의 테스트용 스키마를 사용하였고, Redis는 **Docker** 로 **테스트 컨테이너**를 생성하여 테스트하였습니다.


<br>

## 🎯 문제해결 전략 및 분석
### 💡 주문 및 포인트 충전 시 동시성 제어 위해 Redisson 분산락 이용
- 비즈니스 로직과 분산락 처리 로직의 **관심사 분리**, 주문와 포인트 충전 두 메서드에 같은 분산락을 적용해야 하기 때문에 **코드 재사용성**을 위해 **annotation 기반으로 구현**하였습니다. 
- 동시성 처리를 위해서는 락 획득 이후 트랜잭션이 시작되어야 하고, 커밋 후에 락이 해제되어야 하기 때문에 **락 내부에서 트랜잭션이 동작**하도록 구혔하였습니다.
- 주문과 포인트 충전 시 모두 유저의 포인트 컬럼에 영향을 주기 때문에 락의 **key를 유저 id**로 설정하여 동시에 주문과 포인트 충전 요청이 왔을 때도 **데이터 정합성**과 **순차적인 처리를 보장**하였습니다. 


<br>

### 💡 데이터 수집 플랫폼과 연결이 끊겼을 때 Event id를 이용해 미전송된 데이터 전송
- 클라이언트에 주문 데이터 전송 시 클라이언트 id와 데이터 전송 시간을 Event id로 만들어 데이터와 함께 전송합니다.
- 클라이언트가 미수신한 주문 목록이 존재할 경우 마지막으로 받은 **Event id를 헤더에 추가**하여 서버와 연결 요청을 보내면 Event id에 저장된 시간 이후에 생성된 주문 데이터를 모두 클라이언트에게 전송합니다.


<br>

### 💡 인기메뉴 캐시 업데이트 한 번만 일어나도록 스케줄러와 Redisson 분산락 이용
- 다중 서버에서는 모든 서버에서 스케줄러가 작동되어 Redis에 캐시를 저장하는 동작이 발생합니다. 따라서 **wait time을 0으로 설정한 분산락**을 사용하여 lock을 획득한 하나의 쓰레드에서만 캐시 저장 메서드를 수행하도록 하였습니다.

