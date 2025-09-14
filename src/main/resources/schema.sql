DROP DATABASE if EXISTS `food_store`;

CREATE DATABASE `food_store` 40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;

DROP TABLE if EXISTS `food_category`;

CREATE TABLE `food_category` (
  `food_category_id` int(12) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(254) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_id` int(4) DEFAULT NULL,
  `created_by` varchar(48) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` timestamp NOT NULL,
  `updated_by` varchar(48) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_on` timestamp DEFAULT NULL,
  `is_active` boolean DEFAULT false,
  PRIMARY KEY (`food_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

