DROP DATABASE IF EXISTS g5_lamicodb;
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
-- Dumping data for table `apartment`
--

LOCK TABLES `apartment` WRITE;
/*!40000 ALTER TABLE `apartment` DISABLE KEYS */;
INSERT INTO `apartment` VALUES (2,16,4,303,2,1,1,_binary '','Aluminium',_binary ''),(4,16,2,302,1,1,1,_binary '','Oak',_binary '\0'),(6,16,5,301,3,2,1,_binary '','Italian',_binary ''),(8,18,3,202,2,1,1,_binary '','Chinese',_binary '\0'),(10,18,4,201,3,2,1,_binary '','Luxury',_binary ''),(12,20,2,102,1,1,1,_binary '','Traditional',_binary '\0'),(14,20,3,101,2,1,1,_binary '','Modern',_binary '');
/*!40000 ALTER TABLE `apartment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `broker`
--

DROP TABLE IF EXISTS `broker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `broker` (
  `bShare` double NOT NULL,
  `ssn` char(9) NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `broker_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `broker`
--

LOCK TABLES `broker` WRITE;
/*!40000 ALTER TABLE `broker` DISABLE KEYS */;
/*!40000 ALTER TABLE `broker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `building`
--

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `building` (
  `prNum` int NOT NULL,
  `landNum` int NOT NULL,
  `bName` varchar(40) NOT NULL,
  `yearBuilt` int DEFAULT NULL,
  `floorNum` int NOT NULL,
  PRIMARY KEY (`prNum`),
  KEY `landNum` (`landNum`),
  CONSTRAINT `building_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `realestate` (`prNum`) ON DELETE CASCADE,
  CONSTRAINT `building_ibfk_2` FOREIGN KEY (`landNum`) REFERENCES `land` (`prNum`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `building`
--

LOCK TABLES `building` WRITE;
/*!40000 ALTER TABLE `building` DISABLE KEYS */;
INSERT INTO `building` VALUES (16,17,'Building C',2012,8),(18,19,'Building B',2015,10),(20,21,'Building A',2010,5);
/*!40000 ALTER TABLE `building` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `chequetransaction`
--

LOCK TABLES `chequetransaction` WRITE;
/*!40000 ALTER TABLE `chequetransaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `chequetransaction` ENABLE KEYS */;
UNLOCK TABLES;

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
  `ssn` char(9) NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `clienttbl_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clienttbl`
--

LOCK TABLES `clienttbl` WRITE;
/*!40000 ALTER TABLE `clienttbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `clienttbl` ENABLE KEYS */;
UNLOCK TABLES;

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
  `brokerSSN` char(9) DEFAULT NULL,
  `clientSSN` char(9) DEFAULT NULL,
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
-- Dumping data for table `contract`
--
INSERT INTO contract (contractNo, CDate, Cstatus, ArrangmentType, price, brokerSSN, clientSSN, prNum) 
VALUES 
(1, '2024-06-01', 'Active', 'Sale', '50000', '123456789', '987654321', 1),
(2, '2024-06-02', 'Inactive', 'Lease', '2000', '987654321', '123456789', 2),
(3, '2024-06-03', 'Active', 'Rent', '1500', '123456789', '987654321', 3),
(4, '2024-06-04', 'Active', 'Sale', '75000', '987654321', '123456789', 4),
(5, '2024-06-05', 'Inactive', 'Lease', '3000', '123456789', '987654321', 5);

LOCK TABLES `contract` WRITE;
/*!40000 ALTER TABLE `contract` DISABLE KEYS */;
/*!40000 ALTER TABLE `contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email`
--

DROP TABLE IF EXISTS `email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email` (
  `address` varchar(64) NOT NULL,
  `ssn` char(9) NOT NULL,
  PRIMARY KEY (`ssn`,`address`),
  CONSTRAINT `email_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email`
--

LOCK TABLES `email` WRITE;
/*!40000 ALTER TABLE `email` DISABLE KEYS */;
INSERT INTO `email` VALUES ('Khalil Al-Natsheh@example.com','010123456'),('Sami Al-Hajj@example.com','011234567'),('Nadia Al-Khaldi@example.com','012345678'),('Mohammed Al-Najjar@example.com','013456789'),('Rania Al-Araj@example.com','014567890'),('Omar Al-Hajj@example.com','015678901'),('Amina Al-Masri@example.com','016789012'),('Ali Al-Qasem@example.com','017890123'),('Jihad Al-Hourani@example.com','018901234'),('Rasha Al-Khalil@example.com','019012345'),('Khalil Al-Natsheh@example.com','020123456'),('Sami Al-Hajj@example.com','021234567'),('Nadia Al-Khaldi@example.com','022345678'),('Mohammed Al-Najjar@example.com','023456789'),('Rania Al-Araj@example.com','024567890'),('Omar Al-Hajj@example.com','025678901'),('Amina Al-Masri@example.com','026789012'),('Ali Al-Qasem@example.com','027890123'),('Jihad Al-Hourani@example.com','028901234'),('Rasha Al-Khalil@example.com','029012345'),('Sami Al-Hajj@example.com','111234567'),('Ahmed Al-Khateeb@example.com','123456789'),('Omar Al-Hajj@example.com','219853985'),('Nadia Al-Khaldi@example.com','222345678'),('Fatima Al-Araj@example.com','234567890'),('Mohammed Al-Najjar@example.com','333456789'),('Mohammed Al-Najjar@example.com','345678901'),('Rania Al-Araj@example.com','426985632'),('Rania Al-Araj@example.com','444567890'),('Rania Al-Khaldi@example.com','456789012'),('Omar Al-Hajj@example.com','555678901'),('Omar Al-Hajj@example.com','567890123'),('Nadia Al-Khaldi@example.com','654321985'),('Ali Al-Qasem@example.com','654398753'),('Amina Al-Masri@example.com','666789012'),('Amina Al-Masri@example.com','678901234'),('Mohammed Al-Najjar@example.com','753192654'),('Khalil Al-Natsheh@example.com','753984210'),('Ali Al-Qasem@example.com','777890123'),('Ali Al-Qasem@example.com','789012345'),('Jihad Al-Hourani@example.com','888901234'),('Jihad Al-Hourani@example.com','890123456'),('Rasha Al-Khalil@example.com','901234567'),('Khalil Al-Natsheh@example.com','912345678'),('Sami Al-Hajj@example.com','982135479'),('Amina Al-Masri@example.com','985632159'),('Rasha Al-Khalil@example.com','999012345');
/*!40000 ALTER TABLE `email` ENABLE KEYS */;
UNLOCK TABLES;

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
  `ssn` char(9) NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `independentbroker`
--

DROP TABLE IF EXISTS `independentbroker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `independentbroker` (
  `commission` int NOT NULL,
  `ssn` char(9) NOT NULL,
  PRIMARY KEY (`ssn`),
  CONSTRAINT `independentbroker_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `broker` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `independentbroker`
--

LOCK TABLES `independentbroker` WRITE;
/*!40000 ALTER TABLE `independentbroker` DISABLE KEYS */;
/*!40000 ALTER TABLE `independentbroker` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `land`
--

LOCK TABLES `land` WRITE;
/*!40000 ALTER TABLE `land` DISABLE KEYS */;
INSERT INTO `land` VALUES (13,5,5),(15,4,4),(17,3,3),(19,2,2),(21,1,1);
/*!40000 ALTER TABLE `land` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `ssn` char(9) NOT NULL,
  `pName` varchar(32) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `bankInfo` varchar(32) NOT NULL,
  PRIMARY KEY (`ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES ('010123456','Khalil Al-Natsheh','Al-Walaja St, Bethlehem','1988-04-12','Bank of Palestine'),('011234567','Sami Al-Hajj','Al-Quds St, Jerusalem','1972-06-20','Palestine Islamic Bank'),('012345678','Nadia Al-Khaldi','Al-Manara St, Ramallah','1990-08-22','Arab Bank'),('013456789','Mohammed Al-Najjar','Al-Bireh St, Ramallah','1985-03-15','Bank of Palestine'),('014567890','Rania Al-Araj','Al-Quds St, Jerusalem','1978-11-18','Palestine Islamic Bank'),('015678901','Omar Al-Hajj','Al-Walaja St, Bethlehem','1992-01-15','Arab Bank'),('016789012','Amina Al-Masri','Al-Khansa St, Hebron','1980-05-25','Bank of Palestine'),('017890123','Ali Al-Qasem','Al-Quds St, Jerusalem','1975-02-12','Bank of Palestine'),('018901234','Jihad Al-Hourani','Al-Manara St, Ramallah','1982-09-25','Bank of Palestine'),('019012345','Rasha Al-Khalil','Al-Bireh St, Ramallah','1995-01-01','Arab Bank'),('020123456','Khalil Al-Natsheh','Al-Walaja St, Bethlehem','1988-04-10','Bank of Palestine'),('021234567','Sami Al-Hajj','Al-Quds St, Jerusalem','1972-09-25','Palestine Islamic Bank'),('022345678','Nadia Al-Khaldi','Al-Manara St, Ramallah','1990-06-25','Palestine Islamic Bank'),('023456789','Mohammed Al-Najjar','Al-Bireh St, Ramallah','1985-03-01','Arab Bank'),('024567890','Rania Al-Araj','Al-Quds St, Jerusalem','1978-11-12','Bank of Palestine'),('025678901','Omar Al-Hajj','Al-Walaja St, Bethlehem','1992-01-15','Arab Bank'),('026789012','Amina Al-Masri','Al-Khansa St, Hebron','1980-05-20','Bank of Palestine'),('027890123','Ali Al-Qasem','Al-Quds St, Jerusalem','1975-02-15','Bank of Palestine'),('028901234','Jihad Al-Hourani','Al-Manara St, Ramallah','1982-09-25','Bank of Palestine'),('029012345','Rasha Al-Khalil','Al-Bireh St, Ramallah','1995-01-01','Arab Bank'),('111234567','Sami Al-Hajj','Al-Quds St, Jerusalem','1972-09-28','Bank of Palestine'),('123456789','Ahmed Al-Khateeb','Al-Masayef St, Ramallah','1990-01-01','Arab Bank'),('219853985','Omar Al-Hajj','Al-Walaja St, Bethlehem','1992-01-15','Arab Bank'),('222345678','Nadia Al-Khaldi','Al-Manara St, Ramallah','1990-06-25','Palestine Islamic Bank'),('234567890','Fatima Al-Araj','Al-Bireh St, Ramallah','1985-06-15','Bank of Palestine'),('333456789','Mohammed Al-Najjar','Al-Bireh St, Ramallah','1985-03-01','Arab Bank'),('345678901','Mohammed Al-Najjar','Al-Manara St, Ramallah','1970-03-20','Palestine Islamic Bank'),('426985632','Rania Al-Araj','Al-Quds St, Jerusalem','1978-11-12','Bank of Palestine'),('444567890','Rania Al-Araj','Al-Quds St, Jerusalem','1978-11-12','Bank of Palestine'),('456789012','Rania Al-Khaldi','Al-Quds St, Jerusalem','1995-09-12','Arab Bank'),('555678901','Omar Al-Hajj','Al-Walaja St, Bethlehem','1992-01-15','Arab Bank'),('567890123','Omar Al-Hajj','Al-Walaja St, Bethlehem','1980-11-25','Bank of Palestine'),('654321985','Nadia Al-Khaldi','Al-Manara St, Ramallah','1990-06-25','Palestine Islamic Bank'),('654398753','Ali Al-Qasem','Al-Quds St, Jerusalem','1975-02-15','Bank of Palestine'),('666789012','Amina Al-Masri','Al-Khansa St, Hebron','1980-05-20','Bank of Palestine'),('678901234','Amina Al-Masri','Al-Khansa St, Hebron','1992-05-18','Palestine Islamic Bank'),('753192654','Mohammed Al-Najjar','Al-Bireh St, Ramallah','1985-03-01','Arab Bank'),('753984210','Khalil Al-Natsheh','Al-Walaja St, Bethlehem','1988-04-10','Bank of Palestine'),('777890123','Ali Al-Qasem','Al-Quds St, Jerusalem','1975-02-15','Bank of Palestine'),('789012345','Ali Al-Qasem','Al-Quds St, Jerusalem','1975-02-08','Arab Bank'),('888901234','Jihad Al-Hourani','Al-Manara St, Ramallah','1982-09-25','Bank of Palestine'),('890123456','Jihad Al-Hourani','Al-Manara St, Ramallah','1982-08-22','Bank of Palestine'),('901234567','Rasha Al-Khalil','Al-Bireh St, Ramallah','1997-01-15','Palestine Islamic Bank'),('912345678','Khalil Al-Natsheh','Al-Walaja St, Bethlehem','1988-04-10','Arab Bank'),('982135479','Sami Al-Hajj','Al-Quds St, Jerusalem','1972-09-25','Palestine Islamic Bank'),('985632159','Amina Al-Masri','Al-Khansa St, Hebron','1980-05-20','Bank of Palestine'),('999012345','Rasha Al-Khalil','Al-Bireh St, Ramallah','1995-01-01','Arab Bank');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phone` (
  `phoneNumber` char(10) NOT NULL,
  `ssn` char(9) NOT NULL,
  PRIMARY KEY (`ssn`,`phoneNumber`),
  CONSTRAINT `phone_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `person` (`ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone`
--

LOCK TABLES `phone` WRITE;
/*!40000 ALTER TABLE `phone` DISABLE KEYS */;
/*!40000 ALTER TABLE `phone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `realestate`
--

DROP TABLE IF EXISTS `realestate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `realestate` (
  `prNum` int NOT NULL AUTO_INCREMENT,
  `prCondition` varchar(255) NOT NULL,
  `city` varchar(40) NOT NULL,
  `streetName` varchar(40) NOT NULL,
  `valuation` decimal(12,2) NOT NULL,
  `areaDescription` varchar(255) DEFAULT NULL,
  `area` decimal(12,2) NOT NULL,
  `ownerSSN` char(9) NOT NULL,
  PRIMARY KEY (`prNum`),
  KEY `ownerSSN` (`ownerSSN`),
  CONSTRAINT `realestate_ibfk_1` FOREIGN KEY (`ownerSSN`) REFERENCES `person` (`ssn`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `realestate`
--

LOCK TABLES `realestate` WRITE;
/*!40000 ALTER TABLE `realestate` DISABLE KEYS */;
INSERT INTO `realestate` VALUES (1,'Cleared','New York','5th Avenue',500000.00,'Close to Central Park, walking distance to shops and restaurants',200.00,'654398753'),(2,'Furnished','Los Angeles','Sunset Boulevard',300000.00,'Near Hollywood Hills, great views of the city',150.00,'426985632'),(3,'Just the Basis','Chicago','Michigan Avenue',200000.00,'In the heart of downtown, close to public transportation',120.00,'753192654'),(4,'Partially Renovated','Houston','Main Street',350000.00,'Near the Museum District, walking distance to restaurants',180.00,'982135479'),(5,'Furnished','Phoenix','Camelback Road',250000.00,'Close to hiking trails, great views of the mountains',140.00,'028901234'),(6,'Just the Basis','Philadelphia','Broad Street',180000.00,'In a quiet neighborhood, close to schools',100.00,'024567890'),(7,'Partially Renovated','San Antonio','River Walk',320000.00,'On the River Walk, close to restaurants and bars',160.00,'020123456'),(8,'Furnished','San Diego','La Jolla Boulevard',280000.00,'Close to the beach, great ocean views',130.00,'985632159'),(9,'Just the Basis','Dallas','Main Street',220000.00,'In a growing neighborhood, close to new developments',110.00,'027890123'),(10,'Partially Renovated','San Jose','Silicon Valley Boulevard',380000.00,'Close to tech companies, great access to highways',190.00,'019012345'),(11,'Furnished','Indianapolis','Meridian Street',240000.00,'In a quiet neighborhood, close to parks',120.00,'018901234'),(12,'Just the Basis','Jacksonville','Beach Boulevard',200000.00,'Close to the beach, great ocean views',100.00,'888901234'),(13,'Partially Renovated','San Francisco','Haight Street',420000.00,'In the heart of Haight-Ashbury, close to shops and restaurants',200.00,'999012345'),(14,'Furnished','Columbus','High Street',260000.00,'Close to Ohio State University, great access to public transportation',140.00,'010123456'),(15,'Just the Basis','Fort Worth','Camp Bowie Boulevard',190000.00,'In a growing neighborhood, close to new developments',110.00,'011234567'),(16,'Partially Renovated','Charlotte','Tryon Street',360000.00,'In the heart of downtown, close to financial district',180.00,'012345678'),(17,'Furnished','Memphis','Beale Street',230000.00,'Close to the Mississippi River, great views of the city',130.00,'013456789'),(18,'Just the Basis','Boston','Commonwealth Avenue',210000.00,'In a quiet neighborhood, close to parks',100.00,'014567890'),(19,'Partially Renovated','Baltimore','Inner Harbor Boulevard',400000.00,'On the waterfront, great views of the harbor',200.00,'015678901'),(20,'Furnished','Detroit','Woodward Avenue',250000.00,'In the heart of downtown, close to shops and restaurants',140.00,'016789012'),(21,'Just the Basis','El Paso','Cielo Vista Boulevard',180000.00,'Close to the border, great access to Mexico',110.00,'017890123');
/*!40000 ALTER TABLE `realestate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rentalapartment`
--

DROP TABLE IF EXISTS `rentalapartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rentalapartment` (
  `prNum` int NOT NULL,
  `rent` decimal(8,2) NOT NULL,
  PRIMARY KEY (`prNum`),
  CONSTRAINT `rentalapartment_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `apartment` (`prNum`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rentalapartment`
--

LOCK TABLES `rentalapartment` WRITE;
/*!40000 ALTER TABLE `rentalapartment` DISABLE KEYS */;
INSERT INTO `rentalapartment` VALUES (2,2500.00),(6,2000.00),(10,1500.00),(14,1000.00);
/*!40000 ALTER TABLE `rentalapartment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `saleapartment`
--

DROP TABLE IF EXISTS `saleapartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `saleapartment` (
  `prNum` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`prNum`),
  CONSTRAINT `saleapartment_ibfk_1` FOREIGN KEY (`prNum`) REFERENCES `apartment` (`prNum`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `saleapartment`
--

LOCK TABLES `saleapartment` WRITE;
/*!40000 ALTER TABLE `saleapartment` DISABLE KEYS */;
INSERT INTO `saleapartment` VALUES (4,70000.00),(8,60000.00),(12,50000.00);
/*!40000 ALTER TABLE `saleapartment` ENABLE KEYS */;
UNLOCK TABLES;

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

--
-- Dumping data for table `transaction`
--
INSERT INTO `transaction` (`ID`, `paymentDate`, `amount`, `sender`, `recipient`, `contractNo`) VALUES 
('TXN001', '2024-06-01', 5000.00, 'ACC123', 'ACC456', 1),
('TXN002', '2024-06-02', 2000.00, 'ACC789', 'ACC012', 2),
('TXN003', '2024-06-03', 1500.00, 'ACC345', 'ACC678', 3),
('TXN004', '2024-06-04', 7500.00, 'ACC901', 'ACC234', 4),
('TXN005', '2024-06-05', 3000.00, 'ACC567', 'ACC890', 5);
LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-08 18:34:28
