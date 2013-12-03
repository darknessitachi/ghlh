/*
SQLyog Community v11.28 (64 bit)
MySQL - 5.6.14 : Database - ghlh
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `monitorstock` */

DROP TABLE IF EXISTS `monitorstock`;

CREATE TABLE `monitorstock` (
  `id` int(11) NOT NULL,
  `stockId` char(6) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `tradeAlgorithm` varchar(32) DEFAULT NULL,
  `additionInfo` varchar(128) DEFAULT NULL,
  `onMonitoring` char(5) DEFAULT NULL,
  `createdTimestamp` timestamp NULL DEFAULT NULL,
  `lastModifiedTimestamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stocktrade` */

DROP TABLE IF EXISTS `stocktrade`;

CREATE TABLE `stocktrade` (
  `id` int(11) DEFAULT NULL,
  `stockId` char(6) DEFAULT NULL,
  `tradeAlgorithm` varchar(32) DEFAULT NULL,
  `buyDate` timestamp NULL DEFAULT NULL,
  `buyBasePrice` double DEFAULT NULL,
  `buyPrice` double DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `sellDate` timestamp NULL DEFAULT NULL,
  `sellPrice` double DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1-possible sell, 2-pending buy, 3- holding, 4- finish',
  `comments` varchar(128) DEFAULT NULL,
  `createdTimestamp` timestamp NULL DEFAULT NULL,
  `lastModifiedTimestamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
