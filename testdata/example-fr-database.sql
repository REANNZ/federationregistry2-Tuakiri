-- MySQL dump 10.13  Distrib 5.1.41, for apple-darwin9.5.0 (i386)
--
-- Host: localhost    Database: federationregistry
-- ------------------------------------------------------
-- Server version	5.1.41-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP DATABASE federationregistry;
CREATE DATABASE federationregistry;
USE federationregistry;

--
-- Table structure for table `_group`
--

DROP TABLE IF EXISTS `_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `external` bit(1) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `protect` bit(1) NOT NULL,
  `realm` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_group`
--

LOCK TABLES `_group` WRITE;
/*!40000 ALTER TABLE `_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_group_roles`
--

DROP TABLE IF EXISTS `_group_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_group_roles` (
  `role_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_id`,`role_id`),
  KEY `FK1C8B2B3E42555646` (`role_id`),
  KEY `FK1C8B2B3E35C9368E` (`group_id`),
  CONSTRAINT `FK1C8B2B3E35C9368E` FOREIGN KEY (`group_id`) REFERENCES `_group` (`id`),
  CONSTRAINT `FK1C8B2B3E42555646` FOREIGN KEY (`role_id`) REFERENCES `_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_group_roles`
--

LOCK TABLES `_group_roles` WRITE;
/*!40000 ALTER TABLE `_group_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `_group_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_group_users`
--

DROP TABLE IF EXISTS `_group_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_group_users` (
  `user_base_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_id`,`user_base_id`),
  KEY `FK1CB72A89FA4F085D` (`user_base_id`),
  KEY `FK1CB72A8935C9368E` (`group_id`),
  CONSTRAINT `FK1CB72A8935C9368E` FOREIGN KEY (`group_id`) REFERENCES `_group` (`id`),
  CONSTRAINT `FK1CB72A89FA4F085D` FOREIGN KEY (`user_base_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_group_users`
--

LOCK TABLES `_group_users` WRITE;
/*!40000 ALTER TABLE `_group_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `_group_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_role`
--

DROP TABLE IF EXISTS `_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `external` bit(1) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `protect` bit(1) NOT NULL,
  `realm` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_role`
--

LOCK TABLES `_role` WRITE;
/*!40000 ALTER TABLE `_role` DISABLE KEYS */;
INSERT INTO `_role` VALUES (1,2,'2010-12-14 11:50:39','Issued to all users','\0','2010-12-14 13:15:36','USER','',NULL),(2,1,'2010-12-14 11:50:39','Assigned to users who are considered to be system wide administrators','\0','2010-12-14 11:51:18','SYSTEM ADMINISTRATOR','',NULL),(3,1,'2010-12-14 11:50:40','Role representing federation level administrators who can make decisions onbehalf of the entire federation, particuarly in workflows','\0','2010-12-14 11:51:18','federation-administrators','\0',NULL),(4,2,'2010-12-14 11:50:50','Global administrators for organization:[id:1, name: dropbear.edu.au, displayName: Dropbear University]','\0','2010-12-14 11:55:15','organization-1-administrators','\0',NULL),(5,1,'2010-12-14 11:50:51','Global administrators for entitydescriptor:[id:1, entityID: https://idp.dropbear.edu.au/idp/shibboleth]','\0','2010-12-14 11:50:51','descriptor-1-administrators','\0',NULL),(6,3,'2010-12-14 11:50:51','Global administrators for idpssodescriptor:[id:3, displayName: Drop Bear IDP]','\0','2010-12-14 11:55:39','descriptor-3-administrators','\0',NULL),(7,4,'2010-12-14 12:51:25','Global administrators for organization:[id:2, name: yowie.edu.au, displayName: University of Yowie]','\0','2010-12-14 13:26:42','organization-2-administrators','\0',NULL),(8,1,'2010-12-14 12:51:26','Global administrators for entitydescriptor:[id:4, entityID: https://idp.yowie.edu.au/idp/shibboleth]','\0','2010-12-14 12:51:26','descriptor-4-administrators','\0',NULL),(9,3,'2010-12-14 12:51:26','Global administrators for idpssodescriptor:[id:6, displayName: Yowie IDP]','\0','2010-12-14 13:26:58','descriptor-6-administrators','\0',NULL),(10,1,'2010-12-14 13:14:36','Global administrators for entitydescriptor:[id:7, entityID: https://shrimp.bbq.dropbear.edu.au/shibboleth]','\0','2010-12-14 13:14:36','descriptor-7-administrators','\0',NULL),(11,1,'2010-12-14 13:14:36','Global administrators for spssodescriptor:[id:8, displayName: How to cook Shrimp]','\0','2010-12-14 13:14:36','descriptor-8-administrators','\0',NULL);
/*!40000 ALTER TABLE `_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_role_users`
--

DROP TABLE IF EXISTS `_role_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_role_users` (
  `user_base_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`user_base_id`),
  KEY `FKADD6169EFA4F085D` (`user_base_id`),
  KEY `FKADD6169E42555646` (`role_id`),
  CONSTRAINT `FKADD6169E42555646` FOREIGN KEY (`role_id`) REFERENCES `_role` (`id`),
  CONSTRAINT `FKADD6169EFA4F085D` FOREIGN KEY (`user_base_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_role_users`
--

LOCK TABLES `_role_users` WRITE;
/*!40000 ALTER TABLE `_role_users` DISABLE KEYS */;
INSERT INTO `_role_users` VALUES (2,1),(2,2),(2,3),(2,4),(2,6),(3,1),(3,7),(3,9);
/*!40000 ALTER TABLE `_role_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_user`
--

DROP TABLE IF EXISTS `_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `action_hash` varchar(255) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `expiration` datetime DEFAULT NULL,
  `external` bit(1) NOT NULL,
  `federated` bit(1) NOT NULL,
  `federation_provider_id` bigint(20) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `profile_id` bigint(20) NOT NULL,
  `realm` varchar(255) DEFAULT NULL,
  `remoteapi` bit(1) NOT NULL,
  `username` varchar(255) NOT NULL,
  `class` varchar(255) NOT NULL,
  `contact_id` bigint(20) DEFAULT NULL,
  `entity_descriptor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `FK571A4AAC365AAEA` (`contact_id`),
  KEY `FK571A4AAD15ADD11` (`entity_descriptor_id`),
  KEY `FK571A4AA6F1935BF` (`profile_id`),
  KEY `FK571A4AA6D69CCD` (`federation_provider_id`),
  CONSTRAINT `FK571A4AA6D69CCD` FOREIGN KEY (`federation_provider_id`) REFERENCES `federation_provider` (`id`),
  CONSTRAINT `FK571A4AA6F1935BF` FOREIGN KEY (`profile_id`) REFERENCES `profile_base` (`id`),
  CONSTRAINT `FK571A4AAC365AAEA` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`),
  CONSTRAINT `FK571A4AAD15ADD11` FOREIGN KEY (`entity_descriptor_id`) REFERENCES `entity_descriptor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_user`
--

LOCK TABLES `_user` WRITE;
/*!40000 ALTER TABLE `_user` DISABLE KEYS */;
INSERT INTO `_user` VALUES (1,0,NULL,'2010-12-14 11:50:40','\0',NULL,'\0','\0',NULL,'2010-12-14 11:50:40',NULL,1,NULL,'\0','internaladministrator','fedreg.host.User',NULL,NULL),(2,14,NULL,'2010-12-14 11:51:18','',NULL,'','',1,'2010-12-14 14:11:02',NULL,2,NULL,'\0','https://idp.dropbear.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1','fedreg.host.User',1,1),(3,6,NULL,'2010-12-14 13:15:36','',NULL,'','',1,'2010-12-14 13:27:16',NULL,3,NULL,'\0','https://idp.yowie.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2','fedreg.host.User',3,4);
/*!40000 ALTER TABLE `_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_user__user`
--

DROP TABLE IF EXISTS `_user__user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_user__user` (
  `user_base_followers_id` bigint(20) DEFAULT NULL,
  `user_base_id` bigint(20) DEFAULT NULL,
  `user_base_follows_id` bigint(20) DEFAULT NULL,
  KEY `FKB31804D5131C54DA` (`user_base_follows_id`),
  KEY `FKB31804D5A829B447` (`user_base_followers_id`),
  KEY `FKB31804D5FA4F085D` (`user_base_id`),
  CONSTRAINT `FKB31804D5FA4F085D` FOREIGN KEY (`user_base_id`) REFERENCES `_user` (`id`),
  CONSTRAINT `FKB31804D5131C54DA` FOREIGN KEY (`user_base_follows_id`) REFERENCES `_user` (`id`),
  CONSTRAINT `FKB31804D5A829B447` FOREIGN KEY (`user_base_followers_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_user__user`
--

LOCK TABLES `_user__user` WRITE;
/*!40000 ALTER TABLE `_user__user` DISABLE KEYS */;
/*!40000 ALTER TABLE `_user__user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_user_passwd_history`
--

DROP TABLE IF EXISTS `_user_passwd_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_user_passwd_history` (
  `user_base_id` bigint(20) DEFAULT NULL,
  `passwd_history_string` varchar(255) DEFAULT NULL,
  KEY `FKA4C16028FA4F085D` (`user_base_id`),
  CONSTRAINT `FKA4C16028FA4F085D` FOREIGN KEY (`user_base_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_user_passwd_history`
--

LOCK TABLES `_user_passwd_history` WRITE;
/*!40000 ALTER TABLE `_user_passwd_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `_user_passwd_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `additional_metadata_location`
--

DROP TABLE IF EXISTS `additional_metadata_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `additional_metadata_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `namespace` varchar(255) NOT NULL,
  `uri_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA1A840AD2493EB1B` (`uri_id`),
  CONSTRAINT `FKA1A840AD2493EB1B` FOREIGN KEY (`uri_id`) REFERENCES `urluri` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `additional_metadata_location`
--

LOCK TABLES `additional_metadata_location` WRITE;
/*!40000 ALTER TABLE `additional_metadata_location` DISABLE KEYS */;
/*!40000 ALTER TABLE `additional_metadata_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `any_uri`
--

DROP TABLE IF EXISTS `any_uri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `any_uri` (
  `id` bigint(20) NOT NULL,
  `contact_id` bigint(20) DEFAULT NULL,
  `descriptor_id` bigint(20) DEFAULT NULL,
  `endpoint_id` bigint(20) DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCDC79899E5FA11AA` (`organization_id`),
  KEY `FKCDC79899ECC1866A` (`endpoint_id`),
  KEY `FKCDC79899C365AAEA` (`contact_id`),
  KEY `FKCDC7989922F46540` (`descriptor_id`),
  CONSTRAINT `FKCDC7989922F46540` FOREIGN KEY (`descriptor_id`) REFERENCES `role_descriptor` (`id`),
  CONSTRAINT `FKCDC79899C365AAEA` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`),
  CONSTRAINT `FKCDC79899E5FA11AA` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`),
  CONSTRAINT `FKCDC79899ECC1866A` FOREIGN KEY (`endpoint_id`) REFERENCES `endpoint` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `any_uri`
--

LOCK TABLES `any_uri` WRITE;
/*!40000 ALTER TABLE `any_uri` DISABLE KEYS */;
INSERT INTO `any_uri` VALUES (27,NULL,NULL,NULL,1),(28,1,NULL,NULL,NULL),(29,NULL,NULL,1,NULL),(30,NULL,NULL,2,NULL),(31,NULL,NULL,3,NULL),(32,NULL,NULL,4,NULL),(33,NULL,NULL,NULL,2),(34,2,NULL,NULL,NULL),(35,NULL,NULL,5,NULL),(36,NULL,NULL,6,NULL),(37,NULL,NULL,7,NULL),(38,NULL,NULL,8,NULL),(39,NULL,NULL,9,NULL),(40,NULL,NULL,10,NULL),(41,NULL,NULL,11,NULL),(42,NULL,NULL,12,NULL),(43,NULL,NULL,13,NULL),(44,NULL,NULL,14,NULL),(45,NULL,NULL,15,NULL),(46,NULL,NULL,16,NULL),(47,NULL,NULL,17,NULL),(48,NULL,NULL,18,NULL),(49,NULL,NULL,19,NULL),(50,3,NULL,NULL,NULL);
/*!40000 ALTER TABLE `any_uri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artifact_resolution_service`
--

DROP TABLE IF EXISTS `artifact_resolution_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artifact_resolution_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA0A175AFC6661FF9` (`descriptor_id`),
  CONSTRAINT `FKA0A175AFC6661FF9` FOREIGN KEY (`descriptor_id`) REFERENCES `ssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artifact_resolution_service`
--

LOCK TABLES `artifact_resolution_service` WRITE;
/*!40000 ALTER TABLE `artifact_resolution_service` DISABLE KEYS */;
INSERT INTO `artifact_resolution_service` VALUES (2,3),(6,6);
/*!40000 ALTER TABLE `artifact_resolution_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assertion_consumer_service`
--

DROP TABLE IF EXISTS `assertion_consumer_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assertion_consumer_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK77B18B09A3A780BC` (`descriptor_id`),
  CONSTRAINT `FK77B18B09A3A780BC` FOREIGN KEY (`descriptor_id`) REFERENCES `spssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assertion_consumer_service`
--

LOCK TABLES `assertion_consumer_service` WRITE;
/*!40000 ALTER TABLE `assertion_consumer_service` DISABLE KEYS */;
INSERT INTO `assertion_consumer_service` VALUES (17,8),(18,8);
/*!40000 ALTER TABLE `assertion_consumer_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assertionidrequest_service`
--

DROP TABLE IF EXISTS `assertionidrequest_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assertionidrequest_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9D0DA7C881D2AAE4` (`descriptor_id`),
  KEY `FK9D0DA7C8C9326391` (`descriptor_id`),
  KEY `FK9D0DA7C81C590EA6` (`descriptor_id`),
  KEY `FK9D0DA7C822F46540` (`descriptor_id`),
  CONSTRAINT `FK9D0DA7C822F46540` FOREIGN KEY (`descriptor_id`) REFERENCES `role_descriptor` (`id`),
  CONSTRAINT `FK9D0DA7C81C590EA6` FOREIGN KEY (`descriptor_id`) REFERENCES `pdpdescriptor` (`id`),
  CONSTRAINT `FK9D0DA7C881D2AAE4` FOREIGN KEY (`descriptor_id`) REFERENCES `idpssodescriptor` (`id`),
  CONSTRAINT `FK9D0DA7C8C9326391` FOREIGN KEY (`descriptor_id`) REFERENCES `attribute_authority_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assertionidrequest_service`
--

LOCK TABLES `assertionidrequest_service` WRITE;
/*!40000 ALTER TABLE `assertionidrequest_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `assertionidrequest_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute`
--

DROP TABLE IF EXISTS `attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `attribute_authority_descriptor_id` bigint(20) DEFAULT NULL,
  `base_id` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `idpssodescriptor_id` bigint(20) DEFAULT NULL,
  `last_updated` datetime NOT NULL,
  `class` varchar(255) NOT NULL,
  `approved` bit(1) DEFAULT NULL,
  `attribute_consuming_service_id` bigint(20) DEFAULT NULL,
  `is_required` bit(1) DEFAULT NULL,
  `reasoning` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC7AA9C59B2AE06` (`base_id`),
  KEY `FKC7AA9CB4F6E596` (`attribute_consuming_service_id`),
  KEY `FKC7AA9C7CB566F2` (`attribute_authority_descriptor_id`),
  KEY `FKC7AA9C914409EA` (`idpssodescriptor_id`),
  CONSTRAINT `FKC7AA9C914409EA` FOREIGN KEY (`idpssodescriptor_id`) REFERENCES `idpssodescriptor` (`id`),
  CONSTRAINT `FKC7AA9C59B2AE06` FOREIGN KEY (`base_id`) REFERENCES `attribute_base` (`id`),
  CONSTRAINT `FKC7AA9C7CB566F2` FOREIGN KEY (`attribute_authority_descriptor_id`) REFERENCES `attribute_authority_descriptor` (`id`),
  CONSTRAINT `FKC7AA9CB4F6E596` FOREIGN KEY (`attribute_consuming_service_id`) REFERENCES `attribute_consuming_service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute`
--

LOCK TABLES `attribute` WRITE;
/*!40000 ALTER TABLE `attribute` DISABLE KEYS */;
INSERT INTO `attribute` VALUES (1,0,NULL,7,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47','fedreg.core.RequestedAttribute','',1,'','To uniquely identify the user'),(2,0,NULL,18,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47','fedreg.core.RequestedAttribute','',1,'','For future notifications about new Shrimp techniques'),(3,0,NULL,20,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47','fedreg.core.RequestedAttribute','',1,'\0','We\'ll SMS BBQ updates as they become available'),(4,0,NULL,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47','fedreg.core.RequestedAttribute','',1,'','For stats purposes'),(5,0,NULL,17,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47','fedreg.core.RequestedAttribute','',1,'\0','For UI customisation and personalisation'),(6,0,NULL,9,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47','fedreg.core.RequestedAttribute','',1,'','Need to ensure users have passed BBQ 101'),(7,0,NULL,14,'2010-12-14 13:28:14',6,'2010-12-14 13:28:14','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(8,0,NULL,17,'2010-12-14 13:28:17',6,'2010-12-14 13:28:17','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(9,0,NULL,9,'2010-12-14 13:28:20',6,'2010-12-14 13:28:20','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(10,0,NULL,18,'2010-12-14 13:28:23',6,'2010-12-14 13:28:23','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(11,0,NULL,21,'2010-12-14 13:28:27',6,'2010-12-14 13:28:27','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(12,0,NULL,22,'2010-12-14 13:28:30',6,'2010-12-14 13:28:30','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(13,0,NULL,1,'2010-12-14 13:28:34',6,'2010-12-14 13:28:34','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(14,0,NULL,7,'2010-12-14 13:28:38',6,'2010-12-14 13:28:38','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(15,0,NULL,5,'2010-12-14 13:39:57',3,'2010-12-14 13:39:57','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(16,0,NULL,8,'2010-12-14 13:39:59',3,'2010-12-14 13:39:59','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(17,0,NULL,11,'2010-12-14 13:40:02',3,'2010-12-14 13:40:02','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(18,0,NULL,19,'2010-12-14 13:40:04',3,'2010-12-14 13:40:04','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(19,0,NULL,3,'2010-12-14 13:40:07',3,'2010-12-14 13:40:07','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(20,0,NULL,14,'2010-12-14 13:40:10',3,'2010-12-14 13:40:10','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(21,0,NULL,17,'2010-12-14 13:40:12',3,'2010-12-14 13:40:12','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(22,0,NULL,18,'2010-12-14 13:40:14',3,'2010-12-14 13:40:14','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(23,0,NULL,9,'2010-12-14 13:40:18',3,'2010-12-14 13:40:18','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(24,0,NULL,16,'2010-12-14 13:40:21',3,'2010-12-14 13:40:21','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(25,0,NULL,21,'2010-12-14 13:40:23',3,'2010-12-14 13:40:23','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(26,0,NULL,22,'2010-12-14 13:40:26',3,'2010-12-14 13:40:26','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(27,0,NULL,6,'2010-12-14 13:40:28',3,'2010-12-14 13:40:28','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(28,0,NULL,20,'2010-12-14 13:40:31',3,'2010-12-14 13:40:31','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(29,0,NULL,1,'2010-12-14 13:40:34',3,'2010-12-14 13:40:34','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(30,0,NULL,2,'2010-12-14 13:40:37',3,'2010-12-14 13:40:37','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(31,0,NULL,13,'2010-12-14 14:11:18',3,'2010-12-14 14:11:18','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(32,0,NULL,10,'2010-12-14 14:11:21',3,'2010-12-14 14:11:21','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(33,0,NULL,4,'2010-12-14 14:11:24',3,'2010-12-14 14:11:24','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(34,0,NULL,15,'2010-12-14 14:11:26',3,'2010-12-14 14:11:26','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(35,0,NULL,7,'2010-12-14 14:11:29',3,'2010-12-14 14:11:29','fedreg.core.Attribute',NULL,NULL,NULL,NULL),(36,0,NULL,12,'2010-12-14 14:11:32',3,'2010-12-14 14:11:32','fedreg.core.Attribute',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_attribute_value`
--

DROP TABLE IF EXISTS `attribute_attribute_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_attribute_value` (
  `attribute_values_id` bigint(20) DEFAULT NULL,
  `attribute_value_id` bigint(20) DEFAULT NULL,
  KEY `FK14CD826B86A429F1` (`attribute_value_id`),
  KEY `FK14CD826B558C8741` (`attribute_values_id`),
  CONSTRAINT `FK14CD826B558C8741` FOREIGN KEY (`attribute_values_id`) REFERENCES `attribute` (`id`),
  CONSTRAINT `FK14CD826B86A429F1` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_value` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_attribute_value`
--

LOCK TABLES `attribute_attribute_value` WRITE;
/*!40000 ALTER TABLE `attribute_attribute_value` DISABLE KEYS */;
INSERT INTO `attribute_attribute_value` VALUES (6,1);
/*!40000 ALTER TABLE `attribute_attribute_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_authority_descriptor`
--

DROP TABLE IF EXISTS `attribute_authority_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_authority_descriptor` (
  `id` bigint(20) NOT NULL,
  `collaborator_id` bigint(20) DEFAULT NULL,
  `entity_descriptor_id` bigint(20) NOT NULL,
  `scope` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK609A73AE3F0CB2ED` (`collaborator_id`),
  KEY `FK609A73AED15ADD11` (`entity_descriptor_id`),
  CONSTRAINT `FK609A73AED15ADD11` FOREIGN KEY (`entity_descriptor_id`) REFERENCES `entity_descriptor` (`id`),
  CONSTRAINT `FK609A73AE3F0CB2ED` FOREIGN KEY (`collaborator_id`) REFERENCES `idpssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_authority_descriptor`
--

LOCK TABLES `attribute_authority_descriptor` WRITE;
/*!40000 ALTER TABLE `attribute_authority_descriptor` DISABLE KEYS */;
INSERT INTO `attribute_authority_descriptor` VALUES (2,3,1,'dropbear.edu.au'),(5,6,4,'yowie.edu.au');
/*!40000 ALTER TABLE `attribute_authority_descriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_authority_descriptor_attribute_profiles`
--

DROP TABLE IF EXISTS `attribute_authority_descriptor_attribute_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_authority_descriptor_attribute_profiles` (
  `attribute_authority_descriptor_id` bigint(20) DEFAULT NULL,
  `attribute_profiles_string` varchar(255) DEFAULT NULL,
  KEY `FK654D0A5E7CB566F2` (`attribute_authority_descriptor_id`),
  CONSTRAINT `FK654D0A5E7CB566F2` FOREIGN KEY (`attribute_authority_descriptor_id`) REFERENCES `attribute_authority_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_authority_descriptor_attribute_profiles`
--

LOCK TABLES `attribute_authority_descriptor_attribute_profiles` WRITE;
/*!40000 ALTER TABLE `attribute_authority_descriptor_attribute_profiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `attribute_authority_descriptor_attribute_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_authority_descriptor_samluri`
--

DROP TABLE IF EXISTS `attribute_authority_descriptor_samluri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_authority_descriptor_samluri` (
  `attribute_authority_descriptor_nameidformats_id` bigint(20) DEFAULT NULL,
  `samluri_id` bigint(20) DEFAULT NULL,
  KEY `FKA6B01E4E3688E26A` (`samluri_id`),
  KEY `FKA6B01E4E2E1B65B` (`attribute_authority_descriptor_nameidformats_id`),
  CONSTRAINT `FKA6B01E4E2E1B65B` FOREIGN KEY (`attribute_authority_descriptor_nameidformats_id`) REFERENCES `attribute_authority_descriptor` (`id`),
  CONSTRAINT `FKA6B01E4E3688E26A` FOREIGN KEY (`samluri_id`) REFERENCES `samluri` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_authority_descriptor_samluri`
--

LOCK TABLES `attribute_authority_descriptor_samluri` WRITE;
/*!40000 ALTER TABLE `attribute_authority_descriptor_samluri` DISABLE KEYS */;
/*!40000 ALTER TABLE `attribute_authority_descriptor_samluri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_base`
--

DROP TABLE IF EXISTS `attribute_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_base` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `friendly_name` varchar(255) NOT NULL,
  `header_name` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `name_format_id` bigint(20) DEFAULT NULL,
  `oid` varchar(255) DEFAULT NULL,
  `specification_required` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK8B0F56947688A89E` (`name_format_id`),
  KEY `FK8B0F5694A5C8CE6` (`category_id`),
  CONSTRAINT `FK8B0F5694A5C8CE6` FOREIGN KEY (`category_id`) REFERENCES `attribute_category` (`id`),
  CONSTRAINT `FK8B0F56947688A89E` FOREIGN KEY (`name_format_id`) REFERENCES `samluri` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_base`
--

LOCK TABLES `attribute_base` WRITE;
/*!40000 ALTER TABLE `attribute_base` DISABLE KEYS */;
INSERT INTO `attribute_base` VALUES (1,0,'organizationName',1,'2010-12-14 11:50:42','Standard name of the top-level organization (institution) with which the user is associated.','Organization Name',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:o',25,'2.5.4.10','\0'),(2,0,'organizationalUnit',2,'2010-12-14 11:50:42','Organizational Unit currently used for faculty membership of staff','Organizational Unit',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:ou',25,'2.5.4.11','\0'),(3,0,'postalAddress',2,'2010-12-14 11:50:42','Business postal address: Campus or office address','Business postal address',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:postalAddress',25,'2.5.4.16','\0'),(4,0,'auEduPersonSharedToken',1,'2010-12-14 11:50:42','A unique identifier enabling federation spanning services such as Grid and Repositories','Shared Token',NULL,'2010-12-14 11:50:42','urn:oid:1.3.6.1.4.1.27856.1.2.5',25,'1.3.6.1.4.1.27856.1.2.5','\0'),(5,0,'auEduPersonAffiliation',2,'2010-12-14 11:50:42','Specifies a persons relationship to the institution in broad categories but with a finer-grained set of permissible values than eduPersonAffiliation.','AU Affiliation',NULL,'2010-12-14 11:50:42','urn:oid:1.3.6.1.4.1.27856.1.2.1',25,'1.3.6.1.4.1.27856.1.2.1','\0'),(6,0,'auEduPersonLegalName',2,'2010-12-14 11:50:42','The users legal name, as per their passport, birth certificate, or other legal document','Legal Name',NULL,'2010-12-14 11:50:42','urn:oid:1.3.6.1.4.1.27856.1.2.2',25,'1.3.6.1.4.1.27856.1.2.2','\0'),(7,0,'eduPersonTargetedID',1,'2010-12-14 11:50:42','A persistent, non-reassigned, privacy-preserving identifier for a principal shared between a pair of coordinating entities','Targeted ID',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:eduPersonTargetedID',25,'1.3.6.1.4.1.5923.1.1.1.10','\0'),(8,0,'eduPersonAffiliation',1,'2010-12-14 11:50:42','Specifies the persons relationship(s) to the institution in broad categories such as student, faculty, staff, alum, etc.','Affiliation',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:eduPersonAffiliation',25,'1.3.6.1.4.1.5923.1.1.1.1','\0'),(9,0,'eduPersonEntitlement',1,'2010-12-14 11:50:42','Member of: URI (either URL or URN) that indicates a set of rights to specific resources based on an agreement across the releavant community','Entitlement',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:eduPersonEntitlement',25,'1.3.6.1.4.1.5923.1.1.1.7',''),(10,0,'eduPersonPrincipalName',2,'2010-12-14 11:50:42','eduPerson per Internet2 and EDUCAUSE','Principal Name',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:eduPersonPrincipalName',25,'1.3.6.1.4.1.5923.1.1.1.6','\0'),(11,0,'eduPersonAssurance',1,'2010-12-14 11:50:42','This attribute represents identity assurance profiles (IAPs), which are the set of standards that are met by an identity assertion, based on the Identity Providers identity management processes, type of auth credential used, binding strength,  etc.','Assurance Level',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:eduPersonAssurance',25,'1.3.6.1.4.1.5923.1.1.1.11','\0'),(12,0,'eduPersonScopedAffiliation',1,'2010-12-14 11:50:42','This attribute enables an organisation to assert its relationship with the user.','eduPersonScopedAffiliation',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:eduPersonScopedAffiliation',25,'1.3.6.1.4.1.5923.1.1.1.9','\0'),(13,0,'eduPersonPrimaryAffiliation',2,'2010-12-14 11:50:42','Specifies the persons PRIMARY relationship to the institution in broad categories such as student, faculty, staff, alum, etc.','Primary Affiliation',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:eduPersonPrimaryAffiliation',25,'1.3.6.1.4.1.5923.1.1.1.5','\0'),(14,0,'commonName',1,'2010-12-14 11:50:42','An individuals common name, typically their full name. This attribute should not be used in transactions where it is desirable to maintain user anonymity.','Common Name',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:cn',25,'2.5.4.3','\0'),(15,0,'surname',2,'2010-12-14 11:50:42','Surname or family name','Surname',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:sn',25,'2.5.4.4','\0'),(16,0,'givenName',2,'2010-12-14 11:50:42','Given name of a person','Given name',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:givenName',25,'2.5.4.42','\0'),(17,0,'displayName',1,'2010-12-14 11:50:42','Preferred name of a person to be used when displaying entries. This attribute should not be used in transactions where it is desirable to maintain user anonymity.','Display Name',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:displayName',25,'2.16.840.1.113730.3.1.241','\0'),(18,0,'email',1,'2010-12-14 11:50:42','Preferred address for e-mail to be sent to this person','Email',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:mail',25,'0.9.2342.19200300.100.1.3','\0'),(19,0,'telephoneNumber',2,'2010-12-14 11:50:42','Office or campus phone number of the individual','Business phone number',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:telephoneNumber',25,'2.5.4.20','\0'),(20,0,'mobileNumber',2,'2010-12-14 11:50:42','Mobile phone number','Mobile phone number',NULL,'2010-12-14 11:50:42','urn:mace:dir:attribute-def:mobile',25,'0.9.2342.19200300.100.1.41','\0'),(21,0,'homeOrganization',2,'2010-12-14 11:50:42','Users Home Organization','Home Organization',NULL,'2010-12-14 11:50:42','urn:oid:1.3.6.1.4.1.25178.1.2.9',25,'1.3.6.1.4.1.25178.1.2.9','\0'),(22,0,'homeOrganizationType',2,'2010-12-14 11:50:42','Type of Organization the user belongs too','Home Organization Type',NULL,'2010-12-14 11:50:42','urn:oid:1.3.6.1.4.1.25178.1.2.10',25,'1.3.6.1.4.1.25178.1.2.10','\0');
/*!40000 ALTER TABLE `attribute_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_category`
--

DROP TABLE IF EXISTS `attribute_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_category`
--

LOCK TABLES `attribute_category` WRITE;
/*!40000 ALTER TABLE `attribute_category` DISABLE KEYS */;
INSERT INTO `attribute_category` VALUES (1,0,'2010-12-14 11:50:42','2010-12-14 11:50:42','Core'),(2,0,'2010-12-14 11:50:42','2010-12-14 11:50:42','Optional');
/*!40000 ALTER TABLE `attribute_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_consuming_service`
--

DROP TABLE IF EXISTS `attribute_consuming_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_consuming_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `approved` bit(1) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  `is_default` bit(1) NOT NULL,
  `lang` varchar(255) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK76FE534CA3A780BC` (`descriptor_id`),
  CONSTRAINT `FK76FE534CA3A780BC` FOREIGN KEY (`descriptor_id`) REFERENCES `spssodescriptor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_consuming_service`
--

LOCK TABLES `attribute_consuming_service` WRITE;
/*!40000 ALTER TABLE `attribute_consuming_service` DISABLE KEYS */;
INSERT INTO `attribute_consuming_service` VALUES (1,0,'','2010-12-14 13:08:47',8,'\0','en','2010-12-14 13:08:47');
/*!40000 ALTER TABLE `attribute_consuming_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_consuming_service_service_descriptions`
--

DROP TABLE IF EXISTS `attribute_consuming_service_service_descriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_consuming_service_service_descriptions` (
  `attribute_consuming_service_id` bigint(20) DEFAULT NULL,
  `service_descriptions_string` varchar(255) DEFAULT NULL,
  KEY `FKC631FC54B4F6E596` (`attribute_consuming_service_id`),
  CONSTRAINT `FKC631FC54B4F6E596` FOREIGN KEY (`attribute_consuming_service_id`) REFERENCES `attribute_consuming_service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_consuming_service_service_descriptions`
--

LOCK TABLES `attribute_consuming_service_service_descriptions` WRITE;
/*!40000 ALTER TABLE `attribute_consuming_service_service_descriptions` DISABLE KEYS */;
INSERT INTO `attribute_consuming_service_service_descriptions` VALUES (1,'A service providing extensive knowledge base on throwing Shrimp on the BBQ the right way.');
/*!40000 ALTER TABLE `attribute_consuming_service_service_descriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_consuming_service_service_names`
--

DROP TABLE IF EXISTS `attribute_consuming_service_service_names`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_consuming_service_service_names` (
  `attribute_consuming_service_id` bigint(20) DEFAULT NULL,
  `service_names_string` varchar(255) DEFAULT NULL,
  KEY `FK9D59D58BB4F6E596` (`attribute_consuming_service_id`),
  CONSTRAINT `FK9D59D58BB4F6E596` FOREIGN KEY (`attribute_consuming_service_id`) REFERENCES `attribute_consuming_service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_consuming_service_service_names`
--

LOCK TABLES `attribute_consuming_service_service_names` WRITE;
/*!40000 ALTER TABLE `attribute_consuming_service_service_names` DISABLE KEYS */;
INSERT INTO `attribute_consuming_service_service_names` VALUES (1,'How to cook Shrimp');
/*!40000 ALTER TABLE `attribute_consuming_service_service_names` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_scope`
--

DROP TABLE IF EXISTS `attribute_scope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_scope` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_scope`
--

LOCK TABLES `attribute_scope` WRITE;
/*!40000 ALTER TABLE `attribute_scope` DISABLE KEYS */;
/*!40000 ALTER TABLE `attribute_scope` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_service`
--

DROP TABLE IF EXISTS `attribute_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1804BC12C9326391` (`descriptor_id`),
  CONSTRAINT `FK1804BC12C9326391` FOREIGN KEY (`descriptor_id`) REFERENCES `attribute_authority_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_service`
--

LOCK TABLES `attribute_service` WRITE;
/*!40000 ALTER TABLE `attribute_service` DISABLE KEYS */;
INSERT INTO `attribute_service` VALUES (1,2),(5,5);
/*!40000 ALTER TABLE `attribute_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute_value`
--

DROP TABLE IF EXISTS `attribute_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `approved` bit(1) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute_value`
--

LOCK TABLES `attribute_value` WRITE;
/*!40000 ALTER TABLE `attribute_value` DISABLE KEYS */;
INSERT INTO `attribute_value` VALUES (1,0,'','2010-12-14 13:08:47','2010-12-14 13:08:47','urn:mace:aaf.edu.au:australia:courses:bbq:101:completed');
/*!40000 ALTER TABLE `attribute_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actor` varchar(255) DEFAULT NULL,
  `class_name` varchar(255) DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `event_name` varchar(255) DEFAULT NULL,
  `last_updated` datetime NOT NULL,
  `new_value` varchar(255) DEFAULT NULL,
  `old_value` varchar(255) DEFAULT NULL,
  `persisted_object_id` bigint(20) DEFAULT NULL,
  `persisted_object_version` bigint(20) DEFAULT NULL,
  `property_name` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=248 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,'system','grails.plugins.nimble.core.Role','2010-12-14 11:50:39','INSERT','2010-12-14 11:50:39',NULL,NULL,1,NULL,NULL,NULL),(2,'system','grails.plugins.nimble.core.Role','2010-12-14 11:50:39','INSERT','2010-12-14 11:50:39',NULL,NULL,2,NULL,NULL,NULL),(3,'system','grails.plugins.nimble.core.FederationProvider','2010-12-14 11:50:40','INSERT','2010-12-14 11:50:40',NULL,NULL,1,NULL,NULL,NULL),(4,'system','grails.plugins.nimble.core.Role','2010-12-14 11:50:40','INSERT','2010-12-14 11:50:40',NULL,NULL,3,NULL,NULL,NULL),(5,'system','fedreg.host.Profile','2010-12-14 11:50:40','INSERT','2010-12-14 11:50:40',NULL,NULL,1,NULL,NULL,NULL),(6,'system','fedreg.host.User','2010-12-14 11:50:40','INSERT','2010-12-14 11:50:40',NULL,NULL,1,NULL,NULL,NULL),(7,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,1,NULL,NULL,NULL),(8,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,2,NULL,NULL,NULL),(9,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,3,NULL,NULL,NULL),(10,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,4,NULL,NULL,NULL),(11,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,5,NULL,NULL,NULL),(12,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,6,NULL,NULL,NULL),(13,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,7,NULL,NULL,NULL),(14,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,8,NULL,NULL,NULL),(15,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,9,NULL,NULL,NULL),(16,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,10,NULL,NULL,NULL),(17,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,11,NULL,NULL,NULL),(18,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,12,NULL,NULL,NULL),(19,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,13,NULL,NULL,NULL),(20,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,14,NULL,NULL,NULL),(21,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,15,NULL,NULL,NULL),(22,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,16,NULL,NULL,NULL),(23,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,17,NULL,NULL,NULL),(24,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,18,NULL,NULL,NULL),(25,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,19,NULL,NULL,NULL),(26,'system','fedreg.core.SamlURI','2010-12-14 11:50:41','INSERT','2010-12-14 11:50:41',NULL,NULL,20,NULL,NULL,NULL),(27,'system','fedreg.core.SamlURI','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,21,NULL,NULL,NULL),(28,'system','fedreg.core.SamlURI','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,22,NULL,NULL,NULL),(29,'system','fedreg.core.SamlURI','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,23,NULL,NULL,NULL),(30,'system','fedreg.core.SamlURI','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,24,NULL,NULL,NULL),(31,'system','fedreg.core.SamlURI','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,25,NULL,NULL,NULL),(32,'system','fedreg.core.SamlURI','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,26,NULL,NULL,NULL),(33,'system','fedreg.core.ContactType','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,1,NULL,NULL,NULL),(34,'system','fedreg.core.ContactType','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,2,NULL,NULL,NULL),(35,'system','fedreg.core.ContactType','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,3,NULL,NULL,NULL),(36,'system','fedreg.core.ContactType','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,4,NULL,NULL,NULL),(37,'system','fedreg.core.ContactType','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,5,NULL,NULL,NULL),(38,'system','fedreg.core.ContactType','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,6,NULL,NULL,NULL),(39,'system','fedreg.core.AttributeCategory','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,1,NULL,NULL,NULL),(40,'system','fedreg.core.AttributeCategory','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,2,NULL,NULL,NULL),(41,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,1,NULL,NULL,NULL),(42,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,2,NULL,NULL,NULL),(43,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,3,NULL,NULL,NULL),(44,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,4,NULL,NULL,NULL),(45,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,5,NULL,NULL,NULL),(46,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,6,NULL,NULL,NULL),(47,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,7,NULL,NULL,NULL),(48,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,8,NULL,NULL,NULL),(49,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,9,NULL,NULL,NULL),(50,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,10,NULL,NULL,NULL),(51,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,11,NULL,NULL,NULL),(52,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,12,NULL,NULL,NULL),(53,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,13,NULL,NULL,NULL),(54,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,14,NULL,NULL,NULL),(55,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,15,NULL,NULL,NULL),(56,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,16,NULL,NULL,NULL),(57,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,17,NULL,NULL,NULL),(58,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,18,NULL,NULL,NULL),(59,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,19,NULL,NULL,NULL),(60,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,20,NULL,NULL,NULL),(61,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,21,NULL,NULL,NULL),(62,'system','fedreg.core.AttributeBase','2010-12-14 11:50:42','INSERT','2010-12-14 11:50:42',NULL,NULL,22,NULL,NULL,NULL),(63,NULL,'fedreg.core.OrganizationType','2010-12-14 11:50:50','INSERT','2010-12-14 11:50:50',NULL,NULL,1,NULL,NULL,NULL),(64,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:50','INSERT','2010-12-14 11:50:50',NULL,NULL,27,NULL,NULL,NULL),(65,NULL,'fedreg.core.Organization','2010-12-14 11:50:50','INSERT','2010-12-14 11:50:50',NULL,NULL,1,NULL,NULL,NULL),(66,NULL,'grails.plugins.nimble.core.Role','2010-12-14 11:50:50','INSERT','2010-12-14 11:50:50',NULL,NULL,4,NULL,NULL,NULL),(67,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:50','UPDATE','2010-12-14 11:50:50','organization:[id:1, name: dropbear.edu.au, displayName: Dropbear University]',NULL,27,NULL,'organization',NULL),(68,NULL,'grails.plugins.nimble.core.LevelPermission','2010-12-14 11:50:50','INSERT','2010-12-14 11:50:50',NULL,NULL,1,NULL,NULL,NULL),(69,NULL,'grails.plugins.nimble.core.Role','2010-12-14 11:50:50','UPDATE','2010-12-14 11:50:50','[grails.plugins.nimble.core.LevelPermission : 1]',NULL,4,NULL,'permissions',NULL),(70,NULL,'fedreg.core.MailURI','2010-12-14 11:50:50','INSERT','2010-12-14 11:50:50',NULL,NULL,28,NULL,NULL,NULL),(71,NULL,'fedreg.core.Contact','2010-12-14 11:50:50','INSERT','2010-12-14 11:50:50',NULL,NULL,1,NULL,NULL,NULL),(72,NULL,'fedreg.core.EntityDescriptor','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,1,NULL,NULL,NULL),(73,NULL,'fedreg.core.AttributeAuthorityDescriptor','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,2,NULL,NULL,NULL),(74,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,29,NULL,NULL,NULL),(75,NULL,'fedreg.core.AttributeService','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,1,NULL,NULL,NULL),(76,NULL,'fedreg.core.ContactPerson','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,1,NULL,NULL,NULL),(77,NULL,'fedreg.core.IDPSSODescriptor','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,3,NULL,NULL,NULL),(78,NULL,'fedreg.core.ContactPerson','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,2,NULL,NULL,NULL),(79,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,30,NULL,NULL,NULL),(80,NULL,'fedreg.core.ArtifactResolutionService','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,2,NULL,NULL,NULL),(81,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,31,NULL,NULL,NULL),(82,NULL,'fedreg.core.SingleSignOnService','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,3,NULL,NULL,NULL),(83,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,32,NULL,NULL,NULL),(84,NULL,'fedreg.core.SingleSignOnService','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,4,NULL,NULL,NULL),(85,NULL,'grails.plugins.nimble.core.Role','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,5,NULL,NULL,NULL),(86,NULL,'fedreg.core.MailURI','2010-12-14 11:50:51','UPDATE','2010-12-14 11:50:51','contact:[id:1, givenName: Mick, surname: Dundee, email: mailuri:[id:28, uri: thatsaknife@dropbear.edu.au]]',NULL,28,NULL,'contact',NULL),(87,NULL,'fedreg.core.AttributeAuthorityDescriptor','2010-12-14 11:50:51','UPDATE','2010-12-14 11:50:51','idpssodescriptor:[id:3, displayName: Drop Bear IDP]',NULL,2,NULL,'collaborator',NULL),(88,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:51','UPDATE','2010-12-14 11:50:51','attributeservice:[id:1, location: urluri:[id:29, uri: https://idp.dropbear.edu.au/idp/profile/SAML2/SOAP/AttributeQuery]]',NULL,29,NULL,'endpoint',NULL),(89,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:51','UPDATE','2010-12-14 11:50:51','artifactresolutionservice:[id:2, location: urluri:[id:30, uri: https://idp.dropbear.edu.au/idp/profile/SAML2/SOAP/ArtifactResolution]]',NULL,30,NULL,'endpoint',NULL),(90,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:51','UPDATE','2010-12-14 11:50:51','singlesignonservice:[id:3, location: urluri:[id:31, uri: https://idp.dropbear.edu.au/idp/profile/SAML2/POST/SSO]]',NULL,31,NULL,'endpoint',NULL),(91,NULL,'fedreg.core.UrlURI','2010-12-14 11:50:51','UPDATE','2010-12-14 11:50:51','singlesignonservice:[id:4, location: urluri:[id:32, uri: https://idp.dropbear.edu.au/idp/profile/SAML2/Redirect/SSO]]',NULL,32,NULL,'endpoint',NULL),(92,NULL,'grails.plugins.nimble.core.LevelPermission','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,2,NULL,NULL,NULL),(93,NULL,'grails.plugins.nimble.core.Role','2010-12-14 11:50:51','UPDATE','2010-12-14 11:50:51','[grails.plugins.nimble.core.LevelPermission : 2]',NULL,5,NULL,'permissions',NULL),(94,NULL,'grails.plugins.nimble.core.Role','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,6,NULL,NULL,NULL),(95,NULL,'grails.plugins.nimble.core.LevelPermission','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,3,NULL,NULL,NULL),(96,NULL,'grails.plugins.nimble.core.Role','2010-12-14 11:50:51','UPDATE','2010-12-14 11:50:51','[grails.plugins.nimble.core.LevelPermission : 3]',NULL,6,NULL,'permissions',NULL),(97,NULL,'grails.plugins.nimble.core.LevelPermission','2010-12-14 11:50:51','INSERT','2010-12-14 11:50:51',NULL,NULL,4,NULL,NULL,NULL),(98,NULL,'fedreg.host.Profile','2010-12-14 11:51:18','INSERT','2010-12-14 11:51:18',NULL,NULL,2,NULL,NULL,NULL),(99,NULL,'fedreg.host.User','2010-12-14 11:51:18','INSERT','2010-12-14 11:51:18',NULL,NULL,2,NULL,NULL,NULL),(100,NULL,'grails.plugins.nimble.core.Permission','2010-12-14 11:51:18','INSERT','2010-12-14 11:51:18',NULL,NULL,5,NULL,NULL,NULL),(101,NULL,'grails.plugins.nimble.core.Role','2010-12-14 11:51:18','UPDATE','2010-12-14 11:51:18','[user:[id:2, username: https://idp.dropbear.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1, name:Mick Dundee]]','[]',1,NULL,'users',NULL),(102,NULL,'fedreg.host.User','2010-12-14 11:51:18','UPDATE','2010-12-14 11:51:18','[grails.plugins.nimble.core.Permission : 5]',NULL,2,NULL,'permissions',NULL),(103,NULL,'grails.plugins.nimble.core.Permission','2010-12-14 11:51:18','INSERT','2010-12-14 11:51:18',NULL,NULL,6,NULL,NULL,NULL),(104,NULL,'fedreg.host.User','2010-12-14 11:51:18','UPDATE','2010-12-14 11:51:18','contact:[id:1, givenName: Mick, surname: Dundee, email: mailuri:[id:28, uri: thatsaknife@dropbear.edu.au]]',NULL,2,NULL,'contact',NULL),(105,'2','fedreg.host.User','2010-12-14 11:51:18','UPDATE','2010-12-14 11:51:18','[grails.plugins.nimble.core.LoginRecord : 1]',NULL,2,NULL,'loginRecords',NULL),(106,'2','fedreg.core.Certificate','2010-12-14 12:49:02','INSERT','2010-12-14 12:49:02',NULL,NULL,1,NULL,NULL,NULL),(107,'2','fedreg.core.KeyInfo','2010-12-14 12:49:02','INSERT','2010-12-14 12:49:02',NULL,NULL,1,NULL,NULL,NULL),(108,'2','fedreg.core.KeyDescriptor','2010-12-14 12:49:02','INSERT','2010-12-14 12:49:02',NULL,NULL,1,NULL,NULL,NULL),(109,'2','fedreg.core.UrlURI','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,33,NULL,NULL,NULL),(110,'2','fedreg.core.Organization','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,2,NULL,NULL,NULL),(111,'2','grails.plugins.nimble.core.Role','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,7,NULL,NULL,NULL),(112,'2','fedreg.core.UrlURI','2010-12-14 12:51:25','UPDATE','2010-12-14 12:51:25','organization:[id:2, name: yowie.edu.au, displayName: University of Yowie]',NULL,33,NULL,'organization',NULL),(113,'2','grails.plugins.nimble.core.LevelPermission','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,7,NULL,NULL,NULL),(114,'2','grails.plugins.nimble.core.Role','2010-12-14 12:51:25','UPDATE','2010-12-14 12:51:25','[grails.plugins.nimble.core.LevelPermission : 7]',NULL,7,NULL,'permissions',NULL),(115,'2','fedreg.core.MailURI','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,34,NULL,NULL,NULL),(116,'2','fedreg.core.Contact','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,2,NULL,NULL,NULL),(117,'2','fedreg.core.EntityDescriptor','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,4,NULL,NULL,NULL),(118,'2','fedreg.core.AttributeAuthorityDescriptor','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,5,NULL,NULL,NULL),(119,'2','fedreg.core.UrlURI','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,35,NULL,NULL,NULL),(120,'2','fedreg.core.AttributeService','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,5,NULL,NULL,NULL),(121,'2','fedreg.core.ContactPerson','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,3,NULL,NULL,NULL),(122,'2','fedreg.core.IDPSSODescriptor','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,6,NULL,NULL,NULL),(123,'2','fedreg.core.ContactPerson','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,4,NULL,NULL,NULL),(124,'2','fedreg.core.UrlURI','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,36,NULL,NULL,NULL),(125,'2','fedreg.core.ArtifactResolutionService','2010-12-14 12:51:25','INSERT','2010-12-14 12:51:25',NULL,NULL,6,NULL,NULL,NULL),(126,'2','fedreg.core.UrlURI','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,37,NULL,NULL,NULL),(127,'2','fedreg.core.SingleSignOnService','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,7,NULL,NULL,NULL),(128,'2','fedreg.core.UrlURI','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,38,NULL,NULL,NULL),(129,'2','fedreg.core.SingleSignOnService','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,8,NULL,NULL,NULL),(130,'2','grails.plugins.nimble.core.Role','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,8,NULL,NULL,NULL),(131,'2','fedreg.core.MailURI','2010-12-14 12:51:26','UPDATE','2010-12-14 12:51:26','contact:[id:2, givenName: Steve, surname: Irwin, email: mailuri:[id:34, uri: crikey@yowie.edu.au]]',NULL,34,NULL,'contact',NULL),(132,'2','fedreg.core.AttributeAuthorityDescriptor','2010-12-14 12:51:26','UPDATE','2010-12-14 12:51:26','idpssodescriptor:[id:6, displayName: Yowie IDP]',NULL,5,NULL,'collaborator',NULL),(133,'2','fedreg.core.UrlURI','2010-12-14 12:51:26','UPDATE','2010-12-14 12:51:26','attributeservice:[id:5, location: urluri:[id:35, uri: https://idp.yowie.edu.au/idp/profile/SAML2/SOAP/AttributeQuery]]',NULL,35,NULL,'endpoint',NULL),(134,'2','fedreg.core.UrlURI','2010-12-14 12:51:26','UPDATE','2010-12-14 12:51:26','artifactresolutionservice:[id:6, location: urluri:[id:36, uri: https://idp.yowie.edu.au/idp/profile/SAML2/SOAP/ArtifactResolution]]',NULL,36,NULL,'endpoint',NULL),(135,'2','fedreg.core.UrlURI','2010-12-14 12:51:26','UPDATE','2010-12-14 12:51:26','singlesignonservice:[id:7, location: urluri:[id:37, uri: https://idp.yowie.edu.au/idp/profile/SAML2/POST/SSO]]',NULL,37,NULL,'endpoint',NULL),(136,'2','fedreg.core.UrlURI','2010-12-14 12:51:26','UPDATE','2010-12-14 12:51:26','singlesignonservice:[id:8, location: urluri:[id:38, uri: https://idp.yowie.edu.au/idp/profile/SAML2/Redirect/SSO]]',NULL,38,NULL,'endpoint',NULL),(137,'2','grails.plugins.nimble.core.LevelPermission','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,8,NULL,NULL,NULL),(138,'2','grails.plugins.nimble.core.Role','2010-12-14 12:51:26','UPDATE','2010-12-14 12:51:26','[grails.plugins.nimble.core.LevelPermission : 8]',NULL,8,NULL,'permissions',NULL),(139,'2','grails.plugins.nimble.core.Role','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,9,NULL,NULL,NULL),(140,'2','grails.plugins.nimble.core.LevelPermission','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,9,NULL,NULL,NULL),(141,'2','grails.plugins.nimble.core.Role','2010-12-14 12:51:26','UPDATE','2010-12-14 12:51:26','[grails.plugins.nimble.core.LevelPermission : 9]',NULL,9,NULL,'permissions',NULL),(142,'2','grails.plugins.nimble.core.LevelPermission','2010-12-14 12:51:26','INSERT','2010-12-14 12:51:26',NULL,NULL,10,NULL,NULL,NULL),(143,'2','fedreg.core.EntitiesDescriptor','2010-12-14 12:54:34','INSERT','2010-12-14 12:54:34',NULL,NULL,1,NULL,NULL,NULL),(144,'2','fedreg.core.Certificate','2010-12-14 12:58:22','INSERT','2010-12-14 12:58:22',NULL,NULL,2,NULL,NULL,NULL),(145,'2','fedreg.core.KeyInfo','2010-12-14 12:58:22','INSERT','2010-12-14 12:58:22',NULL,NULL,2,NULL,NULL,NULL),(146,'2','fedreg.core.KeyDescriptor','2010-12-14 12:58:22','INSERT','2010-12-14 12:58:22',NULL,NULL,2,NULL,NULL,NULL),(147,'2','fedreg.core.EntityDescriptor','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,7,NULL,NULL,NULL),(148,'2','fedreg.core.ContactPerson','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,5,NULL,NULL,NULL),(149,'2','fedreg.core.ServiceDescription','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,1,NULL,NULL,NULL),(150,'2','fedreg.core.SPSSODescriptor','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,8,NULL,NULL,NULL),(151,'2','fedreg.core.ContactPerson','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,6,NULL,NULL,NULL),(152,'2','fedreg.core.Certificate','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,3,NULL,NULL,NULL),(153,'2','fedreg.core.KeyInfo','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,3,NULL,NULL,NULL),(154,'2','fedreg.core.KeyDescriptor','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,3,NULL,NULL,NULL),(155,'2','fedreg.core.Certificate','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,4,NULL,NULL,NULL),(156,'2','fedreg.core.KeyInfo','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,4,NULL,NULL,NULL),(157,'2','fedreg.core.KeyDescriptor','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,4,NULL,NULL,NULL),(158,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,39,NULL,NULL,NULL),(159,'2','fedreg.core.ManageNameIDService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,9,NULL,NULL,NULL),(160,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,40,NULL,NULL,NULL),(161,'2','fedreg.core.ManageNameIDService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,10,NULL,NULL,NULL),(162,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,41,NULL,NULL,NULL),(163,'2','fedreg.core.ManageNameIDService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,11,NULL,NULL,NULL),(164,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,42,NULL,NULL,NULL),(165,'2','fedreg.core.ManageNameIDService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,12,NULL,NULL,NULL),(166,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,43,NULL,NULL,NULL),(167,'2','fedreg.core.SingleLogoutService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,13,NULL,NULL,NULL),(168,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,44,NULL,NULL,NULL),(169,'2','fedreg.core.SingleLogoutService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,14,NULL,NULL,NULL),(170,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,45,NULL,NULL,NULL),(171,'2','fedreg.core.SingleLogoutService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,15,NULL,NULL,NULL),(172,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,46,NULL,NULL,NULL),(173,'2','fedreg.core.SingleLogoutService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,16,NULL,NULL,NULL),(174,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,47,NULL,NULL,NULL),(175,'2','fedreg.core.AssertionConsumerService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,17,NULL,NULL,NULL),(176,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,48,NULL,NULL,NULL),(177,'2','fedreg.core.AssertionConsumerService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,18,NULL,NULL,NULL),(178,'2','fedreg.core.AttributeConsumingService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,1,NULL,NULL,NULL),(179,'2','fedreg.core.RequestedAttribute','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,1,NULL,NULL,NULL),(180,'2','fedreg.core.RequestedAttribute','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,2,NULL,NULL,NULL),(181,'2','fedreg.core.RequestedAttribute','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,3,NULL,NULL,NULL),(182,'2','fedreg.core.RequestedAttribute','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,4,NULL,NULL,NULL),(183,'2','fedreg.core.RequestedAttribute','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,5,NULL,NULL,NULL),(184,'2','fedreg.core.RequestedAttribute','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,6,NULL,NULL,NULL),(185,'2','fedreg.core.AttributeValue','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,1,NULL,NULL,NULL),(186,'2','fedreg.core.UrlURI','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,49,NULL,NULL,NULL),(187,'2','fedreg.core.DiscoveryResponseService','2010-12-14 13:08:47','INSERT','2010-12-14 13:08:47',NULL,NULL,19,NULL,NULL,NULL),(188,'2','fedreg.core.EntityDescriptor','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','[spssodescriptor:[id:8, displayName: How to cook Shrimp]]',NULL,7,NULL,'spDescriptors',NULL),(189,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','managenameidservice:[id:9, location: urluri:[id:39, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/NIM/Redirect]]',NULL,39,NULL,'endpoint',NULL),(190,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','managenameidservice:[id:10, location: urluri:[id:40, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/NIM/POST]]',NULL,40,NULL,'endpoint',NULL),(191,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','managenameidservice:[id:11, location: urluri:[id:41, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/NIM/Artifact]]',NULL,41,NULL,'endpoint',NULL),(192,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','managenameidservice:[id:12, location: urluri:[id:42, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/NIM/SOAP]]',NULL,42,NULL,'endpoint',NULL),(193,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','singlelogoutservice:[id:13, location: urluri:[id:43, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SLO/SOAP]]',NULL,43,NULL,'endpoint',NULL),(194,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','singlelogoutservice:[id:14, location: urluri:[id:44, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SLO/Artifact]]',NULL,44,NULL,'endpoint',NULL),(195,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','singlelogoutservice:[id:15, location: urluri:[id:45, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SLO/POST]]',NULL,45,NULL,'endpoint',NULL),(196,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','singlelogoutservice:[id:16, location: urluri:[id:46, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SLO/Redirect]]',NULL,46,NULL,'endpoint',NULL),(197,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','assertionconsumerservice:[id:17, location: urluri:[id:47, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SAML2/Artifact]]',NULL,47,NULL,'endpoint',NULL),(198,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','assertionconsumerservice:[id:18, location: urluri:[id:48, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SAML2/POST]]',NULL,48,NULL,'endpoint',NULL),(199,'2','fedreg.core.UrlURI','2010-12-14 13:08:49','UPDATE','2010-12-14 13:08:49','discoveryresponseservice:[id:19, location: urluri:[id:49, uri: https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/DS]]',NULL,49,NULL,'endpoint',NULL),(200,'2','fedreg.core.MonitorType','2010-12-14 13:12:38','INSERT','2010-12-14 13:12:38',NULL,NULL,1,NULL,NULL,NULL),(201,'2','grails.plugins.nimble.core.Role','2010-12-14 13:14:36','INSERT','2010-12-14 13:14:36',NULL,NULL,10,NULL,NULL,NULL),(202,'2','grails.plugins.nimble.core.LevelPermission','2010-12-14 13:14:36','INSERT','2010-12-14 13:14:36',NULL,NULL,11,NULL,NULL,NULL),(203,'2','fedreg.core.SPSSODescriptor','2010-12-14 13:14:36','UPDATE','2010-12-14 13:14:36','true','false',8,NULL,'approved',NULL),(204,'2','fedreg.core.EntityDescriptor','2010-12-14 13:14:36','UPDATE','2010-12-14 13:14:36','true','false',7,NULL,'approved',NULL),(205,'2','grails.plugins.nimble.core.Role','2010-12-14 13:14:36','UPDATE','2010-12-14 13:14:36','[grails.plugins.nimble.core.LevelPermission : 11]',NULL,10,NULL,'permissions',NULL),(206,'2','grails.plugins.nimble.core.Role','2010-12-14 13:14:36','INSERT','2010-12-14 13:14:36',NULL,NULL,11,NULL,NULL,NULL),(207,'2','grails.plugins.nimble.core.LevelPermission','2010-12-14 13:14:36','INSERT','2010-12-14 13:14:36',NULL,NULL,12,NULL,NULL,NULL),(208,'2','grails.plugins.nimble.core.Role','2010-12-14 13:14:36','UPDATE','2010-12-14 13:14:36','[grails.plugins.nimble.core.LevelPermission : 12]',NULL,11,NULL,'permissions',NULL),(209,NULL,'fedreg.host.Profile','2010-12-14 13:15:36','INSERT','2010-12-14 13:15:36',NULL,NULL,3,NULL,NULL,NULL),(210,NULL,'fedreg.host.User','2010-12-14 13:15:36','INSERT','2010-12-14 13:15:36',NULL,NULL,3,NULL,NULL,NULL),(211,NULL,'grails.plugins.nimble.core.Permission','2010-12-14 13:15:36','INSERT','2010-12-14 13:15:36',NULL,NULL,13,NULL,NULL,NULL),(212,NULL,'fedreg.host.User','2010-12-14 13:15:36','UPDATE','2010-12-14 13:15:36','[grails.plugins.nimble.core.Permission : 13]',NULL,3,NULL,'permissions',NULL),(213,NULL,'fedreg.core.MailURI','2010-12-14 13:15:36','INSERT','2010-12-14 13:15:36',NULL,NULL,50,NULL,NULL,NULL),(214,NULL,'fedreg.core.Contact','2010-12-14 13:15:36','INSERT','2010-12-14 13:15:36',NULL,NULL,3,NULL,NULL,NULL),(215,NULL,'fedreg.host.User','2010-12-14 13:15:36','UPDATE','2010-12-14 13:15:36','contact:[id:3, givenName: Steve, surname: Irwin, email: mailuri:[id:50, uri: crikey@yowie.com.au]]',NULL,3,NULL,'contact',NULL),(216,NULL,'fedreg.core.MailURI','2010-12-14 13:15:36','UPDATE','2010-12-14 13:15:36','contact:[id:3, givenName: Steve, surname: Irwin, email: mailuri:[id:50, uri: crikey@yowie.com.au]]',NULL,50,NULL,'contact',NULL),(217,'3','fedreg.host.User','2010-12-14 13:15:36','UPDATE','2010-12-14 13:15:36','[grails.plugins.nimble.core.LoginRecord : 3]',NULL,3,NULL,'loginRecords',NULL),(218,'3','fedreg.core.Attribute','2010-12-14 13:28:14','INSERT','2010-12-14 13:28:14',NULL,NULL,7,NULL,NULL,NULL),(219,'3','fedreg.core.Attribute','2010-12-14 13:28:17','INSERT','2010-12-14 13:28:17',NULL,NULL,8,NULL,NULL,NULL),(220,'3','fedreg.core.Attribute','2010-12-14 13:28:20','INSERT','2010-12-14 13:28:20',NULL,NULL,9,NULL,NULL,NULL),(221,'3','fedreg.core.Attribute','2010-12-14 13:28:23','INSERT','2010-12-14 13:28:23',NULL,NULL,10,NULL,NULL,NULL),(222,'3','fedreg.core.Attribute','2010-12-14 13:28:27','INSERT','2010-12-14 13:28:27',NULL,NULL,11,NULL,NULL,NULL),(223,'3','fedreg.core.Attribute','2010-12-14 13:28:30','INSERT','2010-12-14 13:28:30',NULL,NULL,12,NULL,NULL,NULL),(224,'3','fedreg.core.Attribute','2010-12-14 13:28:34','INSERT','2010-12-14 13:28:34',NULL,NULL,13,NULL,NULL,NULL),(225,'3','fedreg.core.Attribute','2010-12-14 13:28:38','INSERT','2010-12-14 13:28:38',NULL,NULL,14,NULL,NULL,NULL),(226,'2','fedreg.core.Attribute','2010-12-14 13:39:57','INSERT','2010-12-14 13:39:57',NULL,NULL,15,NULL,NULL,NULL),(227,'2','fedreg.core.Attribute','2010-12-14 13:39:59','INSERT','2010-12-14 13:39:59',NULL,NULL,16,NULL,NULL,NULL),(228,'2','fedreg.core.Attribute','2010-12-14 13:40:02','INSERT','2010-12-14 13:40:02',NULL,NULL,17,NULL,NULL,NULL),(229,'2','fedreg.core.Attribute','2010-12-14 13:40:04','INSERT','2010-12-14 13:40:04',NULL,NULL,18,NULL,NULL,NULL),(230,'2','fedreg.core.Attribute','2010-12-14 13:40:07','INSERT','2010-12-14 13:40:07',NULL,NULL,19,NULL,NULL,NULL),(231,'2','fedreg.core.Attribute','2010-12-14 13:40:10','INSERT','2010-12-14 13:40:10',NULL,NULL,20,NULL,NULL,NULL),(232,'2','fedreg.core.Attribute','2010-12-14 13:40:12','INSERT','2010-12-14 13:40:12',NULL,NULL,21,NULL,NULL,NULL),(233,'2','fedreg.core.Attribute','2010-12-14 13:40:14','INSERT','2010-12-14 13:40:14',NULL,NULL,22,NULL,NULL,NULL),(234,'2','fedreg.core.Attribute','2010-12-14 13:40:18','INSERT','2010-12-14 13:40:18',NULL,NULL,23,NULL,NULL,NULL),(235,'2','fedreg.core.Attribute','2010-12-14 13:40:21','INSERT','2010-12-14 13:40:21',NULL,NULL,24,NULL,NULL,NULL),(236,'2','fedreg.core.Attribute','2010-12-14 13:40:23','INSERT','2010-12-14 13:40:23',NULL,NULL,25,NULL,NULL,NULL),(237,'2','fedreg.core.Attribute','2010-12-14 13:40:26','INSERT','2010-12-14 13:40:26',NULL,NULL,26,NULL,NULL,NULL),(238,'2','fedreg.core.Attribute','2010-12-14 13:40:28','INSERT','2010-12-14 13:40:28',NULL,NULL,27,NULL,NULL,NULL),(239,'2','fedreg.core.Attribute','2010-12-14 13:40:31','INSERT','2010-12-14 13:40:31',NULL,NULL,28,NULL,NULL,NULL),(240,'2','fedreg.core.Attribute','2010-12-14 13:40:34','INSERT','2010-12-14 13:40:34',NULL,NULL,29,NULL,NULL,NULL),(241,'2','fedreg.core.Attribute','2010-12-14 13:40:37','INSERT','2010-12-14 13:40:37',NULL,NULL,30,NULL,NULL,NULL),(242,'2','fedreg.core.Attribute','2010-12-14 14:11:18','INSERT','2010-12-14 14:11:18',NULL,NULL,31,NULL,NULL,NULL),(243,'2','fedreg.core.Attribute','2010-12-14 14:11:21','INSERT','2010-12-14 14:11:21',NULL,NULL,32,NULL,NULL,NULL),(244,'2','fedreg.core.Attribute','2010-12-14 14:11:24','INSERT','2010-12-14 14:11:24',NULL,NULL,33,NULL,NULL,NULL),(245,'2','fedreg.core.Attribute','2010-12-14 14:11:26','INSERT','2010-12-14 14:11:26',NULL,NULL,34,NULL,NULL,NULL),(246,'2','fedreg.core.Attribute','2010-12-14 14:11:29','INSERT','2010-12-14 14:11:29',NULL,NULL,35,NULL,NULL,NULL),(247,'2','fedreg.core.Attribute','2010-12-14 14:11:32','INSERT','2010-12-14 14:11:32',NULL,NULL,36,NULL,NULL,NULL);
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authz_service`
--

DROP TABLE IF EXISTS `authz_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authz_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK44BD53481C590EA6` (`descriptor_id`),
  CONSTRAINT `FK44BD53481C590EA6` FOREIGN KEY (`descriptor_id`) REFERENCES `pdpdescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authz_service`
--

LOCK TABLES `authz_service` WRITE;
/*!40000 ALTER TABLE `authz_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `authz_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cacertificate`
--

DROP TABLE IF EXISTS `cacertificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cacertificate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `data` longtext NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cacertificate`
--

LOCK TABLES `cacertificate` WRITE;
/*!40000 ALTER TABLE `cacertificate` DISABLE KEYS */;
/*!40000 ALTER TABLE `cacertificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cakey_info`
--

DROP TABLE IF EXISTS `cakey_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cakey_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `certificate_id` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC42E70EC2590286C` (`certificate_id`),
  CONSTRAINT `FKC42E70EC2590286C` FOREIGN KEY (`certificate_id`) REFERENCES `cacertificate` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cakey_info`
--

LOCK TABLES `cakey_info` WRITE;
/*!40000 ALTER TABLE `cakey_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `cakey_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_support_status`
--

DROP TABLE IF EXISTS `category_support_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_support_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `supported_count` int(11) NOT NULL,
  `total_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_support_status`
--

LOCK TABLES `category_support_status` WRITE;
/*!40000 ALTER TABLE `category_support_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `category_support_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificate`
--

DROP TABLE IF EXISTS `certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `certificate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `data` longtext NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `expiry_date` datetime NOT NULL,
  `issuer` longtext NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `subject` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
INSERT INTO `certificate` VALUES (1,0,'-----BEGIN CERTIFICATE-----\nMIICWDCCAcGgAwIBAgIJANgNhAIWMDl+MA0GCSqGSIb3DQEBBQUAMEUxCzAJBgNV\nBAYTAkFVMQwwCgYDVQQIDANRTEQxCjAIBgNVBAoMASAxHDAaBgNVBAMME2lkcC5k\ncm9wYmVhci5lZHUuYXUwHhcNMTAxMjE0MDI0ODE5WhcNMTExMjE0MDI0ODE5WjBF\nMQswCQYDVQQGEwJBVTEMMAoGA1UECAwDUUxEMQowCAYDVQQKDAEgMRwwGgYDVQQD\nDBNpZHAuZHJvcGJlYXIuZWR1LmF1MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKB\ngQDWDOarM4aRHHRQhkRJK7gw2ip3JAKjhOzp6k5YzKvRvMPS0NQnRH27CMPaXOCr\n+kypMDMJbpRsCfzwqY4naJCv2Qh4RlCXmVOTcBIv02j8ydM7mPejzvYdHOH5OpoU\nUza04e4IQvkvohp0d0Dx99E6Q83eJcvbPbtVdsRLSdgsgQIDAQABo1AwTjAdBgNV\nHQ4EFgQUqePegl3pzOrsFkXxB2ckG6a9NVwwHwYDVR0jBBgwFoAUqePegl3pzOrs\nFkXxB2ckG6a9NVwwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQAvcB89\nZuJTUglmfZvQXEq0QHUX9r4+bZRaNK/KmeQ8HNaWRQ8iRvOZyv3yvKLiwUdvS2eP\nCwTR4j9LbGmnDtVcUxHYW+tqXghrlWWVORglhoFQR0oK/YlAzZSKfyV0xfz2tJIf\nogs9ShbQJ9gMVxhUchnFJx2GM1mcn6nZTRkSYQ==\n-----END CERTIFICATE-----','2010-12-14 12:49:02','2011-12-14 12:48:19','CN=idp.dropbear.edu.au,O=\\ ,ST=QLD,C=AU','2010-12-14 12:49:02','CN=idp.dropbear.edu.au,O=\\ ,ST=QLD,C=AU'),(2,0,'-----BEGIN CERTIFICATE-----\nMIICcDCCAdmgAwIBAgIJAIpa++wUHz3ZMA0GCSqGSIb3DQEBBQUAMFExCzAJBgNV\nBAYTAkFVMQwwCgYDVQQIDANRTEQxGTAXBgNVBAoMEFlvd2llIFVuaXZlcnNpdHkx\nGTAXBgNVBAMMEGlkcC55b3dpZS5lZHUuYXUwHhcNMTAxMjE0MDI1MjEzWhcNMTEx\nMjE0MDI1MjEzWjBRMQswCQYDVQQGEwJBVTEMMAoGA1UECAwDUUxEMRkwFwYDVQQK\nDBBZb3dpZSBVbml2ZXJzaXR5MRkwFwYDVQQDDBBpZHAueW93aWUuZWR1LmF1MIGf\nMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNRQyeW0LoTVJeIK+7jhTpQqFQjART\nrqE1MGzoQd6dZM2xTNzD0YLPz3aTX/pp5tPjMohZ8HzDmgNpQ0qRSBZkthOGiEFn\nomawNmju9+qggwfRU5FiY97ZrpAk4MTju5gGRfk+ab6G/Ob2NBLfOdhN0eVUHyds\ncP+CdAACBwzHRwIDAQABo1AwTjAdBgNVHQ4EFgQUAoQVZM15mZljq/D3N4P/ORuw\naOMwHwYDVR0jBBgwFoAUAoQVZM15mZljq/D3N4P/ORuwaOMwDAYDVR0TBAUwAwEB\n/zANBgkqhkiG9w0BAQUFAAOBgQA0RZm/BEMLBhSaQdzh/fnaEW35mMwG42KmOWWc\no4jl9R6TXPyZQeYvFmX4QDjgz96nPHg3a3449QcTxiM2BXBZoPIUSF3VO+tzIEbj\n3JoJfNUqXZTu2XlBgXTH4fzcu1yP0Au9GviNqyqqJyi839tYiYzNsa4amLSj/1M7\nqaOwkg==\n-----END CERTIFICATE-----','2010-12-14 12:58:22','2011-12-14 12:52:13','CN=idp.yowie.edu.au,O=Yowie University,ST=QLD,C=AU','2010-12-14 12:58:22','CN=idp.yowie.edu.au,O=Yowie University,ST=QLD,C=AU'),(3,0,'-----BEGIN CERTIFICATE-----\nMIICijCCAfOgAwIBAgIJAMjVTvF8iFZNMA0GCSqGSIb3DQEBBQUAMF4xCzAJBgNV\nBAYTAkFVMQwwCgYDVQQIDANRTEQxHDAaBgNVBAoME0Ryb3BiZWFyIFVuaXZlcnNp\ndHkxIzAhBgNVBAMMGnNocmltcC5iYnEuZHJvcGJlYXIuZWR1LmF1MB4XDTEwMTIx\nNDAzMDQzNVoXDTExMTIxNDAzMDQzNVowXjELMAkGA1UEBhMCQVUxDDAKBgNVBAgM\nA1FMRDEcMBoGA1UECgwTRHJvcGJlYXIgVW5pdmVyc2l0eTEjMCEGA1UEAwwac2hy\naW1wLmJicS5kcm9wYmVhci5lZHUuYXUwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJ\nAoGBAMPKcoFAGFwjd7YapcDVJb8nAFWqrUidEY1dL8AB7hkVQWcB8/P+e8y41khw\n4QjnEvUH9TXUjBiwbgS7NQxKKsMoJzI5cMy5sSLVlBlJ2GrY+XGRqyiAZGeEK6NK\n16qAvH+rEUctBD0qjrNmoHCM1Z3abxUOaI3RuwKbuVeSjp/tAgMBAAGjUDBOMB0G\nA1UdDgQWBBQ+bc/JYEsu0yntp0JPvTByrpKUhTAfBgNVHSMEGDAWgBQ+bc/JYEsu\n0yntp0JPvTByrpKUhTAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4GBACcK\nQvIr3qdZ8OvZM5VyuWKOpf3cShlUwr16jPLDLTVGubp/Am3epzaAkzeOtlgChhXP\n4wWBV/qa6UUjV7N51N1FBkwLc6K7bDjy0zhlFArQMLiUboiqDAO+Q8lk8+1IoicI\nhCaHK/nBUGblO2/TX8SMYwrEvcwExEir3QFND6xD\n-----END CERTIFICATE-----','2010-12-14 13:08:47','2011-12-14 13:04:35','CN=shrimp.bbq.dropbear.edu.au,O=Dropbear University,ST=QLD,C=AU','2010-12-14 13:08:47','CN=shrimp.bbq.dropbear.edu.au,O=Dropbear University,ST=QLD,C=AU'),(4,0,'-----BEGIN CERTIFICATE-----\nMIICijCCAfOgAwIBAgIJAMjVTvF8iFZNMA0GCSqGSIb3DQEBBQUAMF4xCzAJBgNV\nBAYTAkFVMQwwCgYDVQQIDANRTEQxHDAaBgNVBAoME0Ryb3BiZWFyIFVuaXZlcnNp\ndHkxIzAhBgNVBAMMGnNocmltcC5iYnEuZHJvcGJlYXIuZWR1LmF1MB4XDTEwMTIx\nNDAzMDQzNVoXDTExMTIxNDAzMDQzNVowXjELMAkGA1UEBhMCQVUxDDAKBgNVBAgM\nA1FMRDEcMBoGA1UECgwTRHJvcGJlYXIgVW5pdmVyc2l0eTEjMCEGA1UEAwwac2hy\naW1wLmJicS5kcm9wYmVhci5lZHUuYXUwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJ\nAoGBAMPKcoFAGFwjd7YapcDVJb8nAFWqrUidEY1dL8AB7hkVQWcB8/P+e8y41khw\n4QjnEvUH9TXUjBiwbgS7NQxKKsMoJzI5cMy5sSLVlBlJ2GrY+XGRqyiAZGeEK6NK\n16qAvH+rEUctBD0qjrNmoHCM1Z3abxUOaI3RuwKbuVeSjp/tAgMBAAGjUDBOMB0G\nA1UdDgQWBBQ+bc/JYEsu0yntp0JPvTByrpKUhTAfBgNVHSMEGDAWgBQ+bc/JYEsu\n0yntp0JPvTByrpKUhTAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4GBACcK\nQvIr3qdZ8OvZM5VyuWKOpf3cShlUwr16jPLDLTVGubp/Am3epzaAkzeOtlgChhXP\n4wWBV/qa6UUjV7N51N1FBkwLc6K7bDjy0zhlFArQMLiUboiqDAO+Q8lk8+1IoicI\nhCaHK/nBUGblO2/TX8SMYwrEvcwExEir3QFND6xD\n-----END CERTIFICATE-----','2010-12-14 13:08:47','2011-12-14 13:04:35','CN=shrimp.bbq.dropbear.edu.au,O=Dropbear University,ST=QLD,C=AU','2010-12-14 13:08:47','CN=shrimp.bbq.dropbear.edu.au,O=Dropbear University,ST=QLD,C=AU');
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email_id` bigint(20) NOT NULL,
  `given_name` varchar(255) NOT NULL,
  `home_phone_id` bigint(20) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `mobile_phone_id` bigint(20) DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `secondary_email_id` bigint(20) DEFAULT NULL,
  `surname` varchar(255) NOT NULL,
  `work_phone_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK38B72420E5FA11AA` (`organization_id`),
  KEY `FK38B72420E6E281BA` (`mobile_phone_id`),
  KEY `FK38B72420F6C921D` (`home_phone_id`),
  KEY `FK38B72420AB4A182B` (`work_phone_id`),
  KEY `FK38B724202014D46E` (`secondary_email_id`),
  KEY `FK38B72420E13DB203` (`email_id`),
  CONSTRAINT `FK38B72420E13DB203` FOREIGN KEY (`email_id`) REFERENCES `mailuri` (`id`),
  CONSTRAINT `FK38B724202014D46E` FOREIGN KEY (`secondary_email_id`) REFERENCES `mailuri` (`id`),
  CONSTRAINT `FK38B72420AB4A182B` FOREIGN KEY (`work_phone_id`) REFERENCES `tel_numuri` (`id`),
  CONSTRAINT `FK38B72420E5FA11AA` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`),
  CONSTRAINT `FK38B72420E6E281BA` FOREIGN KEY (`mobile_phone_id`) REFERENCES `tel_numuri` (`id`),
  CONSTRAINT `FK38B72420F6C921D` FOREIGN KEY (`home_phone_id`) REFERENCES `tel_numuri` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` VALUES (1,0,'2010-12-14 11:50:50',NULL,28,'Mick',NULL,'2010-12-14 11:50:50',NULL,1,NULL,'Dundee',NULL),(2,0,'2010-12-14 12:51:25',NULL,34,'Steve',NULL,'2010-12-14 12:51:25',NULL,2,NULL,'Irwin',NULL),(3,0,'2010-12-14 13:15:36',NULL,50,'Steve',NULL,'2010-12-14 13:15:36',NULL,2,NULL,'Irwin',NULL);
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_person`
--

DROP TABLE IF EXISTS `contact_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `contact_id` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `descriptor_id` bigint(20) DEFAULT NULL,
  `entity_id` bigint(20) DEFAULT NULL,
  `extensions` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE8319054190ADB79` (`entity_id`),
  KEY `FKE8319054C365AAEA` (`contact_id`),
  KEY `FKE831905422F46540` (`descriptor_id`),
  KEY `FKE8319054E2B50CEA` (`type_id`),
  CONSTRAINT `FKE8319054E2B50CEA` FOREIGN KEY (`type_id`) REFERENCES `contact_type` (`id`),
  CONSTRAINT `FKE8319054190ADB79` FOREIGN KEY (`entity_id`) REFERENCES `entity_descriptor` (`id`),
  CONSTRAINT `FKE831905422F46540` FOREIGN KEY (`descriptor_id`) REFERENCES `role_descriptor` (`id`),
  CONSTRAINT `FKE8319054C365AAEA` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_person`
--

LOCK TABLES `contact_person` WRITE;
/*!40000 ALTER TABLE `contact_person` DISABLE KEYS */;
INSERT INTO `contact_person` VALUES (1,0,1,'2010-12-14 11:50:51',NULL,1,NULL,'2010-12-14 11:50:51',5),(2,0,1,'2010-12-14 11:50:51',3,NULL,NULL,'2010-12-14 11:50:51',5),(3,0,2,'2010-12-14 12:51:25',NULL,4,NULL,'2010-12-14 12:51:25',5),(4,0,2,'2010-12-14 12:51:25',6,NULL,NULL,'2010-12-14 12:51:25',5),(5,0,1,'2010-12-14 13:08:47',NULL,7,NULL,'2010-12-14 13:08:47',5),(6,0,1,'2010-12-14 13:08:47',8,NULL,NULL,'2010-12-14 13:08:47',5);
/*!40000 ALTER TABLE `contact_person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_type`
--

DROP TABLE IF EXISTS `contact_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `display_name` varchar(255) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_type`
--

LOCK TABLES `contact_type` WRITE;
/*!40000 ALTER TABLE `contact_type` DISABLE KEYS */;
INSERT INTO `contact_type` VALUES (1,0,'2010-12-14 11:50:42','Technical contacts','Technical','2010-12-14 11:50:42','technical'),(2,0,'2010-12-14 11:50:42','Marketting contacts','Marketing','2010-12-14 11:50:42','marketing'),(3,0,'2010-12-14 11:50:42','Billing contacts','Billing','2010-12-14 11:50:42','billing'),(4,0,'2010-12-14 11:50:42','Support contacts','Support','2010-12-14 11:50:42','support'),(5,0,'2010-12-14 11:50:42','Administrative contacts','Administrative','2010-12-14 11:50:42','administrative'),(6,0,'2010-12-14 11:50:42','Other contacts','Other','2010-12-14 11:50:42','other');
/*!40000 ALTER TABLE `contact_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `descriptor`
--

DROP TABLE IF EXISTS `descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `descriptor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descriptor`
--

LOCK TABLES `descriptor` WRITE;
/*!40000 ALTER TABLE `descriptor` DISABLE KEYS */;
INSERT INTO `descriptor` VALUES (1,0),(2,1),(3,23),(4,0),(5,1),(6,10),(7,2),(8,1);
/*!40000 ALTER TABLE `descriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `details`
--

DROP TABLE IF EXISTS `details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `logo_small` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5CD8F242E6644D8E` (`url_id`),
  CONSTRAINT `FK5CD8F242E6644D8E` FOREIGN KEY (`url_id`) REFERENCES `url` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `details`
--

LOCK TABLES `details` WRITE;
/*!40000 ALTER TABLE `details` DISABLE KEYS */;
INSERT INTO `details` VALUES (1,0,'fedreg.templates.federation.shibboleth.description','fedreg.templates.federation.shibboleth.displayname',NULL,NULL,'fedreg.templates.federation.shibboleth.name',1);
/*!40000 ALTER TABLE `details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discovery_response_service`
--

DROP TABLE IF EXISTS `discovery_response_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discovery_response_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF5FA366A3A780BC` (`descriptor_id`),
  KEY `FKF5FA366C6661FF9` (`descriptor_id`),
  CONSTRAINT `FKF5FA366C6661FF9` FOREIGN KEY (`descriptor_id`) REFERENCES `ssodescriptor` (`id`),
  CONSTRAINT `FKF5FA366A3A780BC` FOREIGN KEY (`descriptor_id`) REFERENCES `spssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discovery_response_service`
--

LOCK TABLES `discovery_response_service` WRITE;
/*!40000 ALTER TABLE `discovery_response_service` DISABLE KEYS */;
INSERT INTO `discovery_response_service` VALUES (19,8);
/*!40000 ALTER TABLE `discovery_response_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encryption_method`
--

DROP TABLE IF EXISTS `encryption_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `encryption_method` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `algorithm` varchar(255) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `key_size` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `oae_params` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encryption_method`
--

LOCK TABLES `encryption_method` WRITE;
/*!40000 ALTER TABLE `encryption_method` DISABLE KEYS */;
/*!40000 ALTER TABLE `encryption_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `endpoint`
--

DROP TABLE IF EXISTS `endpoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endpoint` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `active` bit(1) NOT NULL,
  `approved` bit(1) NOT NULL,
  `binding_id` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `location_id` bigint(20) NOT NULL,
  `response_location_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK67C71D954BC7BC04` (`binding_id`),
  KEY `FK67C71D9577610032` (`location_id`),
  KEY `FK67C71D952F4B1934` (`response_location_id`),
  CONSTRAINT `FK67C71D952F4B1934` FOREIGN KEY (`response_location_id`) REFERENCES `urluri` (`id`),
  CONSTRAINT `FK67C71D954BC7BC04` FOREIGN KEY (`binding_id`) REFERENCES `samluri` (`id`),
  CONSTRAINT `FK67C71D9577610032` FOREIGN KEY (`location_id`) REFERENCES `urluri` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `endpoint`
--

LOCK TABLES `endpoint` WRITE;
/*!40000 ALTER TABLE `endpoint` DISABLE KEYS */;
INSERT INTO `endpoint` VALUES (1,0,'','',9,'2010-12-14 11:50:51','2010-12-14 11:50:51',29,NULL),(2,0,'','',9,'2010-12-14 11:50:51','2010-12-14 11:50:51',30,NULL),(3,0,'','',5,'2010-12-14 11:50:51','2010-12-14 11:50:51',31,NULL),(4,0,'','',4,'2010-12-14 11:50:51','2010-12-14 11:50:51',32,NULL),(5,0,'','',9,'2010-12-14 12:51:25','2010-12-14 12:51:25',35,NULL),(6,0,'','',9,'2010-12-14 12:51:25','2010-12-14 12:51:25',36,NULL),(7,0,'','',5,'2010-12-14 12:51:26','2010-12-14 12:51:26',37,NULL),(8,0,'','',4,'2010-12-14 12:51:26','2010-12-14 12:51:26',38,NULL),(9,0,'','',4,'2010-12-14 13:08:47','2010-12-14 13:08:47',39,NULL),(10,0,'','',5,'2010-12-14 13:08:47','2010-12-14 13:08:47',40,NULL),(11,0,'','',6,'2010-12-14 13:08:47','2010-12-14 13:08:47',41,NULL),(12,0,'','',9,'2010-12-14 13:08:47','2010-12-14 13:08:47',42,NULL),(13,0,'','',9,'2010-12-14 13:08:47','2010-12-14 13:08:47',43,NULL),(14,0,'','',6,'2010-12-14 13:08:47','2010-12-14 13:08:47',44,NULL),(15,0,'','',5,'2010-12-14 13:08:47','2010-12-14 13:08:47',45,NULL),(16,0,'','',4,'2010-12-14 13:08:47','2010-12-14 13:08:47',46,NULL),(17,0,'','',9,'2010-12-14 13:08:47','2010-12-14 13:08:47',47,NULL),(18,0,'','',5,'2010-12-14 13:08:47','2010-12-14 13:08:47',48,NULL),(19,0,'','',14,'2010-12-14 13:08:47','2010-12-14 13:08:47',49,NULL);
/*!40000 ALTER TABLE `endpoint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entities_descriptor`
--

DROP TABLE IF EXISTS `entities_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities_descriptor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `extensions` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entities_descriptor`
--

LOCK TABLES `entities_descriptor` WRITE;
/*!40000 ALTER TABLE `entities_descriptor` DISABLE KEYS */;
INSERT INTO `entities_descriptor` VALUES (1,0,'2010-12-14 12:54:34',NULL,'2010-12-14 12:54:34','aaf.edu.au');
/*!40000 ALTER TABLE `entities_descriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entities_descriptor_entities_descriptor`
--

DROP TABLE IF EXISTS `entities_descriptor_entities_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities_descriptor_entities_descriptor` (
  `entities_descriptor_entities_descriptors_id` bigint(20) DEFAULT NULL,
  `entities_descriptor_id` bigint(20) DEFAULT NULL,
  KEY `FK593DD21B6FA8B3E2` (`entities_descriptor_entities_descriptors_id`),
  KEY `FK593DD21BB010D6CD` (`entities_descriptor_id`),
  CONSTRAINT `FK593DD21BB010D6CD` FOREIGN KEY (`entities_descriptor_id`) REFERENCES `entities_descriptor` (`id`),
  CONSTRAINT `FK593DD21B6FA8B3E2` FOREIGN KEY (`entities_descriptor_entities_descriptors_id`) REFERENCES `entities_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entities_descriptor_entities_descriptor`
--

LOCK TABLES `entities_descriptor_entities_descriptor` WRITE;
/*!40000 ALTER TABLE `entities_descriptor_entities_descriptor` DISABLE KEYS */;
/*!40000 ALTER TABLE `entities_descriptor_entities_descriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entities_descriptor_entity_descriptor`
--

DROP TABLE IF EXISTS `entities_descriptor_entity_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities_descriptor_entity_descriptor` (
  `entities_descriptor_entity_descriptors_id` bigint(20) DEFAULT NULL,
  `entity_descriptor_id` bigint(20) DEFAULT NULL,
  KEY `FKD9E0B0399535300` (`entities_descriptor_entity_descriptors_id`),
  KEY `FKD9E0B039D15ADD11` (`entity_descriptor_id`),
  CONSTRAINT `FKD9E0B039D15ADD11` FOREIGN KEY (`entity_descriptor_id`) REFERENCES `entity_descriptor` (`id`),
  CONSTRAINT `FKD9E0B0399535300` FOREIGN KEY (`entities_descriptor_entity_descriptors_id`) REFERENCES `entities_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entities_descriptor_entity_descriptor`
--

LOCK TABLES `entities_descriptor_entity_descriptor` WRITE;
/*!40000 ALTER TABLE `entities_descriptor_entity_descriptor` DISABLE KEYS */;
/*!40000 ALTER TABLE `entities_descriptor_entity_descriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity_descriptor`
--

DROP TABLE IF EXISTS `entity_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_descriptor` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) NOT NULL,
  `approved` bit(1) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `entityid` varchar(255) NOT NULL,
  `extensions` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `organization_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `entityid` (`entityid`),
  KEY `FKE5AF094BE5FA11AA` (`organization_id`),
  CONSTRAINT `FKE5AF094BE5FA11AA` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_descriptor`
--

LOCK TABLES `entity_descriptor` WRITE;
/*!40000 ALTER TABLE `entity_descriptor` DISABLE KEYS */;
INSERT INTO `entity_descriptor` VALUES (1,'','','2010-12-14 11:50:51','https://idp.dropbear.edu.au/idp/shibboleth',NULL,'2010-12-14 11:50:51',1),(4,'','','2010-12-14 12:51:25','https://idp.yowie.edu.au/idp/shibboleth',NULL,'2010-12-14 12:51:25',2),(7,'','','2010-12-14 13:08:47','https://shrimp.bbq.dropbear.edu.au/shibboleth',NULL,'2010-12-14 13:14:36',1);
/*!40000 ALTER TABLE `entity_descriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity_descriptor_additional_metadata_location`
--

DROP TABLE IF EXISTS `entity_descriptor_additional_metadata_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_descriptor_additional_metadata_location` (
  `entity_descriptor_additional_metadata_locations_id` bigint(20) DEFAULT NULL,
  `additional_metadata_location_id` bigint(20) DEFAULT NULL,
  KEY `FKC79DEB21E7E6278A` (`entity_descriptor_additional_metadata_locations_id`),
  KEY `FKC79DEB217541A908` (`additional_metadata_location_id`),
  CONSTRAINT `FKC79DEB217541A908` FOREIGN KEY (`additional_metadata_location_id`) REFERENCES `additional_metadata_location` (`id`),
  CONSTRAINT `FKC79DEB21E7E6278A` FOREIGN KEY (`entity_descriptor_additional_metadata_locations_id`) REFERENCES `entity_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_descriptor_additional_metadata_location`
--

LOCK TABLES `entity_descriptor_additional_metadata_location` WRITE;
/*!40000 ALTER TABLE `entity_descriptor_additional_metadata_location` DISABLE KEYS */;
/*!40000 ALTER TABLE `entity_descriptor_additional_metadata_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `federation_provider`
--

DROP TABLE IF EXISTS `federation_provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `federation_provider` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `auto_provision` bit(1) NOT NULL,
  `details_id` bigint(20) NOT NULL,
  `uid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK25943B6DA98E7EAE` (`details_id`),
  CONSTRAINT `FK25943B6DA98E7EAE` FOREIGN KEY (`details_id`) REFERENCES `details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `federation_provider`
--

LOCK TABLES `federation_provider` WRITE;
/*!40000 ALTER TABLE `federation_provider` DISABLE KEYS */;
INSERT INTO `federation_provider` VALUES (1,0,'',1,'shibboleth');
/*!40000 ALTER TABLE `federation_provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `federation_provider_props`
--

DROP TABLE IF EXISTS `federation_provider_props`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `federation_provider_props` (
  `props` bigint(20) DEFAULT NULL,
  `props_idx` varchar(255) DEFAULT NULL,
  `props_elt` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `federation_provider_props`
--

LOCK TABLES `federation_provider_props` WRITE;
/*!40000 ALTER TABLE `federation_provider_props` DISABLE KEYS */;
/*!40000 ALTER TABLE `federation_provider_props` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idpssodescriptor`
--

DROP TABLE IF EXISTS `idpssodescriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idpssodescriptor` (
  `id` bigint(20) NOT NULL,
  `auto_accept_services` bit(1) NOT NULL,
  `collaborator_id` bigint(20) DEFAULT NULL,
  `entity_descriptor_id` bigint(20) NOT NULL,
  `scope` varchar(255) NOT NULL,
  `want_authn_requests_signed` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6BC94229866C6B9A` (`collaborator_id`),
  KEY `FK6BC94229D15ADD11` (`entity_descriptor_id`),
  CONSTRAINT `FK6BC94229D15ADD11` FOREIGN KEY (`entity_descriptor_id`) REFERENCES `entity_descriptor` (`id`),
  CONSTRAINT `FK6BC94229866C6B9A` FOREIGN KEY (`collaborator_id`) REFERENCES `attribute_authority_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idpssodescriptor`
--

LOCK TABLES `idpssodescriptor` WRITE;
/*!40000 ALTER TABLE `idpssodescriptor` DISABLE KEYS */;
INSERT INTO `idpssodescriptor` VALUES (3,'',2,1,'dropbear.edu.au','\0'),(6,'',5,4,'yowie.edu.au','\0');
/*!40000 ALTER TABLE `idpssodescriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idpssodescriptor_samluri`
--

DROP TABLE IF EXISTS `idpssodescriptor_samluri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idpssodescriptor_samluri` (
  `idpssodescriptor_attribute_profiles_id` bigint(20) DEFAULT NULL,
  `samluri_id` bigint(20) DEFAULT NULL,
  KEY `FKF21241C93688E26A` (`samluri_id`),
  KEY `FKF21241C9C3B849D0` (`idpssodescriptor_attribute_profiles_id`),
  CONSTRAINT `FKF21241C9C3B849D0` FOREIGN KEY (`idpssodescriptor_attribute_profiles_id`) REFERENCES `idpssodescriptor` (`id`),
  CONSTRAINT `FKF21241C93688E26A` FOREIGN KEY (`samluri_id`) REFERENCES `samluri` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idpssodescriptor_samluri`
--

LOCK TABLES `idpssodescriptor_samluri` WRITE;
/*!40000 ALTER TABLE `idpssodescriptor_samluri` DISABLE KEYS */;
/*!40000 ALTER TABLE `idpssodescriptor_samluri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `indexed_endpoint`
--

DROP TABLE IF EXISTS `indexed_endpoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `indexed_endpoint` (
  `id` bigint(20) NOT NULL,
  `is_default` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indexed_endpoint`
--

LOCK TABLES `indexed_endpoint` WRITE;
/*!40000 ALTER TABLE `indexed_endpoint` DISABLE KEYS */;
INSERT INTO `indexed_endpoint` VALUES (2,''),(6,''),(17,'\0'),(18,''),(19,'');
/*!40000 ALTER TABLE `indexed_endpoint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitation`
--

DROP TABLE IF EXISTS `invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `action` varchar(255) DEFAULT NULL,
  `controller` varchar(255) DEFAULT NULL,
  `grp__` bigint(20) DEFAULT NULL,
  `invite_code` varchar(255) NOT NULL,
  `objid` varchar(255) DEFAULT NULL,
  `perm__` bigint(20) DEFAULT NULL,
  `role__` bigint(20) DEFAULT NULL,
  `utilized` bit(1) NOT NULL,
  `utilized_by_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invite_code` (`invite_code`),
  KEY `FK473F7799DBC6B448` (`utilized_by_id`),
  CONSTRAINT `FK473F7799DBC6B448` FOREIGN KEY (`utilized_by_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitation`
--

LOCK TABLES `invitation` WRITE;
/*!40000 ALTER TABLE `invitation` DISABLE KEYS */;
INSERT INTO `invitation` VALUES (1,0,'show','SPSSODescriptor',NULL,'97793cdea20f16c7816056b183d81725be1ec575','8',NULL,11,'\0',NULL);
/*!40000 ALTER TABLE `invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `key_descriptor`
--

DROP TABLE IF EXISTS `key_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `key_descriptor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `encryption_method_id` bigint(20) DEFAULT NULL,
  `key_info_id` bigint(20) NOT NULL,
  `key_type` varchar(255) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `role_descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK389F294FA9433291` (`encryption_method_id`),
  KEY `FK389F294F13FAB137` (`role_descriptor_id`),
  KEY `FK389F294F3257949` (`key_info_id`),
  CONSTRAINT `FK389F294F3257949` FOREIGN KEY (`key_info_id`) REFERENCES `key_info` (`id`),
  CONSTRAINT `FK389F294F13FAB137` FOREIGN KEY (`role_descriptor_id`) REFERENCES `role_descriptor` (`id`),
  CONSTRAINT `FK389F294FA9433291` FOREIGN KEY (`encryption_method_id`) REFERENCES `encryption_method` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `key_descriptor`
--

LOCK TABLES `key_descriptor` WRITE;
/*!40000 ALTER TABLE `key_descriptor` DISABLE KEYS */;
INSERT INTO `key_descriptor` VALUES (1,0,'2010-12-14 12:49:02','\0',NULL,1,'signing','2010-12-14 12:49:02',3),(2,0,'2010-12-14 12:58:22','\0',NULL,2,'signing','2010-12-14 12:58:22',6),(3,0,'2010-12-14 13:08:47','\0',NULL,3,'signing','2010-12-14 13:08:47',8),(4,0,'2010-12-14 13:08:47','\0',NULL,4,'encryption','2010-12-14 13:08:47',8);
/*!40000 ALTER TABLE `key_descriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `key_info`
--

DROP TABLE IF EXISTS `key_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `key_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `certificate_id` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1DD9A28EE0908B8A` (`certificate_id`),
  CONSTRAINT `FK1DD9A28EE0908B8A` FOREIGN KEY (`certificate_id`) REFERENCES `certificate` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `key_info`
--

LOCK TABLES `key_info` WRITE;
/*!40000 ALTER TABLE `key_info` DISABLE KEYS */;
INSERT INTO `key_info` VALUES (1,0,1,'2010-12-14 12:49:02',NULL,'2010-12-14 12:49:02'),(2,0,2,'2010-12-14 12:58:22',NULL,'2010-12-14 12:58:22'),(3,0,3,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47'),(4,0,4,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47');
/*!40000 ALTER TABLE `key_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level_permission_fifth`
--

DROP TABLE IF EXISTS `level_permission_fifth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level_permission_fifth` (
  `level_permission_id` bigint(20) DEFAULT NULL,
  `fifth_string` varchar(255) DEFAULT NULL,
  KEY `FKC93CB6A2D7151CB7` (`level_permission_id`),
  CONSTRAINT `FKC93CB6A2D7151CB7` FOREIGN KEY (`level_permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level_permission_fifth`
--

LOCK TABLES `level_permission_fifth` WRITE;
/*!40000 ALTER TABLE `level_permission_fifth` DISABLE KEYS */;
/*!40000 ALTER TABLE `level_permission_fifth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level_permission_first`
--

DROP TABLE IF EXISTS `level_permission_first`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level_permission_first` (
  `level_permission_id` bigint(20) DEFAULT NULL,
  `first_string` varchar(255) DEFAULT NULL,
  KEY `FKC93CE39BD7151CB7` (`level_permission_id`),
  CONSTRAINT `FKC93CE39BD7151CB7` FOREIGN KEY (`level_permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level_permission_first`
--

LOCK TABLES `level_permission_first` WRITE;
/*!40000 ALTER TABLE `level_permission_first` DISABLE KEYS */;
INSERT INTO `level_permission_first` VALUES (1,'organization'),(2,'descriptor'),(3,'descriptor'),(4,'descriptor'),(7,'organization'),(8,'descriptor'),(9,'descriptor'),(10,'descriptor'),(11,'descriptor'),(12,'descriptor');
/*!40000 ALTER TABLE `level_permission_first` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level_permission_fourth`
--

DROP TABLE IF EXISTS `level_permission_fourth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level_permission_fourth` (
  `level_permission_id` bigint(20) DEFAULT NULL,
  `fourth_string` varchar(255) DEFAULT NULL,
  KEY `FK5EB5768FD7151CB7` (`level_permission_id`),
  CONSTRAINT `FK5EB5768FD7151CB7` FOREIGN KEY (`level_permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level_permission_fourth`
--

LOCK TABLES `level_permission_fourth` WRITE;
/*!40000 ALTER TABLE `level_permission_fourth` DISABLE KEYS */;
/*!40000 ALTER TABLE `level_permission_fourth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level_permission_second`
--

DROP TABLE IF EXISTS `level_permission_second`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level_permission_second` (
  `level_permission_id` bigint(20) DEFAULT NULL,
  `second_string` varchar(255) DEFAULT NULL,
  KEY `FK744F50E9D7151CB7` (`level_permission_id`),
  CONSTRAINT `FK744F50E9D7151CB7` FOREIGN KEY (`level_permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level_permission_second`
--

LOCK TABLES `level_permission_second` WRITE;
/*!40000 ALTER TABLE `level_permission_second` DISABLE KEYS */;
INSERT INTO `level_permission_second` VALUES (1,'1'),(2,'1'),(3,'3'),(4,'2'),(7,'2'),(8,'4'),(9,'6'),(10,'5'),(11,'7'),(12,'8');
/*!40000 ALTER TABLE `level_permission_second` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level_permission_sixth`
--

DROP TABLE IF EXISTS `level_permission_sixth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level_permission_sixth` (
  `level_permission_id` bigint(20) DEFAULT NULL,
  `sixth_string` varchar(255) DEFAULT NULL,
  KEY `FKC9F42BC1D7151CB7` (`level_permission_id`),
  CONSTRAINT `FKC9F42BC1D7151CB7` FOREIGN KEY (`level_permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level_permission_sixth`
--

LOCK TABLES `level_permission_sixth` WRITE;
/*!40000 ALTER TABLE `level_permission_sixth` DISABLE KEYS */;
/*!40000 ALTER TABLE `level_permission_sixth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level_permission_third`
--

DROP TABLE IF EXISTS `level_permission_third`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level_permission_third` (
  `level_permission_id` bigint(20) DEFAULT NULL,
  `third_string` varchar(255) DEFAULT NULL,
  KEY `FKCA019652D7151CB7` (`level_permission_id`),
  CONSTRAINT `FKCA019652D7151CB7` FOREIGN KEY (`level_permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level_permission_third`
--

LOCK TABLES `level_permission_third` WRITE;
/*!40000 ALTER TABLE `level_permission_third` DISABLE KEYS */;
INSERT INTO `level_permission_third` VALUES (1,'*'),(2,'*'),(3,'*'),(4,'*'),(7,'*'),(8,'*'),(9,'*'),(10,'*'),(11,'*'),(12,'*');
/*!40000 ALTER TABLE `level_permission_third` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_record`
--

DROP TABLE IF EXISTS `login_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  `remote_addr` varchar(255) NOT NULL,
  `remote_host` varchar(255) NOT NULL,
  `user_agent` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF43101E7165461AF` (`owner_id`),
  CONSTRAINT `FKF43101E7165461AF` FOREIGN KEY (`owner_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_record`
--

LOCK TABLES `login_record` WRITE;
/*!40000 ALTER TABLE `login_record` DISABLE KEYS */;
INSERT INTO `login_record` VALUES (1,0,'2010-12-14 11:51:18','2010-12-14 11:51:18',2,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10'),(2,0,'2010-12-14 12:48:39','2010-12-14 12:48:39',2,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10'),(3,0,'2010-12-14 13:15:36','2010-12-14 13:15:36',3,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10'),(4,0,'2010-12-14 13:15:52','2010-12-14 13:15:52',2,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10'),(5,0,'2010-12-14 13:23:57','2010-12-14 13:23:57',2,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10'),(6,0,'2010-12-14 13:26:15','2010-12-14 13:26:15',2,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10'),(7,0,'2010-12-14 13:27:16','2010-12-14 13:27:16',3,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10'),(8,0,'2010-12-14 13:39:14','2010-12-14 13:39:14',2,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10'),(9,0,'2010-12-14 14:11:02','2010-12-14 14:11:02',2,'0:0:0:0:0:0:0:1%0','0:0:0:0:0:0:0:1%0','Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10');
/*!40000 ALTER TABLE `login_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mailuri`
--

DROP TABLE IF EXISTS `mailuri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mailuri` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mailuri`
--

LOCK TABLES `mailuri` WRITE;
/*!40000 ALTER TABLE `mailuri` DISABLE KEYS */;
INSERT INTO `mailuri` VALUES (28),(34),(50);
/*!40000 ALTER TABLE `mailuri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage_nameidservice`
--

DROP TABLE IF EXISTS `manage_nameidservice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manage_nameidservice` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA253A1B5C6661FF9` (`descriptor_id`),
  CONSTRAINT `FKA253A1B5C6661FF9` FOREIGN KEY (`descriptor_id`) REFERENCES `ssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_nameidservice`
--

LOCK TABLES `manage_nameidservice` WRITE;
/*!40000 ALTER TABLE `manage_nameidservice` DISABLE KEYS */;
INSERT INTO `manage_nameidservice` VALUES (9,8),(10,8),(11,8),(12,8);
/*!40000 ALTER TABLE `manage_nameidservice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monitor_type`
--

DROP TABLE IF EXISTS `monitor_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monitor_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monitor_type`
--

LOCK TABLES `monitor_type` WRITE;
/*!40000 ALTER TABLE `monitor_type` DISABLE KEYS */;
INSERT INTO `monitor_type` VALUES (1,0,'Ping check of associated endpoint to ensure availability','ping');
/*!40000 ALTER TABLE `monitor_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nameidmapping_service`
--

DROP TABLE IF EXISTS `nameidmapping_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nameidmapping_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC2051E9E81D2AAE4` (`descriptor_id`),
  CONSTRAINT `FKC2051E9E81D2AAE4` FOREIGN KEY (`descriptor_id`) REFERENCES `idpssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nameidmapping_service`
--

LOCK TABLES `nameidmapping_service` WRITE;
/*!40000 ALTER TABLE `nameidmapping_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `nameidmapping_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `active` bit(1) NOT NULL,
  `approved` bit(1) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` longtext,
  `display_name` varchar(255) NOT NULL,
  `extensions` varchar(255) DEFAULT NULL,
  `lang` varchar(255) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `logourl` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `primary_id` bigint(20) NOT NULL,
  `url_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK4644ED339E4F5455` (`primary_id`),
  KEY `FK4644ED3324954838` (`url_id`),
  CONSTRAINT `FK4644ED3324954838` FOREIGN KEY (`url_id`) REFERENCES `urluri` (`id`),
  CONSTRAINT `FK4644ED339E4F5455` FOREIGN KEY (`primary_id`) REFERENCES `organization_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` VALUES (1,0,'','','2010-12-14 11:50:50',NULL,'Dropbear University',NULL,'en_AU','2010-12-14 11:50:50',NULL,'dropbear.edu.au',1,27),(2,0,'','','2010-12-14 12:51:25',NULL,'University of Yowie',NULL,'en_AU','2010-12-14 12:51:25',NULL,'yowie.edu.au',1,33);
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_organization`
--

DROP TABLE IF EXISTS `organization_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_organization` (
  `organization_affiliates_id` bigint(20) DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `organization_sponsors_id` bigint(20) DEFAULT NULL,
  KEY `FK23F7B4BFE5FA11AA` (`organization_id`),
  KEY `FK23F7B4BF6469772B` (`organization_affiliates_id`),
  KEY `FK23F7B4BF79D78378` (`organization_sponsors_id`),
  CONSTRAINT `FK23F7B4BF79D78378` FOREIGN KEY (`organization_sponsors_id`) REFERENCES `organization` (`id`),
  CONSTRAINT `FK23F7B4BF6469772B` FOREIGN KEY (`organization_affiliates_id`) REFERENCES `organization` (`id`),
  CONSTRAINT `FK23F7B4BFE5FA11AA` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_organization`
--

LOCK TABLES `organization_organization` WRITE;
/*!40000 ALTER TABLE `organization_organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization_organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_organization_type`
--

DROP TABLE IF EXISTS `organization_organization_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_organization_type` (
  `organization_suspensions_id` bigint(20) DEFAULT NULL,
  `organization_type_id` bigint(20) DEFAULT NULL,
  `organization_types_id` bigint(20) DEFAULT NULL,
  KEY `FK3217B2BAEAAD5911` (`organization_type_id`),
  KEY `FK3217B2BA1B0C2453` (`organization_suspensions_id`),
  KEY `FK3217B2BAA2A40630` (`organization_types_id`),
  CONSTRAINT `FK3217B2BAA2A40630` FOREIGN KEY (`organization_types_id`) REFERENCES `organization` (`id`),
  CONSTRAINT `FK3217B2BA1B0C2453` FOREIGN KEY (`organization_suspensions_id`) REFERENCES `organization` (`id`),
  CONSTRAINT `FK3217B2BAEAAD5911` FOREIGN KEY (`organization_type_id`) REFERENCES `organization_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_organization_type`
--

LOCK TABLES `organization_organization_type` WRITE;
/*!40000 ALTER TABLE `organization_organization_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization_organization_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_type`
--

DROP TABLE IF EXISTS `organization_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discovery_service_category` bit(1) NOT NULL,
  `display_name` varchar(255) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_type`
--

LOCK TABLES `organization_type` WRITE;
/*!40000 ALTER TABLE `organization_type` DISABLE KEYS */;
INSERT INTO `organization_type` VALUES (1,0,'2010-12-14 11:50:50','Organization Type that is associated with all Australian Universities','\0','Australian Universities','2010-12-14 11:50:50','AU University');
/*!40000 ALTER TABLE `organization_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pdpdescriptor`
--

DROP TABLE IF EXISTS `pdpdescriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pdpdescriptor` (
  `id` bigint(20) NOT NULL,
  `entity_descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK522A796BD15ADD11` (`entity_descriptor_id`),
  CONSTRAINT `FK522A796BD15ADD11` FOREIGN KEY (`entity_descriptor_id`) REFERENCES `entity_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pdpdescriptor`
--

LOCK TABLES `pdpdescriptor` WRITE;
/*!40000 ALTER TABLE `pdpdescriptor` DISABLE KEYS */;
/*!40000 ALTER TABLE `pdpdescriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pdpdescriptor_nameidformats`
--

DROP TABLE IF EXISTS `pdpdescriptor_nameidformats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pdpdescriptor_nameidformats` (
  `pdpdescriptor_id` bigint(20) DEFAULT NULL,
  `nameidformats_string` varchar(255) DEFAULT NULL,
  KEY `FKBE823BE2B6E7D72A` (`pdpdescriptor_id`),
  CONSTRAINT `FKBE823BE2B6E7D72A` FOREIGN KEY (`pdpdescriptor_id`) REFERENCES `pdpdescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pdpdescriptor_nameidformats`
--

LOCK TABLES `pdpdescriptor_nameidformats` WRITE;
/*!40000 ALTER TABLE `pdpdescriptor_nameidformats` DISABLE KEYS */;
/*!40000 ALTER TABLE `pdpdescriptor_nameidformats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `actions` varchar(255) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `managed` bit(1) NOT NULL,
  `possible_actions` varchar(255) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `target` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `class` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE125C5CF42555646` (`role_id`),
  KEY `FKE125C5CFAA6DB197` (`user_id`),
  KEY `FKE125C5CF35C9368E` (`group_id`),
  CONSTRAINT `FKE125C5CF35C9368E` FOREIGN KEY (`group_id`) REFERENCES `_group` (`id`),
  CONSTRAINT `FKE125C5CF42555646` FOREIGN KEY (`role_id`) REFERENCES `_role` (`id`),
  CONSTRAINT `FKE125C5CFAA6DB197` FOREIGN KEY (`user_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,0,'*',NULL,'\0','*',4,'organization:1:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(2,0,'*',NULL,'\0','*',5,'descriptor:1:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(3,0,'*',NULL,'\0','*',6,'descriptor:3:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(4,0,'*',NULL,'\0','*',6,'descriptor:2:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(5,0,'*',NULL,'','*',NULL,'profile:edit:2','grails.plugins.nimble.auth.WildcardPermission',2,'grails.plugins.nimble.core.Permission'),(6,0,'*',NULL,'','*',NULL,'*','grails.plugins.nimble.auth.AllPermission',2,'grails.plugins.nimble.core.Permission'),(7,0,'*',NULL,'\0','*',7,'organization:2:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(8,0,'*',NULL,'\0','*',8,'descriptor:4:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(9,0,'*',NULL,'\0','*',9,'descriptor:6:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(10,0,'*',NULL,'\0','*',9,'descriptor:5:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(11,0,'*',NULL,'\0','*',10,'descriptor:7:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(12,0,'*',NULL,'\0','*',11,'descriptor:8:*','grails.plugins.nimble.auth.WildcardPermission',NULL,'grails.plugins.nimble.core.LevelPermission'),(13,0,'*',NULL,'','*',NULL,'profile:edit:3','grails.plugins.nimble.auth.WildcardPermission',3,'grails.plugins.nimble.core.Permission');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process`
--

DROP TABLE IF EXISTS `process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  `creator_id` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `definition` longtext NOT NULL,
  `description` varchar(255) NOT NULL,
  `last_editor_id` bigint(20) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `process_version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKED8D1E6F81BFCA4C` (`last_editor_id`),
  KEY `FKED8D1E6F569B596` (`creator_id`),
  CONSTRAINT `FKED8D1E6F569B596` FOREIGN KEY (`creator_id`) REFERENCES `_user` (`id`),
  CONSTRAINT `FKED8D1E6F81BFCA4C` FOREIGN KEY (`last_editor_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process`
--

LOCK TABLES `process` WRITE;
/*!40000 ALTER TABLE `process` DISABLE KEYS */;
INSERT INTO `process` VALUES (1,0,'',1,'2010-12-14 11:50:41','\nprocess(name:\'idpssodescriptor_create\', description: \'Workflow defining Identity Provider creation and approval process\') {\n	\n	task(name: \'Send confirmation message\', description: \'Invokes a script to confirm registration of IDP.\') {\n		execute(script: \'idpssodescriptor_confirm\')\n		outcome(name: \'confirmedidpssodescriptor\', description:\'User registering the IDP has been advised of creation\') {\n			start (\'Check for valid approvers\')\n		}\n	}\n	\n	task(name: \'Check for valid approvers\', description: \'Ensures that organization-{organization}-administrators is actually populated with users, if not bypasses directly to federation administrators.\') {\n		execute(script: \'organization_administrators_populated\')\n		outcome(name: \'organization_hasadministrators\', description:\'The Organization has administrators and they will be asked to approve.\') {\n			start (\'Request organization approval\')\n		}\n		outcome(name: \'organization_noadministrators\', description:\'The Organization has no locally registered administrators. Federation Administrators will undertake an out of band approval process.\') {\n			start (\'Request executive federation approval\')\n		}\n	}\n	\n	task(name: \'Request organization approval\', description: \'Requests that a user who is an administrative member of the owning organization approves creation and associated billing by introducing this Identity Provider.\') {\n		approver(role: \'organization-{organization}-administrators\') {\n			reject(name: \'Not Associated\', description:\'Not an Identity Provider associated with this organization (Identity Provider details will be discarded)\') {\n				start (\'Delete Identity Provider\')\n			}\n			reject(name: \'Not Accepted\', description:\'The organization will not accept ownership for this Identity Provider (Identity Provider details will be discarded)\') {\n				start (\'Delete Identity Provider\')\n			}\n		}\n		outcome(name: \'organizationapproved\', description:\'The organization has accepted responsibility for this Identity Provider\') {\n			start (\'Request Federation approval\')\n		}\n	}\n	\n	task(name: \'Request Federation approval\', description: \'Requests that a user who is an administrative member of the federation approves activation for this Identity Provider as a valuable addition for the federation.\') {\n		approver(role: \'federation-administrators\') {\n			reject(name: \'Not Accepted\', description:\'The federation will not accept ownership for this Identity Provider (Identity Provider details will be discarded)\') {\n				start (\'Delete Identity Provider\')\n			}\n		}\n		outcome(name: \'federationapproved\', description:\'The organization has accepted responsibility for this Identity Provider\') {\n			start (\'Activate Identity Provider\')\n		}\n	}\n	\n	task(name: \'Request executive federation approval\', description: \'Requests that a user who is an administrative member of the federation approves activation for this Identity Provider for the Federation and on behalf of the owning Organization. External verification should be undertaken.\') {\n		approver(role: \'federation-administrators\') {\n			reject(name: \'Not Accepted\', description:\'The federation will not accept ownership for this Identity Provider (Identity Provider details will be discarded).\') {\n				start (\'Delete Identity Provider\')\n			}\n			reject(name: \'Organization Not Accepted\', description:\'The organization responsible will not accept ownership for this Identity Provider (Identity Provider details will be discarded).\') {\n				start (\'Delete Identity Provider\')\n			}\n		}\n		outcome(name: \'federationapproved\', description:\'The organization has accepted responsibility for this Identity Provider\') {\n			start (\'Activate Identity Provider\')\n		}\n	}\n	\n	task(name: \'Activate Identity Provider\', description: \'Activates the Identity Provider so it can be rendered into Metadata.\') {\n		execute(script: \'idpssodescriptor_activate\')\n		outcome(name: \'idpssodescriptoractivated\', description:\'The Identity Provider is now activate and being populated into Metadata\') {\n			start (\'finish\')\n		}\n	}\n	\n	task(name: \'Delete Identity Provider\', description: \'Deletes the Identity Provider after it has been rejected.\') {\n		execute(script: \'idpssodescriptor_delete\')\n		outcome(name: \'idpssodescriptordeleted\', description:\'The Identity Provider definition has been deleted\') {\n			start (\'finish\')\n		}\n	}\n\n	task(name: \'finish\', description: \'Completes the idpssodescriptor_create workflow\') {\n		finish()\n	}\n	\n}','Workflow defining Identity Provider creation and approval process',NULL,'2010-12-14 11:50:41','idpssodescriptor_create',1),(2,0,'',1,'2010-12-14 11:50:41','\nprocess(name:\'organization_create\', description: \'Workflow defining Organization creation and approval process\') {\n	\n	task(name: \'Send confirmation message\', description: \'Invokes a script to confirm registration of an Organization.\') {\n		execute(script: \'organization_confirm\')\n		outcome(name: \'confirmedorganization\', description:\'User registering the Organization has been advised of creation\') {\n			start (\'Request Federation approval\')\n		}\n	}\n	\n	task(name: \'Request Federation approval\', description: \'Requests that a user who is an administrative member of the federation approves activation for this Organization as a valuable addition for the federation.\') {\n		approver(role: \'federation-administrators\') {\n			reject(name: \'Not Accepted\', description:\'The federation will not accept this Organization (Organization details will be discarded)\') {\n				start (\'Delete Organization\')\n			}\n		}\n		outcome(name: \'federationapproved\', description:\'The organization has accepted responsibility for this Identity Provider\') {\n			start (\'Activate Organization\')\n		}\n	}\n	\n	task(name: \'Activate Organization\', description: \'Activates the Organization so it can register components with the federation\') {\n		execute(script: \'organization_activate\')\n		outcome(name: \'organizationactivated\', description:\'The Organization is now activate and being populated into Metadata\') {\n			start (\'finish\')\n		}\n	}\n	\n	task(name: \'Delete Organization\', description: \'Deletes the Organization after it has been rejected.\') {\n		execute(script: \'organization_delete\')\n		outcome(name: \'organizationdeleted\', description:\'The Organization definition has been deleted\') {\n			start (\'finish\')\n		}\n	}\n\n	task(name: \'finish\', description: \'Completes the organization_create workflow\') {\n		finish()\n	}\n	\n}','Workflow defining Organization creation and approval process',NULL,'2010-12-14 11:50:41','organization_create',1),(3,0,'',1,'2010-12-14 11:50:41','process(name:\'requestedattribute_create\', description: \'Workflow defining Requested Attribute creation and approval process for SPSSODescriptors\') {\n	\n	task(name: \'Auto approve\', description: \'Automatically approves all new attributes for SPSSOdescriptors, useful as default in test environments\') {\n		execute(script: \'requestedattribute_activate\')\n		outcome(name: \'requestedattributeactivated\', description:\'The Requested Attribute is now activate and being populated into Metadata\') {\n			start (\'finish\')\n		}\n	}\n	\n	task(name: \'finish\', description: \'Completes the requestedattribute_create workflow\') {\n		finish()\n	}\n\n}','Workflow defining Requested Attribute creation and approval process for SPSSODescriptors',NULL,'2010-12-14 11:50:41','requestedattribute_create',1),(4,1,'',1,'2010-12-14 11:50:41','\nprocess(name:\'spssodescriptor_create\', description: \'Workflow defining Service Provider creation and approval process\') {\n	\n	task(name: \'Send confirmation message\', description: \'Invokes a script to confirm registration of SP.\') {\n		execute(script: \'spssodescriptor_confirm\')\n		outcome(name: \'confirmedspssodescriptor\', description:\'User registering the SP has been advised of creation\') {\n			start (\'Check for valid approvers\')\n		}\n	}\n	\n	task(name: \'Check for valid approvers\', description: \'Ensures that organization-{organization}-administrators is actually populated with users, if not bypasses directly to federation administrators.\') {\n		execute(script: \'organization_administrators_populated\')\n		outcome(name: \'organization_hasadministrators\', description:\'The Organization has administrators and they will be asked to approve.\') {\n			start (\'Request organization approval\')\n		}\n		outcome(name: \'organization_noadministrators\', description:\'The Organization has no locally registered administrators. Federation Administrators will undertake an out of band approval process.\') {\n			start (\'Request executive federation approval\')\n		}\n	}\n	\n	task(name: \'Request organization approval\', description: \'Requests that a user who is an administrative member of the owning organization approves creation and associated billing by introducing this Service Provider.\') {\n		approver(role: \'organization-{organization}-administrators\') {\n			reject(name: \'Not Associated\', description:\'Not a Service Provider associated with this organization (Service Provider details will be discarded)\') {\n				start (\'Delete Service Provider\')\n			}\n			reject(name: \'Not Accepted\', description:\'The organization will not accept ownership for this Service Provider (Service Provider details will be discarded)\') {\n				start (\'Delete Service Provider\')\n			}\n		}\n		outcome(name: \'organizationapproved\', description:\'The organization has accepted responsibility for this Service Provider\') {\n			start (\'Request Federation approval\')\n		}\n	}\n	\n	task(name: \'Request Federation approval\', description: \'Requests that a user who is an administrative member of the federation approves activation for this Service Provider as a valuable addition for the federation.\') {\n		approver(role: \'federation-administrators\') {\n			reject(name: \'Not Accepted\', description:\'The federation will not accept ownership for this Service Provider (Service Provider details will be discarded)\') {\n				start (\'Delete Service Provider\')\n			}\n		}\n		outcome(name: \'federationapproved\', description:\'The organization has accepted responsibility for this Service Provider\') {\n			start (\'Activate Service Provider\')\n		}\n	}\n	\n	task(name: \'Request executive federation approval\', description: \'Requests that a user who is an administrative member of the federation approves activation for this Service Provider for the Federation and on behalf of the owning Organization. External verification should be undertaken.\') {\n		approver(role: \'federation-administrators\') {\n			reject(name: \'Not Accepted\', description:\'The federation will not accept ownership for this Service Provider (Service Provider details will be discarded).\') {\n				start (\'Delete Service Provider\')\n			}\n			reject(name: \'Organization Not Accepted\', description:\'The organization responsible will not accept ownership for this Service Provider (Service Provider details will be discarded).\') {\n				start (\'Delete Service Provider\')\n			}\n		}\n		outcome(name: \'executivelyapproved\', description:\'The organization and federation combined have accepted responsibility for this Service Provider\') {\n			start (\'Activate Service Provider\')\n		}\n	}\n	\n	task(name: \'Activate Service Provider\', description: \'Activates the Service Provider so it can be rendered into Metadata.\') {\n		execute(script: \'spssodescriptor_activate\')\n		outcome(name: \'spssodescriptoractivated\', description:\'The Service Provider is now activate and being populated into Metadata\') {\n			start (\'finish\')\n		}\n	}\n	\n	task(name: \'Delete Service Provider\', description: \'Deletes the Service Provider after it has been rejected.\') {\n		execute(script: \'spssodescriptor_delete\')\n		outcome(name: \'spssodescriptordeleted\', description:\'The Service Provider definition has been deleted\') {\n			start (\'finish\')\n		}\n	}\n\n	task(name: \'finish\', description: \'Completes the spssodescriptor_create workflow\') {\n		finish()\n	}\n	\n}','Workflow defining Service Provider creation and approval process',NULL,'2010-12-14 13:08:49','spssodescriptor_create',1);
/*!40000 ALTER TABLE `process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_instance`
--

DROP TABLE IF EXISTS `process_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `completion_acknowlegded` bit(1) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `priority` varchar(255) NOT NULL,
  `process_id` bigint(20) NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAEA874655190F52A` (`process_id`),
  CONSTRAINT `FKAEA874655190F52A` FOREIGN KEY (`process_id`) REFERENCES `process` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_instance`
--

LOCK TABLES `process_instance` WRITE;
/*!40000 ALTER TABLE `process_instance` DISABLE KEYS */;
INSERT INTO `process_instance` VALUES (1,4,'\0','2010-12-14 13:08:47','Approval for creation of spssodescriptor:[id:8, displayName: How to cook Shrimp]','2010-12-14 13:14:36','MEDIUM',4,'INPROGRESS');
/*!40000 ALTER TABLE `process_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_instance_params`
--

DROP TABLE IF EXISTS `process_instance_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_instance_params` (
  `params` bigint(20) DEFAULT NULL,
  `params_idx` varchar(255) DEFAULT NULL,
  `params_elt` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_instance_params`
--

LOCK TABLES `process_instance_params` WRITE;
/*!40000 ALTER TABLE `process_instance_params` DISABLE KEYS */;
INSERT INTO `process_instance_params` VALUES (1,'creator','1'),(1,'serviceProvider','8'),(1,'organization','1'),(1,'locale','en');
/*!40000 ALTER TABLE `process_instance_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile_base`
--

DROP TABLE IF EXISTS `profile_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profile_base` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_hash` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `non_verified_email` varchar(255) DEFAULT NULL,
  `class` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile_base`
--

LOCK TABLES `profile_base` WRITE;
/*!40000 ALTER TABLE `profile_base` DISABLE KEYS */;
INSERT INTO `profile_base` VALUES (1,0,'2010-12-14 11:50:40','internaladministrator@not.valid','8b9ff1c54cc67441fb45cc5ec2087497',NULL,'2010-12-14 11:50:40',NULL,NULL,'fedreg.host.Profile'),(2,0,'2010-12-14 11:51:18','thatsaknife@dropbear.edu.au','9c122f37f7fdef033552e33bec2fdd43','Mick Dundee','2010-12-14 11:51:18',NULL,NULL,'fedreg.host.Profile'),(3,0,'2010-12-14 13:15:36','crikey@yowie.com.au','1ce2f3c367ea09a5154eef3b3d62f137','Steve Irwin','2010-12-14 13:15:36',NULL,NULL,'fedreg.host.Profile');
/*!40000 ALTER TABLE `profile_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_descriptor`
--

DROP TABLE IF EXISTS `role_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_descriptor` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) NOT NULL,
  `approved` bit(1) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` longtext NOT NULL,
  `display_name` varchar(255) NOT NULL,
  `errorurl_id` bigint(20) DEFAULT NULL,
  `extensions` longtext,
  `last_updated` datetime DEFAULT NULL,
  `organization_id` bigint(20) NOT NULL,
  `reporting` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAF7FE2D8E5FA11AA` (`organization_id`),
  KEY `FKAF7FE2D84B267440` (`errorurl_id`),
  CONSTRAINT `FKAF7FE2D84B267440` FOREIGN KEY (`errorurl_id`) REFERENCES `urluri` (`id`),
  CONSTRAINT `FKAF7FE2D8E5FA11AA` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_descriptor`
--

LOCK TABLES `role_descriptor` WRITE;
/*!40000 ALTER TABLE `role_descriptor` DISABLE KEYS */;
INSERT INTO `role_descriptor` VALUES (2,'','','2010-12-14 11:50:51','Identity Provider for Drop Bear university','Drop Bear IDP',NULL,NULL,'2010-12-14 11:50:51',1,''),(3,'','','2010-12-14 11:50:51','Identity Provider for Drop Bear university','Drop Bear IDP',NULL,NULL,'2010-12-14 11:50:51',1,''),(5,'','','2010-12-14 12:51:25','Identity Provider for University of Yowie','Yowie IDP',NULL,NULL,'2010-12-14 12:51:25',2,''),(6,'','','2010-12-14 12:51:25','Identity Provider for University of Yowie','Yowie IDP',NULL,NULL,'2010-12-14 12:51:25',2,''),(8,'','','2010-12-14 13:08:47','A service providing extensive knowledge base on throwing Shrimp on the BBQ the right way.','How to cook Shrimp',NULL,NULL,'2010-12-14 13:14:36',1,'');
/*!40000 ALTER TABLE `role_descriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_descriptor_samluri`
--

DROP TABLE IF EXISTS `role_descriptor_samluri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_descriptor_samluri` (
  `role_descriptor_protocol_support_enumerations_id` bigint(20) NOT NULL,
  `samluri_id` bigint(20) DEFAULT NULL,
  KEY `FK3FF0C3783688E26A` (`samluri_id`),
  KEY `FK3FF0C378891B7F3` (`role_descriptor_protocol_support_enumerations_id`),
  CONSTRAINT `FK3FF0C378891B7F3` FOREIGN KEY (`role_descriptor_protocol_support_enumerations_id`) REFERENCES `role_descriptor` (`id`),
  CONSTRAINT `FK3FF0C3783688E26A` FOREIGN KEY (`samluri_id`) REFERENCES `samluri` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_descriptor_samluri`
--

LOCK TABLES `role_descriptor_samluri` WRITE;
/*!40000 ALTER TABLE `role_descriptor_samluri` DISABLE KEYS */;
INSERT INTO `role_descriptor_samluri` VALUES (2,1),(3,1),(5,1),(6,1),(8,1);
/*!40000 ALTER TABLE `role_descriptor_samluri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samluri`
--

DROP TABLE IF EXISTS `samluri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samluri` (
  `id` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samluri`
--

LOCK TABLES `samluri` WRITE;
/*!40000 ALTER TABLE `samluri` DISABLE KEYS */;
INSERT INTO `samluri` VALUES (1,'ProtocolSupport'),(2,'ProtocolSupport'),(3,'ProtocolSupport'),(4,'ProtocolBinding'),(5,'ProtocolBinding'),(6,'ProtocolBinding'),(7,'ProtocolBinding'),(8,'ProtocolBinding'),(9,'ProtocolBinding'),(10,'ProtocolBinding'),(11,'ProtocolBinding'),(12,'ProtocolBinding'),(13,'ProtocolBinding'),(14,'ProtocolBinding'),(15,'NameIdentifierFormat'),(16,'NameIdentifierFormat'),(17,'NameIdentifierFormat'),(18,'NameIdentifierFormat'),(19,'NameIdentifierFormat'),(20,'NameIdentifierFormat'),(21,'NameIdentifierFormat'),(22,'NameIdentifierFormat'),(23,'NameIdentifierFormat'),(24,'AttributeNameFormat'),(25,'AttributeNameFormat'),(26,'AttributeNameFormat');
/*!40000 ALTER TABLE `samluri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_category`
--

DROP TABLE IF EXISTS `service_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_category`
--

LOCK TABLES `service_category` WRITE;
/*!40000 ALTER TABLE `service_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `service_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_description`
--

DROP TABLE IF EXISTS `service_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_description` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `accessing` longtext,
  `audience` longtext,
  `benefits` longtext,
  `connecturl` varchar(255) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `further_info` longtext,
  `last_updated` datetime DEFAULT NULL,
  `logourl` varchar(255) DEFAULT NULL,
  `maintenance` longtext,
  `provides` longtext,
  `publish` bit(1) NOT NULL,
  `restrictions` longtext,
  `support` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_description`
--

LOCK TABLES `service_description` WRITE;
/*!40000 ALTER TABLE `service_description` DISABLE KEYS */;
INSERT INTO `service_description` VALUES (1,0,NULL,NULL,'All Australians will be able to cook this stereotype when asked.','https://shrimp.bbq.dropbear.edu.au','2010-12-14 13:08:47',NULL,'2010-12-14 13:08:47',NULL,NULL,'An extensive wiki on how to \'throw a Shrimp on the barbie\'','\0',NULL,'Support provided by all good Shrimp BBQers Australia wide.');
/*!40000 ALTER TABLE `service_description` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_monitor`
--

DROP TABLE IF EXISTS `service_monitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_monitor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `check_period` int(11) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `role_descriptor_id` bigint(20) NOT NULL,
  `type_id` bigint(20) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1BC0A5D013FAB137` (`role_descriptor_id`),
  KEY `FK1BC0A5D03E6B7924` (`type_id`),
  CONSTRAINT `FK1BC0A5D03E6B7924` FOREIGN KEY (`type_id`) REFERENCES `monitor_type` (`id`),
  CONSTRAINT `FK1BC0A5D013FAB137` FOREIGN KEY (`role_descriptor_id`) REFERENCES `role_descriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_monitor`
--

LOCK TABLES `service_monitor` WRITE;
/*!40000 ALTER TABLE `service_monitor` DISABLE KEYS */;
/*!40000 ALTER TABLE `service_monitor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `single_logout_service`
--

DROP TABLE IF EXISTS `single_logout_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `single_logout_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB20C29D7C6661FF9` (`descriptor_id`),
  CONSTRAINT `FKB20C29D7C6661FF9` FOREIGN KEY (`descriptor_id`) REFERENCES `ssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `single_logout_service`
--

LOCK TABLES `single_logout_service` WRITE;
/*!40000 ALTER TABLE `single_logout_service` DISABLE KEYS */;
INSERT INTO `single_logout_service` VALUES (13,8),(14,8),(15,8),(16,8);
/*!40000 ALTER TABLE `single_logout_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `single_sign_on_service`
--

DROP TABLE IF EXISTS `single_sign_on_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `single_sign_on_service` (
  `id` bigint(20) NOT NULL,
  `descriptor_id` bigint(20) NOT NULL,
  `single_sign_on_services_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK811EE62081D2AAE4` (`descriptor_id`),
  CONSTRAINT `FK811EE62081D2AAE4` FOREIGN KEY (`descriptor_id`) REFERENCES `idpssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `single_sign_on_service`
--

LOCK TABLES `single_sign_on_service` WRITE;
/*!40000 ALTER TABLE `single_sign_on_service` DISABLE KEYS */;
INSERT INTO `single_sign_on_service` VALUES (3,3,0),(4,3,1),(7,6,0),(8,6,1);
/*!40000 ALTER TABLE `single_sign_on_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spssodescriptor`
--

DROP TABLE IF EXISTS `spssodescriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spssodescriptor` (
  `id` bigint(20) NOT NULL,
  `authn_requests_signed` bit(1) NOT NULL,
  `entity_descriptor_id` bigint(20) NOT NULL,
  `service_description_id` bigint(20) NOT NULL,
  `want_assertions_signed` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2AD07B814337A9DF` (`service_description_id`),
  KEY `FK2AD07B81D15ADD11` (`entity_descriptor_id`),
  CONSTRAINT `FK2AD07B81D15ADD11` FOREIGN KEY (`entity_descriptor_id`) REFERENCES `entity_descriptor` (`id`),
  CONSTRAINT `FK2AD07B814337A9DF` FOREIGN KEY (`service_description_id`) REFERENCES `service_description` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spssodescriptor`
--

LOCK TABLES `spssodescriptor` WRITE;
/*!40000 ALTER TABLE `spssodescriptor` DISABLE KEYS */;
INSERT INTO `spssodescriptor` VALUES (8,'\0',7,1,'\0');
/*!40000 ALTER TABLE `spssodescriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spssodescriptor_service_category`
--

DROP TABLE IF EXISTS `spssodescriptor_service_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spssodescriptor_service_category` (
  `spssodescriptor_service_categories_id` bigint(20) DEFAULT NULL,
  `service_category_id` bigint(20) DEFAULT NULL,
  KEY `FK56511C469F5860A7` (`spssodescriptor_service_categories_id`),
  KEY `FK56511C462510C975` (`service_category_id`),
  CONSTRAINT `FK56511C462510C975` FOREIGN KEY (`service_category_id`) REFERENCES `service_category` (`id`),
  CONSTRAINT `FK56511C469F5860A7` FOREIGN KEY (`spssodescriptor_service_categories_id`) REFERENCES `spssodescriptor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spssodescriptor_service_category`
--

LOCK TABLES `spssodescriptor_service_category` WRITE;
/*!40000 ALTER TABLE `spssodescriptor_service_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `spssodescriptor_service_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ssodescriptor`
--

DROP TABLE IF EXISTS `ssodescriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ssodescriptor` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssodescriptor`
--

LOCK TABLES `ssodescriptor` WRITE;
/*!40000 ALTER TABLE `ssodescriptor` DISABLE KEYS */;
INSERT INTO `ssodescriptor` VALUES (3),(6),(8);
/*!40000 ALTER TABLE `ssodescriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ssodescriptor_samluri`
--

DROP TABLE IF EXISTS `ssodescriptor_samluri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ssodescriptor_samluri` (
  `ssodescriptor_nameidformats_id` bigint(20) DEFAULT NULL,
  `samluri_id` bigint(20) DEFAULT NULL,
  KEY `FKBF8D255E3688E26A` (`samluri_id`),
  KEY `FKBF8D255ED6875DB3` (`ssodescriptor_nameidformats_id`),
  CONSTRAINT `FKBF8D255ED6875DB3` FOREIGN KEY (`ssodescriptor_nameidformats_id`) REFERENCES `ssodescriptor` (`id`),
  CONSTRAINT `FKBF8D255E3688E26A` FOREIGN KEY (`samluri_id`) REFERENCES `samluri` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssodescriptor_samluri`
--

LOCK TABLES `ssodescriptor_samluri` WRITE;
/*!40000 ALTER TABLE `ssodescriptor_samluri` DISABLE KEYS */;
INSERT INTO `ssodescriptor_samluri` VALUES (8,22),(6,22);
/*!40000 ALTER TABLE `ssodescriptor_samluri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `description` varchar(255) NOT NULL,
  `finish_on_this_task` bit(1) NOT NULL,
  `last_updated` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `process_id` bigint(20) NOT NULL,
  `tasks_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3635855190F52A` (`process_id`),
  CONSTRAINT `FK3635855190F52A` FOREIGN KEY (`process_id`) REFERENCES `process` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,0,'2010-12-14 11:50:41','Invokes a script to confirm registration of IDP.','\0','2010-12-14 11:50:41','Send confirmation message',1,0),(2,0,'2010-12-14 11:50:41','Ensures that organization-{organization}-administrators is actually populated with users, if not bypasses directly to federation administrators.','\0','2010-12-14 11:50:41','Check for valid approvers',1,1),(3,0,'2010-12-14 11:50:41','Requests that a user who is an administrative member of the owning organization approves creation and associated billing by introducing this Identity Provider.','\0','2010-12-14 11:50:41','Request organization approval',1,2),(4,0,'2010-12-14 11:50:41','Requests that a user who is an administrative member of the federation approves activation for this Identity Provider as a valuable addition for the federation.','\0','2010-12-14 11:50:41','Request Federation approval',1,3),(5,0,'2010-12-14 11:50:41','Requests that a user who is an administrative member of the federation approves activation for this Identity Provider for the Federation and on behalf of the owning Organization. External verification should be undertaken.','\0','2010-12-14 11:50:41','Request executive federation approval',1,4),(6,0,'2010-12-14 11:50:41','Activates the Identity Provider so it can be rendered into Metadata.','\0','2010-12-14 11:50:41','Activate Identity Provider',1,5),(7,0,'2010-12-14 11:50:41','Deletes the Identity Provider after it has been rejected.','\0','2010-12-14 11:50:41','Delete Identity Provider',1,6),(8,0,'2010-12-14 11:50:41','Completes the idpssodescriptor_create workflow','','2010-12-14 11:50:41','finish',1,7),(9,0,'2010-12-14 11:50:41','Invokes a script to confirm registration of an Organization.','\0','2010-12-14 11:50:41','Send confirmation message',2,0),(10,0,'2010-12-14 11:50:41','Requests that a user who is an administrative member of the federation approves activation for this Organization as a valuable addition for the federation.','\0','2010-12-14 11:50:41','Request Federation approval',2,1),(11,0,'2010-12-14 11:50:41','Activates the Organization so it can register components with the federation','\0','2010-12-14 11:50:41','Activate Organization',2,2),(12,0,'2010-12-14 11:50:41','Deletes the Organization after it has been rejected.','\0','2010-12-14 11:50:41','Delete Organization',2,3),(13,0,'2010-12-14 11:50:41','Completes the organization_create workflow','','2010-12-14 11:50:41','finish',2,4),(14,0,'2010-12-14 11:50:41','Automatically approves all new attributes for SPSSOdescriptors, useful as default in test environments','\0','2010-12-14 11:50:41','Auto approve',3,0),(15,0,'2010-12-14 11:50:41','Completes the requestedattribute_create workflow','','2010-12-14 11:50:41','finish',3,1),(16,1,'2010-12-14 11:50:41','Invokes a script to confirm registration of SP.','\0','2010-12-14 13:08:49','Send confirmation message',4,0),(17,1,'2010-12-14 11:50:41','Ensures that organization-{organization}-administrators is actually populated with users, if not bypasses directly to federation administrators.','\0','2010-12-14 13:08:49','Check for valid approvers',4,1),(18,1,'2010-12-14 11:50:41','Requests that a user who is an administrative member of the owning organization approves creation and associated billing by introducing this Service Provider.','\0','2010-12-14 13:08:49','Request organization approval',4,2),(19,1,'2010-12-14 11:50:41','Requests that a user who is an administrative member of the federation approves activation for this Service Provider as a valuable addition for the federation.','\0','2010-12-14 13:14:00','Request Federation approval',4,3),(20,0,'2010-12-14 11:50:41','Requests that a user who is an administrative member of the federation approves activation for this Service Provider for the Federation and on behalf of the owning Organization. External verification should be undertaken.','\0','2010-12-14 11:50:41','Request executive federation approval',4,4),(21,1,'2010-12-14 11:50:41','Activates the Service Provider so it can be rendered into Metadata.','\0','2010-12-14 13:14:36','Activate Service Provider',4,5),(22,0,'2010-12-14 11:50:41','Deletes the Service Provider after it has been rejected.','\0','2010-12-14 11:50:41','Delete Service Provider',4,6),(23,1,'2010-12-14 11:50:41','Completes the spssodescriptor_create workflow','','2010-12-14 13:14:36','finish',4,7);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_approver_groups`
--

DROP TABLE IF EXISTS `task_approver_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_approver_groups` (
  `task_id` bigint(20) DEFAULT NULL,
  `approver_groups_string` varchar(255) DEFAULT NULL,
  `approver_groups_idx` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_approver_groups`
--

LOCK TABLES `task_approver_groups` WRITE;
/*!40000 ALTER TABLE `task_approver_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_approver_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_approver_roles`
--

DROP TABLE IF EXISTS `task_approver_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_approver_roles` (
  `task_id` bigint(20) DEFAULT NULL,
  `approver_roles_string` varchar(255) DEFAULT NULL,
  `approver_roles_idx` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_approver_roles`
--

LOCK TABLES `task_approver_roles` WRITE;
/*!40000 ALTER TABLE `task_approver_roles` DISABLE KEYS */;
INSERT INTO `task_approver_roles` VALUES (3,'organization-{organization}-administrators',0),(4,'federation-administrators',0),(5,'federation-administrators',0),(10,'federation-administrators',0),(18,'organization-{organization}-administrators',0),(19,'federation-administrators',0),(20,'federation-administrators',0);
/*!40000 ALTER TABLE `task_approver_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_approvers`
--

DROP TABLE IF EXISTS `task_approvers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_approvers` (
  `task_id` bigint(20) DEFAULT NULL,
  `approvers_string` varchar(255) DEFAULT NULL,
  `approvers_idx` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_approvers`
--

LOCK TABLES `task_approvers` WRITE;
/*!40000 ALTER TABLE `task_approvers` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_approvers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_dependencies`
--

DROP TABLE IF EXISTS `task_dependencies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_dependencies` (
  `task_id` bigint(20) DEFAULT NULL,
  `dependencies_string` varchar(255) DEFAULT NULL,
  KEY `FK6B2811A3C4B34BCA` (`task_id`),
  CONSTRAINT `FK6B2811A3C4B34BCA` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_dependencies`
--

LOCK TABLES `task_dependencies` WRITE;
/*!40000 ALTER TABLE `task_dependencies` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_dependencies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_execute`
--

DROP TABLE IF EXISTS `task_execute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_execute` (
  `task_id` bigint(20) DEFAULT NULL,
  `execute_string` varchar(255) DEFAULT NULL,
  `execute_idx` varchar(255) DEFAULT NULL,
  `execute_elt` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_execute`
--

LOCK TABLES `task_execute` WRITE;
/*!40000 ALTER TABLE `task_execute` DISABLE KEYS */;
INSERT INTO `task_execute` VALUES (1,NULL,'script','idpssodescriptor_confirm'),(2,NULL,'script','organization_administrators_populated'),(6,NULL,'script','idpssodescriptor_activate'),(7,NULL,'script','idpssodescriptor_delete'),(9,NULL,'script','organization_confirm'),(11,NULL,'script','organization_activate'),(12,NULL,'script','organization_delete'),(14,NULL,'script','requestedattribute_activate'),(16,NULL,'script','spssodescriptor_confirm'),(17,NULL,'script','organization_administrators_populated'),(21,NULL,'script','spssodescriptor_activate'),(22,NULL,'script','spssodescriptor_delete');
/*!40000 ALTER TABLE `task_execute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_instance`
--

DROP TABLE IF EXISTS `task_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `approver_id` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `process_instance_id` bigint(20) NOT NULL,
  `status` varchar(255) NOT NULL,
  `task_id` bigint(20) NOT NULL,
  `task_instances_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2198F60F4FC59D29` (`process_instance_id`),
  KEY `FK2198F60FDD269C5D` (`approver_id`),
  KEY `FK2198F60FC4B34BCA` (`task_id`),
  CONSTRAINT `FK2198F60FC4B34BCA` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`),
  CONSTRAINT `FK2198F60F4FC59D29` FOREIGN KEY (`process_instance_id`) REFERENCES `process_instance` (`id`),
  CONSTRAINT `FK2198F60FDD269C5D` FOREIGN KEY (`approver_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_instance`
--

LOCK TABLES `task_instance` WRITE;
/*!40000 ALTER TABLE `task_instance` DISABLE KEYS */;
INSERT INTO `task_instance` VALUES (1,1,NULL,'2010-12-14 13:08:47','2010-12-14 13:08:49',1,'SUCCESSFUL',16,0),(2,1,NULL,'2010-12-14 13:08:48','2010-12-14 13:08:49',1,'SUCCESSFUL',17,1),(3,2,2,'2010-12-14 13:08:48','2010-12-14 13:14:00',1,'SUCCESSFUL',18,2),(4,2,2,'2010-12-14 13:13:59','2010-12-14 13:14:36',1,'SUCCESSFUL',19,3),(5,2,NULL,'2010-12-14 13:14:36','2010-12-14 13:14:36',1,'SUCCESSFUL',21,4),(6,1,NULL,'2010-12-14 13:14:36','2010-12-14 13:14:36',1,'FINALIZED',23,5);
/*!40000 ALTER TABLE `task_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_instance__user`
--

DROP TABLE IF EXISTS `task_instance__user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_instance__user` (
  `task_instance_potential_approvers_id` bigint(20) DEFAULT NULL,
  `user_base_id` bigint(20) DEFAULT NULL,
  KEY `FK8D93F67AFA4F085D` (`user_base_id`),
  KEY `FK8D93F67AC9D134C7` (`task_instance_potential_approvers_id`),
  CONSTRAINT `FK8D93F67AC9D134C7` FOREIGN KEY (`task_instance_potential_approvers_id`) REFERENCES `task_instance` (`id`),
  CONSTRAINT `FK8D93F67AFA4F085D` FOREIGN KEY (`user_base_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_instance__user`
--

LOCK TABLES `task_instance__user` WRITE;
/*!40000 ALTER TABLE `task_instance__user` DISABLE KEYS */;
INSERT INTO `task_instance__user` VALUES (3,2),(4,2);
/*!40000 ALTER TABLE `task_instance__user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_outcome`
--

DROP TABLE IF EXISTS `task_outcome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_outcome` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE75A8E98C4B34BCA` (`task_id`),
  CONSTRAINT `FKE75A8E98C4B34BCA` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_outcome`
--

LOCK TABLES `task_outcome` WRITE;
/*!40000 ALTER TABLE `task_outcome` DISABLE KEYS */;
INSERT INTO `task_outcome` VALUES (1,0,'User registering the IDP has been advised of creation','confirmedidpssodescriptor',1),(2,0,'The Organization has administrators and they will be asked to approve.','organization_hasadministrators',2),(3,0,'The Organization has no locally registered administrators. Federation Administrators will undertake an out of band approval process.','organization_noadministrators',2),(4,0,'The organization has accepted responsibility for this Identity Provider','organizationapproved',3),(5,0,'The organization has accepted responsibility for this Identity Provider','federationapproved',4),(6,0,'The organization has accepted responsibility for this Identity Provider','federationapproved',5),(7,0,'The Identity Provider is now activate and being populated into Metadata','idpssodescriptoractivated',6),(8,0,'The Identity Provider definition has been deleted','idpssodescriptordeleted',7),(9,0,'User registering the Organization has been advised of creation','confirmedorganization',9),(10,0,'The organization has accepted responsibility for this Identity Provider','federationapproved',10),(11,0,'The Organization is now activate and being populated into Metadata','organizationactivated',11),(12,0,'The Organization definition has been deleted','organizationdeleted',12),(13,0,'The Requested Attribute is now activate and being populated into Metadata','requestedattributeactivated',14),(14,0,'User registering the SP has been advised of creation','confirmedspssodescriptor',16),(15,0,'The Organization has administrators and they will be asked to approve.','organization_hasadministrators',17),(16,0,'The Organization has no locally registered administrators. Federation Administrators will undertake an out of band approval process.','organization_noadministrators',17),(17,0,'The organization has accepted responsibility for this Service Provider','organizationapproved',18),(18,0,'The organization has accepted responsibility for this Service Provider','federationapproved',19),(19,0,'The organization and federation combined have accepted responsibility for this Service Provider','executivelyapproved',20),(20,0,'The Service Provider is now activate and being populated into Metadata','spssodescriptoractivated',21),(21,0,'The Service Provider definition has been deleted','spssodescriptordeleted',22);
/*!40000 ALTER TABLE `task_outcome` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_outcome_start`
--

DROP TABLE IF EXISTS `task_outcome_start`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_outcome_start` (
  `task_outcome_id` bigint(20) DEFAULT NULL,
  `start_string` varchar(255) DEFAULT NULL,
  `start_idx` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_outcome_start`
--

LOCK TABLES `task_outcome_start` WRITE;
/*!40000 ALTER TABLE `task_outcome_start` DISABLE KEYS */;
INSERT INTO `task_outcome_start` VALUES (1,'Check for valid approvers',0),(2,'Request organization approval',0),(3,'Request executive federation approval',0),(4,'Request Federation approval',0),(5,'Activate Identity Provider',0),(6,'Activate Identity Provider',0),(7,'finish',0),(8,'finish',0),(9,'Request Federation approval',0),(10,'Activate Organization',0),(11,'finish',0),(12,'finish',0),(13,'finish',0),(14,'Check for valid approvers',0),(15,'Request organization approval',0),(16,'Request executive federation approval',0),(17,'Request Federation approval',0),(18,'Activate Service Provider',0),(19,'Activate Service Provider',0),(20,'finish',0),(21,'finish',0);
/*!40000 ALTER TABLE `task_outcome_start` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_outcome_terminate`
--

DROP TABLE IF EXISTS `task_outcome_terminate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_outcome_terminate` (
  `task_outcome_id` bigint(20) DEFAULT NULL,
  `terminate_string` varchar(255) DEFAULT NULL,
  `terminate_idx` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_outcome_terminate`
--

LOCK TABLES `task_outcome_terminate` WRITE;
/*!40000 ALTER TABLE `task_outcome_terminate` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_outcome_terminate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_outcomes`
--

DROP TABLE IF EXISTS `task_outcomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_outcomes` (
  `outcomes_id` bigint(20) DEFAULT NULL,
  `task_id` bigint(20) NOT NULL,
  `outcomes_idx` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_outcomes`
--

LOCK TABLES `task_outcomes` WRITE;
/*!40000 ALTER TABLE `task_outcomes` DISABLE KEYS */;
INSERT INTO `task_outcomes` VALUES (1,1,'confirmedidpssodescriptor'),(2,2,'organization_hasadministrators'),(2,3,'organization_noadministrators'),(3,4,'organizationapproved'),(4,5,'federationapproved'),(5,6,'federationapproved'),(6,7,'idpssodescriptoractivated'),(7,8,'idpssodescriptordeleted'),(9,9,'confirmedorganization'),(10,10,'federationapproved'),(11,11,'organizationactivated'),(12,12,'organizationdeleted'),(14,13,'requestedattributeactivated'),(16,14,'confirmedspssodescriptor'),(17,15,'organization_hasadministrators'),(17,16,'organization_noadministrators'),(18,17,'organizationapproved'),(19,18,'federationapproved'),(20,19,'executivelyapproved'),(21,20,'spssodescriptoractivated'),(22,21,'spssodescriptordeleted');
/*!40000 ALTER TABLE `task_outcomes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_rejection`
--

DROP TABLE IF EXISTS `task_rejection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_rejection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAB7159CFC4B34BCA` (`task_id`),
  CONSTRAINT `FKAB7159CFC4B34BCA` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_rejection`
--

LOCK TABLES `task_rejection` WRITE;
/*!40000 ALTER TABLE `task_rejection` DISABLE KEYS */;
INSERT INTO `task_rejection` VALUES (1,0,'Not an Identity Provider associated with this organization (Identity Provider details will be discarded)','Not Associated',3),(2,0,'The organization will not accept ownership for this Identity Provider (Identity Provider details will be discarded)','Not Accepted',3),(3,0,'The federation will not accept ownership for this Identity Provider (Identity Provider details will be discarded)','Not Accepted',4),(4,0,'The federation will not accept ownership for this Identity Provider (Identity Provider details will be discarded).','Not Accepted',5),(5,0,'The organization responsible will not accept ownership for this Identity Provider (Identity Provider details will be discarded).','Organization Not Accepted',5),(6,0,'The federation will not accept this Organization (Organization details will be discarded)','Not Accepted',10),(7,0,'Not a Service Provider associated with this organization (Service Provider details will be discarded)','Not Associated',18),(8,0,'The organization will not accept ownership for this Service Provider (Service Provider details will be discarded)','Not Accepted',18),(9,0,'The federation will not accept ownership for this Service Provider (Service Provider details will be discarded)','Not Accepted',19),(10,0,'The federation will not accept ownership for this Service Provider (Service Provider details will be discarded).','Not Accepted',20),(11,0,'The organization responsible will not accept ownership for this Service Provider (Service Provider details will be discarded).','Organization Not Accepted',20);
/*!40000 ALTER TABLE `task_rejection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_rejection_start`
--

DROP TABLE IF EXISTS `task_rejection_start`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_rejection_start` (
  `task_rejection_id` bigint(20) DEFAULT NULL,
  `start_string` varchar(255) DEFAULT NULL,
  `start_idx` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_rejection_start`
--

LOCK TABLES `task_rejection_start` WRITE;
/*!40000 ALTER TABLE `task_rejection_start` DISABLE KEYS */;
INSERT INTO `task_rejection_start` VALUES (1,'Delete Identity Provider',0),(2,'Delete Identity Provider',0),(3,'Delete Identity Provider',0),(4,'Delete Identity Provider',0),(5,'Delete Identity Provider',0),(6,'Delete Organization',0),(7,'Delete Service Provider',0),(8,'Delete Service Provider',0),(9,'Delete Service Provider',0),(10,'Delete Service Provider',0),(11,'Delete Service Provider',0);
/*!40000 ALTER TABLE `task_rejection_start` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_rejection_terminate`
--

DROP TABLE IF EXISTS `task_rejection_terminate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_rejection_terminate` (
  `task_rejection_id` bigint(20) DEFAULT NULL,
  `terminate_string` varchar(255) DEFAULT NULL,
  `terminate_idx` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_rejection_terminate`
--

LOCK TABLES `task_rejection_terminate` WRITE;
/*!40000 ALTER TABLE `task_rejection_terminate` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_rejection_terminate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_rejections`
--

DROP TABLE IF EXISTS `task_rejections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_rejections` (
  `rejections_id` bigint(20) DEFAULT NULL,
  `task_id` bigint(20) NOT NULL,
  `rejections_idx` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_rejections`
--

LOCK TABLES `task_rejections` WRITE;
/*!40000 ALTER TABLE `task_rejections` DISABLE KEYS */;
INSERT INTO `task_rejections` VALUES (3,1,'Not Associated'),(3,2,'Not Accepted'),(4,3,'Not Accepted'),(5,4,'Not Accepted'),(5,5,'Organization Not Accepted'),(10,6,'Not Accepted'),(18,7,'Not Associated'),(18,8,'Not Accepted'),(19,9,'Not Accepted'),(20,10,'Not Accepted'),(20,11,'Organization Not Accepted');
/*!40000 ALTER TABLE `task_rejections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tel_numuri`
--

DROP TABLE IF EXISTS `tel_numuri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tel_numuri` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tel_numuri`
--

LOCK TABLES `tel_numuri` WRITE;
/*!40000 ALTER TABLE `tel_numuri` DISABLE KEYS */;
/*!40000 ALTER TABLE `tel_numuri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uri`
--

DROP TABLE IF EXISTS `uri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uri` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `uri` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uri`
--

LOCK TABLES `uri` WRITE;
/*!40000 ALTER TABLE `uri` DISABLE KEYS */;
INSERT INTO `uri` VALUES (1,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:protocol'),(2,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:1.1:protocol'),(3,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:mace:shibboleth:1.0'),(4,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect'),(5,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST'),(6,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact'),(7,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign'),(8,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:bindings:PAOS'),(9,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:bindings:SOAP'),(10,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:mace:shibboleth:1.0:profiles:AuthnRequest'),(11,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:1.0:profiles:browser-post'),(12,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:1.0:profiles:artifact-01'),(13,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding'),(14,0,'2010-12-14 11:50:41',NULL,'2010-12-14 11:50:41','urn:oasis:names:tc:SAML:profiles:SSO:idp-discovery-protocol'),(15,0,'2010-12-14 11:50:41','The interpretation of the content of the element is left to individual implementations.','2010-12-14 11:50:41','urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified'),(16,0,'2010-12-14 11:50:41','Indicates that the content of the element is in the form of an email address, specifically addr-spec as defined in IETF RFC 2822','2010-12-14 11:50:41','urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress'),(17,0,'2010-12-14 11:50:41','Indicates that the content of the element is in the form specified for the contents of the <ds:X509SubjectName> element in the XML Signature Recommendation [XMLSig].','2010-12-14 11:50:41','urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName'),(18,0,'2010-12-14 11:50:41','Indicates that the content of the element is a Windows domain qualified name. A Windows domain qualified user name is a string of the form \"DomainName\\UserName\"','2010-12-14 11:50:41','urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName'),(19,0,'2010-12-14 11:50:41','Indicates that the content of the element is in the form of a Kerberos principal name using the format name[/instance]@REALM','2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:nameid-format:kerberos'),(20,0,'2010-12-14 11:50:41','Indicates that the content of the element is the identifier of an entity that provides SAML-based services or is a participant in SAML profiles.','2010-12-14 11:50:41','urn:oasis:names:tc:SAML:2.0:nameid-format:entity'),(21,0,'2010-12-14 11:50:42','Indicates that the content of the element is a persistent opaque identifier for a principal that is specific to an identity provider and a service provider or affiliation of service providers.','2010-12-14 11:50:42','urn:oasis:names:tc:SAML:2.0:nameid-format:persistent'),(22,0,'2010-12-14 11:50:42','Indicates that the content of the element is an identifier with transient semantics and SHOULD be treated as an opaque and temporary value by the relying party.','2010-12-14 11:50:42','urn:oasis:names:tc:SAML:2.0:nameid-format:transient'),(23,0,'2010-12-14 11:50:42',NULL,'2010-12-14 11:50:42','urn:mace:shibboleth:1.0:nameIdentifier'),(24,0,'2010-12-14 11:50:42',NULL,'2010-12-14 11:50:42','urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified'),(25,0,'2010-12-14 11:50:42',NULL,'2010-12-14 11:50:42','urn:oasis:names:tc:SAML:2.0:attrname-format:uri'),(26,0,'2010-12-14 11:50:42',NULL,'2010-12-14 11:50:42','urn:oasis:names:tc:SAML:2.0:attrname-format:basic'),(27,1,'2010-12-14 11:50:50',NULL,'2010-12-14 11:50:50','http://www.dropbear.edu.au'),(28,1,'2010-12-14 11:50:50',NULL,'2010-12-14 11:50:51','thatsaknife@dropbear.edu.au'),(29,1,'2010-12-14 11:50:51',NULL,'2010-12-14 11:50:51','https://idp.dropbear.edu.au/idp/profile/SAML2/SOAP/AttributeQuery'),(30,1,'2010-12-14 11:50:51',NULL,'2010-12-14 11:50:51','https://idp.dropbear.edu.au/idp/profile/SAML2/SOAP/ArtifactResolution'),(31,1,'2010-12-14 11:50:51',NULL,'2010-12-14 11:50:51','https://idp.dropbear.edu.au/idp/profile/SAML2/POST/SSO'),(32,1,'2010-12-14 11:50:51',NULL,'2010-12-14 11:50:51','https://idp.dropbear.edu.au/idp/profile/SAML2/Redirect/SSO'),(33,1,'2010-12-14 12:51:25',NULL,'2010-12-14 12:51:25','http://www.yowie.edu.au'),(34,1,'2010-12-14 12:51:25',NULL,'2010-12-14 12:51:26','crikey@yowie.edu.au'),(35,1,'2010-12-14 12:51:25',NULL,'2010-12-14 12:51:26','https://idp.yowie.edu.au/idp/profile/SAML2/SOAP/AttributeQuery'),(36,1,'2010-12-14 12:51:25',NULL,'2010-12-14 12:51:26','https://idp.yowie.edu.au/idp/profile/SAML2/SOAP/ArtifactResolution'),(37,1,'2010-12-14 12:51:26',NULL,'2010-12-14 12:51:26','https://idp.yowie.edu.au/idp/profile/SAML2/POST/SSO'),(38,1,'2010-12-14 12:51:26',NULL,'2010-12-14 12:51:26','https://idp.yowie.edu.au/idp/profile/SAML2/Redirect/SSO'),(39,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/NIM/Redirect'),(40,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/NIM/POST'),(41,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/NIM/Artifact'),(42,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/NIM/SOAP'),(43,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SLO/SOAP'),(44,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SLO/Artifact'),(45,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SLO/POST'),(46,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SLO/Redirect'),(47,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SAML2/Artifact'),(48,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/SAML2/POST'),(49,1,'2010-12-14 13:08:47',NULL,'2010-12-14 13:08:49','https://shrimp.bbq.dropbear.edu.au/Shibboleth.sso/DS'),(50,1,'2010-12-14 13:15:36',NULL,'2010-12-14 13:15:36','crikey@yowie.com.au');
/*!40000 ALTER TABLE `uri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `url`
--

DROP TABLE IF EXISTS `url`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `url` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `alt_text` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `location` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `url`
--

LOCK TABLES `url` WRITE;
/*!40000 ALTER TABLE `url` DISABLE KEYS */;
INSERT INTO `url` VALUES (1,0,'fedreg.templates.federation.shibboleth.alttext',NULL,'http://www.aaf.edu.au',NULL);
/*!40000 ALTER TABLE `url` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `urluri`
--

DROP TABLE IF EXISTS `urluri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `urluri` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `urluri`
--

LOCK TABLES `urluri` WRITE;
/*!40000 ALTER TABLE `urluri` DISABLE KEYS */;
INSERT INTO `urluri` VALUES (27),(29),(30),(31),(32),(33),(35),(36),(37),(38),(39),(40),(41),(42),(43),(44),(45),(46),(47),(48),(49);
/*!40000 ALTER TABLE `urluri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflow_message`
--

DROP TABLE IF EXISTS `workflow_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflow_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `message` varchar(255) NOT NULL,
  `process_instance_id` bigint(20) NOT NULL,
  `task_instance_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK94DE4F874FC59D29` (`process_instance_id`),
  KEY `FK94DE4F87569B596` (`creator_id`),
  KEY `FK94DE4F875221BAB5` (`task_instance_id`),
  CONSTRAINT `FK94DE4F875221BAB5` FOREIGN KEY (`task_instance_id`) REFERENCES `task_instance` (`id`),
  CONSTRAINT `FK94DE4F874FC59D29` FOREIGN KEY (`process_instance_id`) REFERENCES `process_instance` (`id`),
  CONSTRAINT `FK94DE4F87569B596` FOREIGN KEY (`creator_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflow_message`
--

LOCK TABLES `workflow_message` WRITE;
/*!40000 ALTER TABLE `workflow_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `workflow_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflow_script`
--

DROP TABLE IF EXISTS `workflow_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflow_script` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `creator_id` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `definition` longtext NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `last_editor_id` bigint(20) DEFAULT NULL,
  `last_updated` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK828A914B81BFCA4C` (`last_editor_id`),
  KEY `FK828A914B569B596` (`creator_id`),
  CONSTRAINT `FK828A914B569B596` FOREIGN KEY (`creator_id`) REFERENCES `_user` (`id`),
  CONSTRAINT `FK828A914B81BFCA4C` FOREIGN KEY (`last_editor_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflow_script`
--

LOCK TABLES `workflow_script` WRITE;
/*!40000 ALTER TABLE `workflow_script` DISABLE KEYS */;
INSERT INTO `workflow_script` VALUES (1,0,1,'2010-12-14 11:50:40','\nimport grails.plugins.nimble.core.*\nimport fedreg.core.*\n\n\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\ninvitationService = ctx.getBean(\"invitationService\")\nroleService = ctx.getBean(\"roleService\")\npermissionService = ctx.getBean(\"permissionService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef idp = IDPSSODescriptor.get(env.identityProvider.toLong())\n\nif(idp) {\n\n	log.info \"Activating $idp. Workflow indicates it is valid and accepted for operation.\"\n	\n	idp.approved = true\n	idp.active = true\n	idp.save()\n	\n	if(idp.hasErrors()) {\n		throw new RuntimeException(\"Attempt to process activate in script idpssodescriptor_activate. Failed due to IDP fault on save\")\n	}\n		\n	if(idp.collaborator) {\n		idp.collaborator.active = true\n		idp.collaborator.approved = true\n		idp.collaborator.save()\n		\n		if(idp.collaborator.hasErrors()) {\n			throw new RuntimeException(\"Attempt to process activate in script idpssodescriptor_activate. Failed due to IDP collaborator fault on save\")\n		}\n	}\n\n	if(idp.entityDescriptor.approved == false || idp.entityDescriptor.active == false) {\n		idp.entityDescriptor.approved = true\n		idp.entityDescriptor.active = true\n		idp.entityDescriptor.save()\n		if(idp.entityDescriptor.hasErrors()) {\n			throw new RuntimeException(\"Attempt to process activate in script idpssodescriptor_activate. Failed due to IDP entityDescriptor fault on save\")\n		}\n	}\n	\n	// Create ED access control role\n	def edRole = Role.findWhere(name:\"descriptor-${idp.entityDescriptor.id}-administrators\")\n	if(!edRole){	// Generally expected state\n		edRole = roleService.createRole(\"descriptor-${idp.entityDescriptor.id}-administrators\", \"Global administrators for ${idp.entityDescriptor}\", false)\n	\n		LevelPermission permission = new LevelPermission()\n	    permission.populate(\"descriptor\", \"${idp.entityDescriptor.id}\", \"*\", null, null, null)\n	    permission.managed = false\n		permissionService.createPermission(permission, edRole)\n	}\n	\n	// Create IDP access control role\n	def role = Role.findWhere(name:\"descriptor-${idp.id}-administrators\")\n	if(!role){	// Expected state\n		role = roleService.createRole(\"descriptor-${idp.id}-administrators\", \"Global administrators for $idp\", false)\n	}\n	\n	// In our model the IDP role has permissions to edit the IDP and the AA\n	// Manage IDP\n	def permission = new LevelPermission()\n	permission.populate(\"descriptor\", \"${idp.id}\", \"*\", null, null, null)\n	permission.managed = false\n	permissionService.createPermission(permission, role)\n	\n	// Manage collaborating AA\n	def aaPermission = new LevelPermission()       \n    aaPermission.populate(\"descriptor\", \"${idp.collaborator.id}\", \"*\", null, null, null)\n    aaPermission.managed = false\n    permissionService.createPermission(aaPermission, role)\n	\n	def invitation = invitationService.create(null, role.id, null, \"IDPSSODescriptor\", \"show\", idp.id.toString())\n	\n	def creator = Contact.get(env.creator.toLong())\n	mailService.sendMail {            \n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.idp.activated.subject\", null, \"fedreg.templates.mail.workflow.idp.activated.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_activated_idp\", model:[identityProvider:idp, locale:env.locale, invitation:invitation]\n	}\n\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'idpssodescriptoractivated\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to process activate in script idpssodescriptor_activate. Failed because referenced IDP does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','idpssodescriptor_activate'),(2,0,1,'2010-12-14 11:50:40','import fedreg.core.*\n\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef idp = IDPSSODescriptor.get(env.identityProvider.toLong())\n\nif(idp) {	\n	def creator = Contact.get(env.creator.toLong())\n	mailService.sendMail {\n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.idp.registered.subject\", null, \"fedreg.templates.mail.workflow.idp.registered.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_registered_idp\", model:[identityProvider:idp, locale:env.locale]\n	}\n\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'confirmedidpssodescriptor\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to email confirmation in script idpssodescriptor_confirm. Failed because referenced IDP does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','idpssodescriptor_confirm'),(3,0,1,'2010-12-14 11:50:40','import fedreg.core.*\n\nentityDescriptorService = ctx.getBean(\"entityDescriptorService\")\nidpSSODescriptorService = ctx.getBean(\"IDPSSODescriptorService\")\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef idp = IDPSSODescriptor.read(env.identityProvider.toLong())\nif(idp) {\n	\n	def creator = Contact.read(env.creator.toLong())\n	def args = new Object[1]\n	args[0] = idp.displayName\n	mailService.sendMail {            \n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.idp.rejected.subject\", args, \"fedreg.templates.mail.workflow.idp.rejected.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_rejected_idp\", model:[identityProvider:idp, locale:env.locale]\n	}\n	\n	log.warn \"Deleting $idp. Workflow indicates it is invalid and no longer needed.\"\n	\n	def entityDescriptor = idp.entityDescriptor\n	if(entityDescriptor.holdsIDPOnly())\n		entityDescriptorService.delete(entityDescriptor.id)\n	else\n		idpSSODescriptorService.delete(idp.id)\n	\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'idpssodescriptordeleted\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to process delete in script idpssodescriptor_delete. Failed because referenced IDP does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','idpssodescriptor_delete'),(4,0,1,'2010-12-14 11:50:40','\nimport grails.plugins.nimble.core.*\nimport fedreg.core.*\n\n\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\ninvitationService = ctx.getBean(\"invitationService\")\nroleService = ctx.getBean(\"roleService\")\npermissionService = ctx.getBean(\"permissionService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef org = Organization.get(env.organization.toLong())\n\nif(org) {\n\n	log.info \"Activating $org. Workflow indicates it is valid and accepted for operation.\"\n	\n	org.approved = true\n	org.active = true\n	org.save()\n	\n	if(org.hasErrors()) {\n		throw new RuntimeException(\"Attempt to process activate in script organization_activate. Failed due to ${org} fault on save\")\n	}\n	\n	def role = Role.findWhere(name:\"organization-${org.id}-administrators\")\n	if(!role){	// Expected state\n		role = roleService.createRole(\"organization-${org.id}-administrators\", \"Global administrators for $org\", false)\n	}\n	\n	def permission = new LevelPermission()\n	permission.populate(\"organization\", \"${org.id}\", \"*\", null, null, null)\n	permission.managed = false\n	permissionService.createPermission(permission, role)\n	\n	def invitation = invitationService.create(null, role.id, null, \"organization\", \"show\", org.id.toString())\n	\n	def creator = Contact.get(env.creator.toLong())\n	mailService.sendMail {            \n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.org.activated.subject\", null, \"fedreg.templates.mail.workflow.org.activated.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_activated_organization\", model:[organization:org, locale:env.locale, invitation:invitation]\n	}\n\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'organizationactivated\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to process activate in script organization_activate. Failed because referenced organization does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','organization_activate'),(5,0,1,'2010-12-14 11:50:40','import grails.plugins.nimble.core.*\n\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\n\ndef orgAdminRole = Role.findWhere(name:\"organization-${env.organization}-administrators\")\n\nif(orgAdminRole) {\n	if(orgAdminRole.users?.size() > 0) {\n		workflowTaskService.complete(env.taskInstanceID.toLong(), \'organization_hasadministrators\')\n		return\n	}\n}\n\nworkflowTaskService.complete(env.taskInstanceID.toLong(), \'organization_noadministrators\')',NULL,NULL,'2010-12-14 11:50:40','organization_administrators_populated'),(6,0,1,'2010-12-14 11:50:40','import fedreg.core.*\n\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef org = Organization.get(env.organization.toLong())\n\nif(org) {	\n	def creator = Contact.get(env.creator.toLong())\n	mailService.sendMail {\n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.org.registered.subject\", null, \"fedreg.templates.mail.workflow.org.registered.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_registered_organization\", model:[organization:org, locale:env.locale]\n	}\n\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'confirmedorganization\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to email confirmation in script organization_confirm. Failed because referenced organization does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','organization_confirm'),(7,0,1,'2010-12-14 11:50:40','import fedreg.core.*\n\norganizationService = ctx.getBean(\"organizationService\")\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef org = Organization.read(env.organization.toLong())\nif(org) {\n	def creator = Contact.read(env.creator.toLong())\n	def args = new Object[1]\n	args[0] = org.displayName\n	mailService.sendMail {            \n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.org.rejected.subject\", args, \"fedreg.templates.mail.workflow.org.rejected.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_rejected_organization\", model:[organization:org, locale:env.locale]\n	}\n	\n	log.warn \"Deleting $org. Workflow indicates it is invalid and no longer needed.\"\n	\n	organizationService.delete(org.id)\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'organizationdeleted\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to process delete in script organization_delete. Failed because referenced organization does not exist\")\n}\n',NULL,NULL,'2010-12-14 11:50:40','organization_delete'),(8,0,1,'2010-12-14 11:50:40','\nimport grails.plugins.nimble.core.*\nimport fedreg.core.*\n\n\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\n\ndef requestedAttribute = RequestedAttribute.get(env.requestedAttribute.toLong())\n\nif(requestedAttribute) {\n\n	log.info \"Activating $requestedAttribute. Workflow indicates it is valid and accepted for operation.\"\n	\n	requestedAttribute.approved = true\n	requestedAttribute.save()\n	\n	if(requestedAttribute.hasErrors()) {\n		throw new RuntimeException(\"Attempt to process activate in script requestedattribute_activate. Failed due to ${requestedAttribute} fault on save\")\n	}\n\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'requestedattributeactivated\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to process activate in script requestedattribute_activate. Failed because referenced requested attribute does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','requestedattribute_activate'),(9,0,1,'2010-12-14 11:50:40','\nimport grails.plugins.nimble.core.*\nimport fedreg.core.*\n\n\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\ninvitationService = ctx.getBean(\"invitationService\")\nroleService = ctx.getBean(\"roleService\")\npermissionService = ctx.getBean(\"permissionService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef sp = SPSSODescriptor.get(env.serviceProvider.toLong())\n\nif(sp) {\n\n	log.info \"Activating $sp. Workflow indicates it is valid and accepted for operation.\"\n	\n	sp.approved = true\n	sp.active = true\n	sp.save()\n	\n	if(sp.hasErrors()) {\n		throw new RuntimeException(\"Attempt to process activate in script spssodescriptor_activate. Failed due to SP fault on save\")\n	}\n\n	if(sp.entityDescriptor.approved == false || sp.entityDescriptor.active == false) {\n		sp.entityDescriptor.approved = true\n		sp.entityDescriptor.active = true\n		sp.entityDescriptor.save()\n		if(sp.entityDescriptor.hasErrors()) {\n			throw new RuntimeException(\"Attempt to process activate in script spssodescriptor_activate. Failed due to SP entityDescriptor fault on save\")\n		}\n	}\n	\n	// Create ED access control role\n	def edRole = Role.findWhere(name:\"descriptor-${sp.entityDescriptor.id}-administrators\")\n	if(!edRole){	// Generally expected state\n		edRole = roleService.createRole(\"descriptor-${sp.entityDescriptor.id}-administrators\", \"Global administrators for ${sp.entityDescriptor}\", false)\n	\n		LevelPermission permission = new LevelPermission()\n	    permission.populate(\"descriptor\", \"${sp.entityDescriptor.id}\", \"*\", null, null, null)\n	    permission.managed = false\n		permissionService.createPermission(permission, edRole)\n	}\n	\n	// Create SP access control role\n	def role = Role.findWhere(name:\"descriptor-${sp.id}-administrators\")\n	if(!role){	// Expected state\n		role = roleService.createRole(\"descriptor-${sp.id}-administrators\", \"Global administrators for $sp\", false)\n	}\n	\n	def permission = new LevelPermission()\n	permission.populate(\"descriptor\", \"${sp.id}\", \"*\", null, null, null)\n	permission.managed = false\n	permissionService.createPermission(permission, role)\n	\n	def invitation = invitationService.create(null, role.id, null, \"SPSSODescriptor\", \"show\", sp.id.toString())\n	\n	def creator = Contact.get(env.creator.toLong())\n	mailService.sendMail {            \n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.sp.activated.subject\", null, \"fedreg.templates.mail.workflow.sp.activated.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_activated_sp\", model:[serviceProvider:sp, locale:env.locale, invitation:invitation]\n	}\n\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'spssodescriptoractivated\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to process activate in script spssodescriptor_activate. Failed because referenced SP does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','spssodescriptor_activate'),(10,0,1,'2010-12-14 11:50:40','import fedreg.core.*\n\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef sp = SPSSODescriptor.get(env.serviceProvider.toLong())\n\nif(sp) {	\n	def creator = Contact.get(env.creator.toLong())\n	mailService.sendMail {\n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.sp.registered.subject\", null, \"fedreg.templates.mail.workflow.sp.registered.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_registered_sp\", model:[serviceProvider:sp, locale:env.locale]\n	}\n\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'confirmedspssodescriptor\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to email confirmation in script spssodescriptor_confirm. Failed because referenced SP does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','spssodescriptor_confirm'),(11,0,1,'2010-12-14 11:50:40','import fedreg.core.*\n\nentityDescriptorService = ctx.getBean(\"entityDescriptorService\")\nspSSODescriptorService = ctx.getBean(\"SPSSODescriptorService\")\nworkflowTaskService = ctx.getBean(\"workflowTaskService\")\nmailService = ctx.getBean(\"mailService\")\nmessageSource = ctx.getBean(\"messageSource\")\n\ndef sp = SPSSODescriptor.read(env.serviceProvider.toLong())\nif(sp) {\n	\n	def creator = Contact.read(env.creator.toLong())\n	def args = new Object[1]\n	args[0] = sp.displayName\n	mailService.sendMail {            \n		to creator.email.uri\n		from ctx.grailsApplication.config.nimble.messaging.mail.from\n		subject messageSource.getMessage(\"fedreg.templates.mail.workflow.sp.rejected.subject\", args, \"fedreg.templates.mail.workflow.sp.rejected.subject\", new Locale(env.locale))\n		body view:\"/templates/mail/workflows/default/_rejected_sp\", model:[serviceProvider:sp, locale:env.locale]\n	}\n	\n	log.warn \"Deleting $sp. Workflow indicates it is invalid and no longer needed.\"\n	\n	def entityDescriptor = sp.entityDescriptor\n	\n	if(entityDescriptor.holdsSPOnly())\n		entityDescriptorService.delete(entityDescriptor.id)\n	else\n		spSSODescriptorService.delete(sp.id)\n	\n	workflowTaskService.complete(env.taskInstanceID.toLong(), \'spssodescriptordeleted\')\n}\nelse {\n	throw new RuntimeException(\"Attempt to process delete in script spssodescriptor_delete. Failed because referenced SP does not exist\")\n}',NULL,NULL,'2010-12-14 11:50:40','spssodescriptor_delete');
/*!40000 ALTER TABLE `workflow_script` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-12-14 14:22:48
