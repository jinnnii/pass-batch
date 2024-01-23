-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        8.0.30 - MySQL Community Server - GPL
-- 서버 OS:                        Linux
-- HeidiSQL 버전:                  12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 테이블 pass_local.admission 구조 내보내기
CREATE TABLE IF NOT EXISTS `admission` (
  `admission_id` int NOT NULL,
  `pass_id` int NOT NULL,
  `seat_id` int NOT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `status` varchar(10) NOT NULL,
  `qr_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `started_at` datetime NOT NULL,
  `ended_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`admission_id`),
  KEY `FK_admission_pass` (`pass_id`),
  KEY `FK_admission_seat` (`seat_id`),
  KEY `FK_admission_user` (`user_id`),
  CONSTRAINT `FK_admission_pass` FOREIGN KEY (`pass_id`) REFERENCES `pass` (`pass_id`),
  CONSTRAINT `FK_admission_seat` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`),
  CONSTRAINT `FK_admission_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='입장권';

-- 테이블 데이터 pass_local.admission:~0 rows (대략적) 내보내기

-- 테이블 pass_local.package 구조 내보내기
CREATE TABLE IF NOT EXISTS `package` (
  `package_id` int NOT NULL,
  `place_id` int NOT NULL,
  `package_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` int NOT NULL,
  `period` int DEFAULT NULL,
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`package_id`),
  KEY `FK_package_place` (`place_id`),
  CONSTRAINT `FK_package_place` FOREIGN KEY (`place_id`) REFERENCES `place` (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='패키지';

-- 테이블 데이터 pass_local.package:~0 rows (대략적) 내보내기

-- 테이블 pass_local.pass 구조 내보내기
CREATE TABLE IF NOT EXISTS `pass` (
  `pass_id` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `package_id` int DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `remaining_time` datetime DEFAULT NULL,
  `started_at` datetime DEFAULT NULL,
  `ended_at` datetime DEFAULT NULL,
  `expired_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`pass_id`),
  KEY `FK_pass_user` (`user_id`),
  KEY `FK_pass_package` (`package_id`),
  CONSTRAINT `FK_pass_package` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`),
  CONSTRAINT `FK_pass_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='이용권 정보';

-- 테이블 데이터 pass_local.pass:~0 rows (대략적) 내보내기

-- 테이블 pass_local.place 구조 내보내기
CREATE TABLE IF NOT EXISTS `place` (
  `place_id` int NOT NULL,
  `place_address` varchar(50) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='플레이스';

-- 테이블 데이터 pass_local.place:~0 rows (대략적) 내보내기

-- 테이블 pass_local.seat 구조 내보내기
CREATE TABLE IF NOT EXISTS `seat` (
  `seat_id` int NOT NULL,
  `place_id` int NOT NULL,
  `seat_name` varchar(10) DEFAULT NULL,
  `type` varchar(10) NOT NULL,
  `x` int NOT NULL,
  `y` int NOT NULL,
  PRIMARY KEY (`seat_id`),
  KEY `FK_seat_place` (`place_id`),
  CONSTRAINT `FK_seat_place` FOREIGN KEY (`place_id`) REFERENCES `place` (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='자리 정보';

-- 테이블 데이터 pass_local.seat:~0 rows (대략적) 내보내기

-- 테이블 pass_local.user 구조 내보내기
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` varchar(50) NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `region` varchar(10) DEFAULT NULL,
  `phone` int DEFAULT NULL,
  `meta` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='사용자';

-- 테이블 데이터 pass_local.user:~0 rows (대략적) 내보내기

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
