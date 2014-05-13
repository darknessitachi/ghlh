/*
SQLyog Community v11.31 (64 bit)
MySQL - 5.6.16 : Database - ghlh
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
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

/*Table structure for table `stockdailyinfo` */

DROP TABLE IF EXISTS `stockdailyinfo`;

CREATE TABLE `stockdailyinfo` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  FULLTEXT KEY `StockIdIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stockdailyinfo10` */

DROP TABLE IF EXISTS `stockdailyinfo10`;

CREATE TABLE `stockdailyinfo10` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  FULLTEXT KEY `StockIdIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stockdailyinfo11` */

DROP TABLE IF EXISTS `stockdailyinfo11`;

CREATE TABLE `stockdailyinfo11` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  FULLTEXT KEY `StockIdIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stockdailyinfo13` */

DROP TABLE IF EXISTS `stockdailyinfo13`;

CREATE TABLE `stockdailyinfo13` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  FULLTEXT KEY `StockIdIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stockdailyinfo14` */

DROP TABLE IF EXISTS `stockdailyinfo14`;

CREATE TABLE `stockdailyinfo14` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  FULLTEXT KEY `StockIdIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stockdailyinfo915` */

DROP TABLE IF EXISTS `stockdailyinfo915`;

CREATE TABLE `stockdailyinfo915` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  FULLTEXT KEY `StockIdIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stockdailyinfo920` */

DROP TABLE IF EXISTS `stockdailyinfo920`;

CREATE TABLE `stockdailyinfo920` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  FULLTEXT KEY `StockIdIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stockdailyinfo925` */

DROP TABLE IF EXISTS `stockdailyinfo925`;

CREATE TABLE `stockdailyinfo925` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  FULLTEXT KEY `StockIdIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stockreport` */

DROP TABLE IF EXISTS `stockreport`;

CREATE TABLE `stockreport` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `type` int(11) NOT NULL,
  `mairu` int(11) DEFAULT NULL,
  `zengchi` int(11) DEFAULT NULL,
  `zhongxin` int(11) DEFAULT NULL,
  `jianchi` int(11) DEFAULT NULL,
  `maichu` int(11) DEFAULT NULL,
  `createdtime` timestamp NULL DEFAULT NULL,
  `lastmodifiedtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`stockid`,`date`,`type`)
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
  `winSellPrice` double DEFAULT NULL,
  `lostSellPrice` double DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1-possible sell, 2-pending buy, 3- holding, 4- finish',
  `previoustradeid` int(11) DEFAULT NULL,
  `comments` varchar(128) DEFAULT NULL,
  `createdTimestamp` timestamp NULL DEFAULT NULL,
  `lastModifiedTimestamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
