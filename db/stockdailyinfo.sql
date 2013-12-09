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
/*Table structure for table `stockdailyinfo` */

DROP TABLE IF EXISTS `stockdailyinfo`;

CREATE TABLE `stockdailyinfo` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `openprice` double DEFAULT NULL,
  `closeprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `stockdailyinfo` */

insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-11-27 22:52:32',11.93,11.95,15,11.74,'2013-11-27 22:37:05','2013-11-27 22:37:09');
insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-11-28 22:35:38',11.88,12.74,13.05,11.8,'2013-11-28 23:04:27','2013-11-28 23:04:33');
insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-11-29 22:32:28',12.61,13.13,12.6,13.2,'2013-11-29 23:03:48','2013-11-29 23:03:54');
insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-12-02 22:31:37',12.5,13.1,14.4,12.36,'2013-12-09 22:31:42','2013-12-09 22:31:44');
insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-12-03 23:00:13',12.66,14.41,14.41,12.66,'2013-12-03 22:25:31','2013-12-03 22:25:34');
insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-12-04 22:31:49',14.78,15.85,15.85,14.6,'2013-12-04 22:58:40','2013-12-04 22:31:47');
insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-12-05 22:31:55',16.5,16.83,17.44,16.38,'2013-12-05 22:31:52','2013-12-05 22:31:49');
insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-12-06 22:55:23',16.97,15.42,17.18,15.19,'2013-12-06 22:25:47','2013-12-06 22:25:50');
insert  into `stockdailyinfo`(`stockid`,`date`,`openprice`,`closeprice`,`highestprice`,`lowestprice`,`createdtime`,`lastmodifiedtime`) values ('002199','2013-12-09 22:56:23',15.2,15.88,16.47,15.2,'2013-12-09 22:56:13','2013-12-09 22:56:19');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
