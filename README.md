# CAFE
카페 키오스크 구현 프로젝트

### ERD
<details>
<summary><strong> OPEN </strong></summary>
<div markdown="1">       
</br>

![2023-01-17](https://user-images.githubusercontent.com/87157566/212794387-6dc89f44-ec7d-48fb-b86e-4f0c8d1e4e24.png)

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
  `created_time` TIMESTAMP NULL,
  `modified_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `mydb`.`point_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(50) NOT NULL,
  `point` BIGINT NOT NULL,
  `created_time` TIMESTAMP NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_point_history_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_point_history_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `mydb`.`menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `price` BIGINT NOT NULL,
  `created_time` TIMESTAMP NULL,
  `modified_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`));
  
  CREATE TABLE IF NOT EXISTS `mydb`.`orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount` BIGINT NOT NULL,
  `created_time` TIMESTAMP NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `mydb`.`order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `price` BIGINT NOT NULL,
  `number` INT NOT NULL,
  `created_time` TIMESTAMP NULL,
  `menu_id` BIGINT NOT NULL,
  `order_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_order_item_orders1_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_item_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `mydb`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


````
</div>
</details>
</br>

### API
https://transparent-overcoat-20e.notion.site/21f006da338c4ef59c27d45cc34e7171?v=83832c529a204a76b2e2994ebc2dc3f5

