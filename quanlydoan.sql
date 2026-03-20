-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: quanlydoan
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authority` (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES ('ROLE_ADMIN'),('ROLE_PARTNER'),('ROLE_STUDENT'),('ROLE_TEACHER');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog`
--

DROP TABLE IF EXISTS `blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `created_time` time DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `num_view` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqyvjif1i2geaeuvkh3n1jrnn4` (`category_id`),
  KEY `FKkr2fy24puc3x3sdnla4r1iok1` (`user_id`),
  CONSTRAINT `FKkr2fy24puc3x3sdnla4r1iok1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKqyvjif1i2geaeuvkh3n1jrnn4` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog`
--

LOCK TABLES `blog` WRITE;
/*!40000 ALTER TABLE `blog` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_type` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `is_file` bit(1) DEFAULT NULL,
  `receiver` bigint DEFAULT NULL,
  `sender` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKassi1e8b64hoj61bb710c8oq4` (`receiver`),
  KEY `FKj5nm89ig7glfvk1ksfmqdsydo` (`sender`),
  CONSTRAINT `FKassi1e8b64hoj61bb710c8oq4` FOREIGN KEY (`receiver`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj5nm89ig7glfvk1ksfmqdsydo` FOREIGN KEY (`sender`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_room`
--

DROP TABLE IF EXISTS `chat_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `semester_teacher_id` bigint DEFAULT NULL,
  `sender` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8w9fc1tk1b9lqiboes8eq6j0s` (`sender`),
  CONSTRAINT `FK8w9fc1tk1b9lqiboes8eq6j0s` FOREIGN KEY (`sender`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_room`
--

LOCK TABLES `chat_room` WRITE;
/*!40000 ALTER TABLE `chat_room` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `description` text,
  `email` varchar(255) DEFAULT NULL,
  `image_banner` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `tax_code` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `link_image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number_download` int DEFAULT NULL,
  `number_view` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1vwugdy4y8ivgpikjcuojibc0` (`category_id`),
  KEY `FKm19xjdnh3l6aueyrpm1705t52` (`user_id`),
  CONSTRAINT `FK1vwugdy4y8ivgpikjcuojibc0` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKm19xjdnh3l6aueyrpm1705t52` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document_detail`
--

DROP TABLE IF EXISTS `document_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_type` varchar(255) DEFAULT NULL,
  `link_file` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `document_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi8p1lw3va1e0qfmuna1wpet1w` (`document_id`),
  CONSTRAINT `FKi8p1lw3va1e0qfmuna1wpet1w` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document_detail`
--

LOCK TABLES `document_detail` WRITE;
/*!40000 ALTER TABLE `document_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `document_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnk4ftb5am9ubmkv1661h15ds9` (`user_id`),
  CONSTRAINT `FKnk4ftb5am9ubmkv1661h15ds9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rate`
--

DROP TABLE IF EXISTS `rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avg_score` double DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `q1` int DEFAULT NULL,
  `q2` int DEFAULT NULL,
  `q3` int DEFAULT NULL,
  `student_regis_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmpfql84dwnn8ysue51jwxpuqu` (`student_regis_id`),
  CONSTRAINT `FKmpfql84dwnn8ysue51jwxpuqu` FOREIGN KEY (`student_regis_id`) REFERENCES `student_regis` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rate`
--

LOCK TABLES `rate` WRITE;
/*!40000 ALTER TABLE `rate` DISABLE KEYS */;
/*!40000 ALTER TABLE `rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `related_document_student`
--

DROP TABLE IF EXISTS `related_document_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `related_document_student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `related_documents_id` bigint DEFAULT NULL,
  `student_regis_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKstat613gi7hsx9cdh0qn6r60i` (`related_documents_id`),
  KEY `FK2rts6hc5s57vn7318bqg7smr4` (`student_regis_id`),
  CONSTRAINT `FK2rts6hc5s57vn7318bqg7smr4` FOREIGN KEY (`student_regis_id`) REFERENCES `student_regis` (`id`),
  CONSTRAINT `FKstat613gi7hsx9cdh0qn6r60i` FOREIGN KEY (`related_documents_id`) REFERENCES `related_documents` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `related_document_student`
--

LOCK TABLES `related_document_student` WRITE;
/*!40000 ALTER TABLE `related_document_student` DISABLE KEYS */;
/*!40000 ALTER TABLE `related_document_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `related_documents`
--

DROP TABLE IF EXISTS `related_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `related_documents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `on_time_count` int DEFAULT NULL,
  `out_time_count` int DEFAULT NULL,
  `semester_teacher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8c9hpbsjq3wnd6eiw0di9mwek` (`semester_teacher_id`),
  CONSTRAINT `FK8c9hpbsjq3wnd6eiw0di9mwek` FOREIGN KEY (`semester_teacher_id`) REFERENCES `semester_teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `related_documents`
--

LOCK TABLES `related_documents` WRITE;
/*!40000 ALTER TABLE `related_documents` DISABLE KEYS */;
/*!40000 ALTER TABLE `related_documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_ratio`
--

DROP TABLE IF EXISTS `score_ratio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_ratio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `percent` float DEFAULT NULL,
  `semester_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3whxlrsntmp3bqx02b8s24xqf` (`semester_id`),
  CONSTRAINT `FK3whxlrsntmp3bqx02b8s24xqf` FOREIGN KEY (`semester_id`) REFERENCES `semester` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_ratio`
--

LOCK TABLES `score_ratio` WRITE;
/*!40000 ALTER TABLE `score_ratio` DISABLE KEYS */;
/*!40000 ALTER TABLE `score_ratio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_result`
--

DROP TABLE IF EXISTS `score_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_result` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `percent` float DEFAULT NULL,
  `point` float DEFAULT NULL,
  `score_ratio_id` bigint DEFAULT NULL,
  `student_regis_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5dk9emc6b60r9vlidoptqmkqc` (`student_regis_id`),
  CONSTRAINT `FK5dk9emc6b60r9vlidoptqmkqc` FOREIGN KEY (`student_regis_id`) REFERENCES `student_regis` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_result`
--

LOCK TABLES `score_result` WRITE;
/*!40000 ALTER TABLE `score_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `score_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semester`
--

DROP TABLE IF EXISTS `semester`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `semester` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `year_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semester`
--

LOCK TABLES `semester` WRITE;
/*!40000 ALTER TABLE `semester` DISABLE KEYS */;
/*!40000 ALTER TABLE `semester` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semester_company`
--

DROP TABLE IF EXISTS `semester_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `semester_company` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `current_student` int DEFAULT NULL,
  `description` text,
  `max_student` int DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  `semester_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh4w7ci2796hvsex4f0ninfobc` (`company_id`),
  KEY `FKpag5v0ojsqrcx2l6b6nb7f81e` (`semester_id`),
  CONSTRAINT `FKh4w7ci2796hvsex4f0ninfobc` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FKpag5v0ojsqrcx2l6b6nb7f81e` FOREIGN KEY (`semester_id`) REFERENCES `semester` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semester_company`
--

LOCK TABLES `semester_company` WRITE;
/*!40000 ALTER TABLE `semester_company` DISABLE KEYS */;
/*!40000 ALTER TABLE `semester_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semester_teacher`
--

DROP TABLE IF EXISTS `semester_teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `semester_teacher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `current_students` int DEFAULT NULL,
  `description_project` varchar(255) DEFAULT NULL,
  `max_students` int DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `semester_type_id` bigint DEFAULT NULL,
  `teacher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpk3y2836e0dwtxdb4gk9odfhj` (`semester_type_id`),
  KEY `FKbt2lx1jh0lplu7mnvwhsash71` (`teacher_id`),
  CONSTRAINT `FKbt2lx1jh0lplu7mnvwhsash71` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpk3y2836e0dwtxdb4gk9odfhj` FOREIGN KEY (`semester_type_id`) REFERENCES `semester_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semester_teacher`
--

LOCK TABLES `semester_teacher` WRITE;
/*!40000 ALTER TABLE `semester_teacher` DISABLE KEYS */;
/*!40000 ALTER TABLE `semester_teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semester_type`
--

DROP TABLE IF EXISTS `semester_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `semester_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `deadline_regis` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `semester_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnr7h71k4p2jxpc6o8jj28hava` (`semester_id`),
  CONSTRAINT `FKnr7h71k4p2jxpc6o8jj28hava` FOREIGN KEY (`semester_id`) REFERENCES `semester` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semester_type`
--

LOCK TABLES `semester_type` WRITE;
/*!40000 ALTER TABLE `semester_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `semester_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_regis`
--

DROP TABLE IF EXISTS `student_regis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_regis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `accept` bit(1) DEFAULT NULL,
  `company_address` varchar(255) DEFAULT NULL,
  `company_email` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `company_phone` varchar(255) DEFAULT NULL,
  `evaluate` varchar(255) DEFAULT NULL,
  `internship_type` varchar(255) DEFAULT NULL,
  `introduction_paper` varchar(255) DEFAULT NULL,
  `local_date_time` datetime DEFAULT NULL,
  `rate` varchar(255) DEFAULT NULL,
  `student_regis_status` varchar(255) DEFAULT NULL,
  `tax_code` varchar(255) DEFAULT NULL,
  `total_score` float DEFAULT NULL,
  `semester_company_id` bigint DEFAULT NULL,
  `semester_teacher_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfm58hyeo7kdj557lcs9l600hb` (`semester_company_id`),
  KEY `FK81879ja0u02mxa3gpj9p9nt30` (`semester_teacher_id`),
  KEY `FKiltmq3wd88vd17hqj1g76ntrw` (`student_id`),
  CONSTRAINT `FK81879ja0u02mxa3gpj9p9nt30` FOREIGN KEY (`semester_teacher_id`) REFERENCES `semester_teacher` (`id`),
  CONSTRAINT `FKfm58hyeo7kdj557lcs9l600hb` FOREIGN KEY (`semester_company_id`) REFERENCES `semester_company` (`id`),
  CONSTRAINT `FKiltmq3wd88vd17hqj1g76ntrw` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_regis`
--

LOCK TABLES `student_regis` WRITE;
/*!40000 ALTER TABLE `student_regis` DISABLE KEYS */;
/*!40000 ALTER TABLE `student_regis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activation_key` varchar(255) DEFAULT NULL,
  `actived` bit(1) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `remember_key` varchar(255) DEFAULT NULL,
  `token_fcm` varchar(255) DEFAULT NULL,
  `user_type` int DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `authority_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq6r7e19l5xjmty0j0w6i2inlv` (`authority_name`),
  CONSTRAINT `FKq6r7e19l5xjmty0j0w6i2inlv` FOREIGN KEY (`authority_name`) REFERENCES `authority` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,_binary '',NULL,NULL,'2026-03-20','admin@gmail.com','ADMIN','$2a$10$8pH7vsjgM1JtB8gNtaz.U.2dT4RKjwUFp9yxfJXRn1g7Xb9BrO9ia','097651232',NULL,NULL,1,'admin@gmail.com','ROLE_ADMIN');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_progress`
--

DROP TABLE IF EXISTS `work_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `description` text,
  `on_time_count` int DEFAULT NULL,
  `out_time_count` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `semester_teacher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl0ik7dw1lsux5ire7vha06lti` (`semester_teacher_id`),
  CONSTRAINT `FKl0ik7dw1lsux5ire7vha06lti` FOREIGN KEY (`semester_teacher_id`) REFERENCES `semester_teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_progress`
--

LOCK TABLES `work_progress` WRITE;
/*!40000 ALTER TABLE `work_progress` DISABLE KEYS */;
/*!40000 ALTER TABLE `work_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_progress_student`
--

DROP TABLE IF EXISTS `work_progress_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_progress_student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text,
  `created_date` datetime DEFAULT NULL,
  `file` varchar(255) DEFAULT NULL,
  `percent` float DEFAULT NULL,
  `replay` varchar(255) DEFAULT NULL,
  `replay_date` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `student_regis_id` bigint DEFAULT NULL,
  `work_process_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3frck48ru667g4kjqm6wqdfdh` (`student_regis_id`),
  KEY `FKauyjjyp052hcora7f9atbvsmf` (`work_process_id`),
  CONSTRAINT `FK3frck48ru667g4kjqm6wqdfdh` FOREIGN KEY (`student_regis_id`) REFERENCES `student_regis` (`id`),
  CONSTRAINT `FKauyjjyp052hcora7f9atbvsmf` FOREIGN KEY (`work_process_id`) REFERENCES `work_progress` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_progress_student`
--

LOCK TABLES `work_progress_student` WRITE;
/*!40000 ALTER TABLE `work_progress_student` DISABLE KEYS */;
/*!40000 ALTER TABLE `work_progress_student` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-20 20:18:33
