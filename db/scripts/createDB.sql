CREATE DATABASE  IF NOT EXISTS `g5_lamicodb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `g5_lamicodb`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: g5_lamicodb
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `apartment`
--

DROP TABLE IF EXISTS `apartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apartment` (
  `prNum` int NOT NULL,
  `buildingNum` int NOT NULL,
  `roomNum` int NOT NULL,
  `unitNum` int NOT NULL,
  `bedroomNum` int NOT NULL,
  `bathroomNUM` int NOT NULL,
  `livingroomNUM` int NOT NULL,
  `hasBalcony` bit(1) NOT NULL,
  `kitchenType` varchar(20) NOT NULL,
  `hasGarden` bit(1) NOT NULL,
  PRIMARY KEY (`prNum`),
  KEY `buildingNum` (`buildingNum`),
  CONSTRAINT `apartment_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `realestate` (`prNum`) ON DELETE CASCADE,
  CONSTRAINT `apartment_ibfk_2` FOREIGN KEY (`buildingNum`) REFERENCES `building` (`prNum`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `broker`
--

DROP TABLE IF EXISTS `broker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `broker` (
  `bShare` double NOT NULL,
  `ssn` int NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `broker_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `building`
--

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `building` (
  `prNum` int NOT NULL,
  `landNum` int NOT NULL,
  `bName` varchar(20) NOT NULL,
  `yearBuilt` int DEFAULT NULL,
  `floorNum` int NOT NULL,
  PRIMARY KEY (`prNum`),
  KEY `landNum` (`landNum`),
  CONSTRAINT `building_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `realestate` (`prNum`) ON DELETE CASCADE,
  CONSTRAINT `building_ibfk_2` FOREIGN KEY (`landNum`) REFERENCES `land` (`prNum`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chequetransaction`
--

DROP TABLE IF EXISTS `chequetransaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chequetransaction` (
  `NoOfMonths` int DEFAULT NULL,
  `ID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `chequetransaction_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `transaction` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clienttbl`
--

DROP TABLE IF EXISTS `clienttbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clienttbl` (
  `sponsor` varchar(32) NOT NULL,
  `incomeLevel` int DEFAULT NULL,
  `employeementInfo` varchar(32) DEFAULT NULL,
  `ssn` int NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `clienttbl_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `companybroker`
--

DROP TABLE IF EXISTS `companybroker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companybroker` (
  `ssn` int NOT NULL,
  `employeeSSN` int NOT NULL,
  PRIMARY KEY (`ssn`),
  KEY `employeeSSN` (`employeeSSN`),
  CONSTRAINT `companybroker_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `broker` (`ssn`) ON DELETE CASCADE,
  CONSTRAINT `companybroker_ibfk_2` FOREIGN KEY (`employeeSSN`) REFERENCES `employee` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contract`
--

DROP TABLE IF EXISTS `contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract` (
  `contractNo` int NOT NULL,
  `CDate` date DEFAULT NULL,
  `Cstatus` varchar(32) DEFAULT NULL,
  `ArrangmentType` varchar(32) DEFAULT NULL,
  `price` varchar(32) DEFAULT NULL,
  `brokerSSN` int DEFAULT NULL,
  `clientSSN` int DEFAULT NULL,
  `prNum` int DEFAULT NULL,
  PRIMARY KEY (`contractNo`),
  KEY `prNum` (`prNum`),
  KEY `brokerSSN` (`brokerSSN`),
  KEY `clientSSN` (`clientSSN`),
  CONSTRAINT `contract_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `realestate` (`prNum`) ON DELETE CASCADE,
  CONSTRAINT `contract_ibfk_2` FOREIGN KEY (`brokerSSN`) REFERENCES `broker` (`ssn`) ON DELETE CASCADE,
  CONSTRAINT `contract_ibfk_3` FOREIGN KEY (`clientSSN`) REFERENCES `clienttbl` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `email`
--

DROP TABLE IF EXISTS `email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email` (
  `address` varchar(64) NOT NULL,
  `ssn` int NOT NULL,
  PRIMARY KEY (`ssn`,`address`),
  CONSTRAINT `email_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `salary` int NOT NULL,
  `hireDate` date DEFAULT NULL,
  `ePosition` varchar(16) NOT NULL,
  `department` varchar(16) NOT NULL,
  `ssn` int NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `independentbroker`
--

DROP TABLE IF EXISTS `independentbroker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `independentbroker` (
  `commission` int NOT NULL,
  `ssn` int NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `independentbroker_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `broker` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `land`
--

DROP TABLE IF EXISTS `land`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `land` (
  `prNum` int NOT NULL,
  `plotNum` int NOT NULL,
  `blockNum` int NOT NULL,
  PRIMARY KEY (`prNum`),
  CONSTRAINT `land_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `realestate` (`prNum`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `ssn` int NOT NULL,
  `pName` varchar(32) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `bankInfo` varchar(32) NOT NULL,
  PRIMARY KEY (`ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phone` (
  `phoneNumber` char(10) NOT NULL,
  `ssn` int NOT NULL,
  PRIMARY KEY (`ssn`,`phoneNumber`),
  CONSTRAINT `phone_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `realestate`
--

DROP TABLE IF EXISTS `realestate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `realestate` (
  `prNum` int NOT NULL,
  `prCondition` varchar(255) NOT NULL,
  `city` varchar(20) NOT NULL,
  `streetName` varchar(20) NOT NULL,
  `valuation` decimal(12,2) NOT NULL,
  `areaDescription` varchar(255) DEFAULT NULL,
  `area` decimal(12,2) NOT NULL,
  `ownerSSN` int NOT NULL,
  PRIMARY KEY (`prNum`),
  KEY `ownerSSN` (`ownerSSN`),
  CONSTRAINT `realestate_ibfk_1` FOREIGN KEY (`ownerSSN`) REFERENCES `person` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rentalapartment`
--

DROP TABLE IF EXISTS `rentalapartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rentalapartment` (
  `prNum` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`prNum`),
  CONSTRAINT `rentalapartment_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `apartment` (`prNum`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `saleapartment`
--

DROP TABLE IF EXISTS `saleapartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `saleapartment` (
  `prNum` int NOT NULL,
  `rent` decimal(8,2) NOT NULL,
  PRIMARY KEY (`prNum`),
  CONSTRAINT `saleapartment_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `apartment` (`prNum`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `ID` varchar(32) NOT NULL,
  `paymentDate` date DEFAULT NULL,
  `amount` decimal(12,2) DEFAULT NULL,
  `recipient` varchar(32) DEFAULT NULL,
  `sender` varchar(32) DEFAULT NULL,
  `contractNo` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `contractNo` (`contractNo`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`contractNo`) REFERENCES `contract` (`contractNo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-06 11:23:32
