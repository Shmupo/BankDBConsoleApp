-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: bankdb
-- ------------------------------------------------------
-- Server version	8.0.38

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `balancetransferdaoimp`
--

DROP TABLE IF EXISTS `balancetransferdaoimp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `balancetransferdaoimp` (
  `transferId` int NOT NULL,
  `balance` int NOT NULL,
  `fromAccountNumber` int NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `toAccountNumber` int NOT NULL,
  PRIMARY KEY (`transferId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `balancetransferdaoimp`
--

LOCK TABLES `balancetransferdaoimp` WRITE;
/*!40000 ALTER TABLE `balancetransferdaoimp` DISABLE KEYS */;
INSERT INTO `balancetransferdaoimp` VALUES (1252,5000,2702,'REJECTED',2652),(1302,5000,2702,'ACCEPTED',2652),(1352,12121,2652,'ACCEPTED',2752);
/*!40000 ALTER TABLE `balancetransferdaoimp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `balancetransferdaoimp_seq`
--

DROP TABLE IF EXISTS `balancetransferdaoimp_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `balancetransferdaoimp_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `balancetransferdaoimp_seq`
--

LOCK TABLES `balancetransferdaoimp_seq` WRITE;
/*!40000 ALTER TABLE `balancetransferdaoimp_seq` DISABLE KEYS */;
INSERT INTO `balancetransferdaoimp_seq` VALUES (1451);
/*!40000 ALTER TABLE `balancetransferdaoimp_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checkingaccountdaoimp`
--

DROP TABLE IF EXISTS `checkingaccountdaoimp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checkingaccountdaoimp` (
  `accountNumber` int NOT NULL,
  `balance` int NOT NULL,
  `customerId` int NOT NULL,
  PRIMARY KEY (`accountNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkingaccountdaoimp`
--

LOCK TABLES `checkingaccountdaoimp` WRITE;
/*!40000 ALTER TABLE `checkingaccountdaoimp` DISABLE KEYS */;
INSERT INTO `checkingaccountdaoimp` VALUES (2652,32879,52),(2702,5000,102),(2703,20000,52),(2752,52121,152),(2753,25000,152);
/*!40000 ALTER TABLE `checkingaccountdaoimp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checkingaccountdaoimp_seq`
--

DROP TABLE IF EXISTS `checkingaccountdaoimp_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checkingaccountdaoimp_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkingaccountdaoimp_seq`
--

LOCK TABLES `checkingaccountdaoimp_seq` WRITE;
/*!40000 ALTER TABLE `checkingaccountdaoimp_seq` DISABLE KEYS */;
INSERT INTO `checkingaccountdaoimp_seq` VALUES (2851);
/*!40000 ALTER TABLE `checkingaccountdaoimp_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customeraccountdaoimp`
--

DROP TABLE IF EXISTS `customeraccountdaoimp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customeraccountdaoimp` (
  `id` int NOT NULL,
  `pass` varchar(255) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customeraccountdaoimp`
--

LOCK TABLES `customeraccountdaoimp` WRITE;
/*!40000 ALTER TABLE `customeraccountdaoimp` DISABLE KEYS */;
INSERT INTO `customeraccountdaoimp` VALUES (52,'pass1','user1'),(102,'pass2','user2'),(152,'pass5','user5');
/*!40000 ALTER TABLE `customeraccountdaoimp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customeraccountdaoimp_seq`
--

DROP TABLE IF EXISTS `customeraccountdaoimp_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customeraccountdaoimp_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customeraccountdaoimp_seq`
--

LOCK TABLES `customeraccountdaoimp_seq` WRITE;
/*!40000 ALTER TABLE `customeraccountdaoimp_seq` DISABLE KEYS */;
INSERT INTO `customeraccountdaoimp_seq` VALUES (251);
/*!40000 ALTER TABLE `customeraccountdaoimp_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employeeaccountdaoimp`
--

DROP TABLE IF EXISTS `employeeaccountdaoimp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employeeaccountdaoimp` (
  `id` int NOT NULL,
  `pass` varchar(255) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employeeaccountdaoimp`
--

LOCK TABLES `employeeaccountdaoimp` WRITE;
/*!40000 ALTER TABLE `employeeaccountdaoimp` DISABLE KEYS */;
INSERT INTO `employeeaccountdaoimp` VALUES (0,'pass','admin');
/*!40000 ALTER TABLE `employeeaccountdaoimp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employeeaccountdaoimp_seq`
--

DROP TABLE IF EXISTS `employeeaccountdaoimp_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employeeaccountdaoimp_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employeeaccountdaoimp_seq`
--

LOCK TABLES `employeeaccountdaoimp_seq` WRITE;
/*!40000 ALTER TABLE `employeeaccountdaoimp_seq` DISABLE KEYS */;
INSERT INTO `employeeaccountdaoimp_seq` VALUES (1);
/*!40000 ALTER TABLE `employeeaccountdaoimp_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pendingcheckingaccountdaoimp`
--

DROP TABLE IF EXISTS `pendingcheckingaccountdaoimp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pendingcheckingaccountdaoimp` (
  `id` int NOT NULL,
  `customerId` int NOT NULL,
  `startingBalance` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pendingcheckingaccountdaoimp`
--

LOCK TABLES `pendingcheckingaccountdaoimp` WRITE;
/*!40000 ALTER TABLE `pendingcheckingaccountdaoimp` DISABLE KEYS */;
/*!40000 ALTER TABLE `pendingcheckingaccountdaoimp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pendingcheckingaccountdaoimp_seq`
--

DROP TABLE IF EXISTS `pendingcheckingaccountdaoimp_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pendingcheckingaccountdaoimp_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pendingcheckingaccountdaoimp_seq`
--

LOCK TABLES `pendingcheckingaccountdaoimp_seq` WRITE;
/*!40000 ALTER TABLE `pendingcheckingaccountdaoimp_seq` DISABLE KEYS */;
INSERT INTO `pendingcheckingaccountdaoimp_seq` VALUES (401);
/*!40000 ALTER TABLE `pendingcheckingaccountdaoimp_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'bankdb'
--
/*!50003 DROP PROCEDURE IF EXISTS `GetCheckingAccountByCustomerId` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetCheckingAccountByCustomerId`(IN custId INT)
BEGIN
    
    SELECT * FROM checkingaccountdaoimp
    WHERE customerId = custId;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getTransferByCustomer` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTransferByCustomer`(IN cust_id INT)
BEGIN
    SELECT b.balance, b.fromAccountNumber, b.toAccountNumber, b.transferId, b.status
    FROM balancetransferdaoimp b
    JOIN checkingaccountdaoimp c ON c.accountNumber = b.fromAccountNumber OR c.accountNumber = b.toAccountNumber
    WHERE c.customerId = cust_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-25  9:08:42
