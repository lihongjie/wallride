/*
Navicat MySQL Data Transfer

Source Server         : 192.168.31.23
Source Server Version : 50546
Source Host           : 192.168.31.23:3306
Source Database       : wallride

Target Server Type    : MYSQL
Target Server Version : 50546
File Encoding         : 65001

Date: 2017-10-31 22:30:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `article`
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK2v5gc16vlmfc3b7v9mug9p0nh` FOREIGN KEY (`id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for `BATCH_JOB_EXECUTION`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION`;
CREATE TABLE `BATCH_JOB_EXECUTION` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `EXIT_CODE` varchar(2500) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  `JOB_CONFIGURATION_LOCATION` varchar(2500) DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  KEY `JOB_INST_EXEC_FK` (`JOB_INSTANCE_ID`),
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of BATCH_JOB_EXECUTION
-- ----------------------------

-- ----------------------------
-- Table structure for `BATCH_JOB_EXECUTION_CONTEXT`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_CONTEXT`;
CREATE TABLE `BATCH_JOB_EXECUTION_CONTEXT` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for `BATCH_JOB_EXECUTION_PARAMS`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_PARAMS`;
CREATE TABLE `BATCH_JOB_EXECUTION_PARAMS` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) NOT NULL,
  `KEY_NAME` varchar(100) NOT NULL,
  `STRING_VAL` varchar(250) DEFAULT NULL,
  `DATE_VAL` datetime DEFAULT NULL,
  `LONG_VAL` bigint(20) DEFAULT NULL,
  `DOUBLE_VAL` double DEFAULT NULL,
  `IDENTIFYING` char(1) NOT NULL,
  KEY `JOB_EXEC_PARAMS_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_PARAMS_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of BATCH_JOB_EXECUTION_PARAMS
-- ----------------------------

-- ----------------------------
-- Table structure for `BATCH_JOB_EXECUTION_SEQ`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_SEQ`;
CREATE TABLE `BATCH_JOB_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for `BATCH_JOB_INSTANCE`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_INSTANCE`;
CREATE TABLE `BATCH_JOB_INSTANCE` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `BATCH_JOB_SEQ`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_SEQ`;
CREATE TABLE `BATCH_JOB_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `BATCH_STEP_EXECUTION`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION`;
CREATE TABLE `BATCH_STEP_EXECUTION` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `STEP_NAME` varchar(100) NOT NULL,
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `COMMIT_COUNT` bigint(20) DEFAULT NULL,
  `READ_COUNT` bigint(20) DEFAULT NULL,
  `FILTER_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_COUNT` bigint(20) DEFAULT NULL,
  `READ_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `PROCESS_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `ROLLBACK_COUNT` bigint(20) DEFAULT NULL,
  `EXIT_CODE` varchar(2500) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  KEY `JOB_EXEC_STEP_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of BATCH_STEP_EXECUTION
-- ----------------------------

-- ----------------------------
-- Table structure for `BATCH_STEP_EXECUTION_CONTEXT`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_CONTEXT`;
CREATE TABLE `BATCH_STEP_EXECUTION_CONTEXT` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of BATCH_STEP_EXECUTION_CONTEXT
-- ----------------------------

-- ----------------------------
-- Table structure for `BATCH_STEP_EXECUTION_SEQ`
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_SEQ`;
CREATE TABLE `BATCH_STEP_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of BATCH_STEP_EXECUTION_SEQ
-- ----------------------------

-- ----------------------------
-- Table structure for `blog`
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(200) NOT NULL,
  `default_language` varchar(3) NOT NULL,
  `ga_tracking_id` varchar(100) DEFAULT NULL,
  `ga_profile_id` varchar(100) DEFAULT NULL,
  `ga_custom_dimension_index` int(11) DEFAULT NULL,
  `ga_service_account_id` varchar(300) DEFAULT NULL,
  `ga_service_account_p12_file_name` varchar(300) DEFAULT NULL,
  `ga_service_account_p12_file_content` longblob,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_398ypeix0usuwxip7hl30tl95` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------

-- ----------------------------
-- Table structure for `blog_language`
-- ----------------------------
DROP TABLE IF EXISTS `blog_language`;
CREATE TABLE `blog_language` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `blog_id` bigint(20) NOT NULL,
  `language` varchar(3) NOT NULL,
  `title` longtext NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjvbtdcpruai93kkn9en48os1j` (`blog_id`,`language`),
  CONSTRAINT `FKm26flfhreaktwyf5x7niter6u` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_language
-- ----------------------------

-- ----------------------------
-- Table structure for `category`
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `code` varchar(200) NOT NULL,
  `language` varchar(3) NOT NULL,
  `name` varchar(200) NOT NULL,
  `description` longtext,
  `lft` int(11) NOT NULL,
  `rgt` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKbcyxs660s0fku8sf6pgy137ai` (`code`,`language`),
  KEY `FKpqbj33aij72uwx8rwt086hvq2` (`parent_id`),
  CONSTRAINT `FKpqbj33aij72uwx8rwt086hvq2` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) NOT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `author_name` varchar(200) NOT NULL,
  `date` datetime NOT NULL,
  `content` longtext NOT NULL,
  `approved` bit(1) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg229tmp8ip9shg6ydifpc2mk6` (`author_id`),
  KEY `FKgxbwgh8hcc6k5f2q9vkmjvdps` (`post_id`),
  CONSTRAINT `FKg229tmp8ip9shg6ydifpc2mk6` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKgxbwgh8hcc6k5f2q9vkmjvdps` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for `custom_field`
-- ----------------------------
DROP TABLE IF EXISTS `custom_field`;
CREATE TABLE `custom_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idx` int(11) DEFAULT NULL,
  `code` varchar(200) DEFAULT NULL,
  `language` varchar(3) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `field_type` varchar(50) NOT NULL,
  `default_value` varchar(200) DEFAULT NULL,
  `description` longtext,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKix3po6weuk4wvhvc95n5rk5ch` (`code`,`language`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of custom_field
-- ----------------------------

-- ----------------------------
-- Table structure for `custom_field_option`
-- ----------------------------
DROP TABLE IF EXISTS `custom_field_option`;
CREATE TABLE `custom_field_option` (
  `custom_field_id` bigint(20) NOT NULL,
  `idx` int(11) NOT NULL,
  `language` varchar(3) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`custom_field_id`,`idx`),
  CONSTRAINT `FKjquafa57imfqsl50qxqm29txr` FOREIGN KEY (`custom_field_id`) REFERENCES `custom_field` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of custom_field_option
-- ----------------------------

-- ----------------------------
-- Table structure for `custom_field_value`
-- ----------------------------
DROP TABLE IF EXISTS `custom_field_value`;
CREATE TABLE `custom_field_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custom_field_id` bigint(20) NOT NULL,
  `post_id` bigint(20) NOT NULL,
  `string_value` varchar(300) DEFAULT NULL,
  `text_value` longtext,
  `date_value` date DEFAULT NULL,
  `datetime_value` datetime DEFAULT NULL,
  `number_value` bigint(20) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnn598oul2m13aiorw3e5clc1i` (`post_id`,`custom_field_id`),
  KEY `FK68g6fssy3gjj4jovfso18uysm` (`custom_field_id`),
  CONSTRAINT `FK68g6fssy3gjj4jovfso18uysm` FOREIGN KEY (`custom_field_id`) REFERENCES `custom_field` (`id`),
  CONSTRAINT `FK814q6mnv98jdn8ubh5fkyy3sc` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of custom_field_value
-- ----------------------------

-- ----------------------------
-- Table structure for `ispn_string_table_LuceneIndexesData`
-- ----------------------------
DROP TABLE IF EXISTS `ispn_string_table_LuceneIndexesData`;
CREATE TABLE `ispn_string_table_LuceneIndexesData` (
  `id_column` varchar(255) NOT NULL,
  `data_column` longblob,
  `timestamp_column` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_column`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ispn_string_table_LuceneIndexesData
-- ----------------------------

-- ----------------------------
-- Table structure for `ispn_string_table_LuceneIndexesLocking`
-- ----------------------------
DROP TABLE IF EXISTS `ispn_string_table_LuceneIndexesLocking`;
CREATE TABLE `ispn_string_table_LuceneIndexesLocking` (
  `id_column` varchar(255) NOT NULL,
  `data_column` longblob,
  `timestamp_column` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_column`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ispn_string_table_LuceneIndexesLocking
-- ----------------------------

-- ----------------------------
-- Table structure for `ispn_string_table_LuceneIndexesMetadata`
-- ----------------------------
DROP TABLE IF EXISTS `ispn_string_table_LuceneIndexesMetadata`;
CREATE TABLE `ispn_string_table_LuceneIndexesMetadata` (
  `id_column` varchar(255) NOT NULL,
  `data_column` longblob,
  `timestamp_column` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_column`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ispn_string_table_LuceneIndexesMetadata
-- ----------------------------

-- ----------------------------
-- Table structure for `media`
-- ----------------------------
DROP TABLE IF EXISTS `media`;
CREATE TABLE `media` (
  `id` varchar(50) NOT NULL,
  `mime_type` varchar(500) NOT NULL,
  `original_name` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of media
-- ----------------------------

-- ----------------------------
-- Table structure for `navigation_item`
-- ----------------------------
DROP TABLE IF EXISTS `navigation_item`;
CREATE TABLE `navigation_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `page_id` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `language` varchar(255) NOT NULL,
  `type` varchar(31) NOT NULL,
  `sort` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo9pj7oh5oc36ia8f9flji199u` (`parent_id`),
  KEY `FK72p6vy4stfruklu8mggg6qt3s` (`category_id`),
  KEY `FKq2bloyhyl745v0ao2kjfjieyf` (`page_id`),
  CONSTRAINT `FK72p6vy4stfruklu8mggg6qt3s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKo9pj7oh5oc36ia8f9flji199u` FOREIGN KEY (`parent_id`) REFERENCES `navigation_item` (`id`),
  CONSTRAINT `FKq2bloyhyl745v0ao2kjfjieyf` FOREIGN KEY (`page_id`) REFERENCES `page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of navigation_item
-- ----------------------------

-- ----------------------------
-- Table structure for `page`
-- ----------------------------
DROP TABLE IF EXISTS `page`;
CREATE TABLE `page` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `lft` int(11) NOT NULL,
  `rgt` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK483vwi7bfr4pl0bf57g4abei7` (`parent_id`),
  CONSTRAINT `FK483vwi7bfr4pl0bf57g4abei7` FOREIGN KEY (`parent_id`) REFERENCES `page` (`id`),
  CONSTRAINT `FK71xxvk6cocuigt994gx2yyohk` FOREIGN KEY (`id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of page
-- ----------------------------

-- ----------------------------
-- Table structure for `password_reset_token`
-- ----------------------------
DROP TABLE IF EXISTS `password_reset_token`;
CREATE TABLE `password_reset_token` (
  `token` varchar(50) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `email` varchar(200) NOT NULL,
  `expired_at` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`token`),
  KEY `FKjthxr8d7rmlunj1uv3lt1xvl5` (`user_id`),
  CONSTRAINT `FKjthxr8d7rmlunj1uv3lt1xvl5` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of password_reset_token
-- ----------------------------

-- ----------------------------
-- Table structure for `persistent_logins`
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------

-- ----------------------------
-- Table structure for `popular_post`
-- ----------------------------
DROP TABLE IF EXISTS `popular_post`;
CREATE TABLE `popular_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) NOT NULL,
  `language` varchar(3) NOT NULL,
  `type` varchar(50) NOT NULL,
  `rank` int(11) NOT NULL,
  `views` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKevl12yr4xxkydmkvigjq82iui` (`language`,`type`,`rank`),
  KEY `FKkk18uxlago62ssjyxk9p3wn4r` (`post_id`),
  CONSTRAINT `FKkk18uxlago62ssjyxk9p3wn4r` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of popular_post
-- ----------------------------

-- ----------------------------
-- Table structure for `post`
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(200) DEFAULT NULL,
  `language` varchar(3) NOT NULL,
  `status` varchar(50) NOT NULL,
  `date` datetime DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `body` longtext,
  `cover_id` varchar(50) DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `drafted_id` bigint(20) DEFAULT NULL,
  `drafted_code` varchar(200) DEFAULT NULL,
  `seo_title` varchar(500) DEFAULT NULL,
  `seo_description` longtext,
  `seo_keywords` longtext,
  `views` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl52i0qo9maim4jb28sahyaf02` (`code`,`language`),
  KEY `FKlv86dv65vxnbyndhwdp9evbn5` (`author_id`),
  KEY `FKnx17yhqhh2l6dphgxr04fno6p` (`cover_id`),
  KEY `FKmnd7c5s0tpi8fsbtrcv3v1w75` (`drafted_id`),
  CONSTRAINT `FKlv86dv65vxnbyndhwdp9evbn5` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKmnd7c5s0tpi8fsbtrcv3v1w75` FOREIGN KEY (`drafted_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKnx17yhqhh2l6dphgxr04fno6p` FOREIGN KEY (`cover_id`) REFERENCES `media` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post
-- ----------------------------

-- ----------------------------
-- Table structure for `post_category`
-- ----------------------------
DROP TABLE IF EXISTS `post_category`;
CREATE TABLE `post_category` (
  `post_id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`post_id`,`category_id`),
  KEY `FKq63x31lf6aykdrgi3llnc171y` (`category_id`),
  CONSTRAINT `FKciko9vgftyon175wslea5d88k` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKq63x31lf6aykdrgi3llnc171y` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post_category
-- ----------------------------

-- ----------------------------
-- Table structure for `post_media`
-- ----------------------------
DROP TABLE IF EXISTS `post_media`;
CREATE TABLE `post_media` (
  `post_id` bigint(20) NOT NULL,
  `media_id` varchar(50) NOT NULL,
  `index` int(11) NOT NULL,
  PRIMARY KEY (`post_id`,`index`),
  KEY `FKbt9h1jh7mqdrodqmy8potin0s` (`media_id`),
  CONSTRAINT `FK7dbnkkaarh7suxjlkwn5sh4a7` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKbt9h1jh7mqdrodqmy8potin0s` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post_media
-- ----------------------------

-- ----------------------------
-- Table structure for `post_related_post`
-- ----------------------------
DROP TABLE IF EXISTS `post_related_post`;
CREATE TABLE `post_related_post` (
  `post_id` bigint(20) NOT NULL,
  `related_id` bigint(20) NOT NULL,
  PRIMARY KEY (`related_id`,`post_id`),
  KEY `FKthyi9hidjpq5vmcamwaj2ap2` (`post_id`),
  CONSTRAINT `FK8h4kulpvd11c5l4bdn3wfbtie` FOREIGN KEY (`related_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKthyi9hidjpq5vmcamwaj2ap2` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post_related_post
-- ----------------------------

-- ----------------------------
-- Table structure for `post_tag`
-- ----------------------------
DROP TABLE IF EXISTS `post_tag`;
CREATE TABLE `post_tag` (
  `post_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `FK8d78naxn3frlhbqyiurgbtg3v` (`tag_id`),
  CONSTRAINT `FK8d78naxn3frlhbqyiurgbtg3v` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),
  CONSTRAINT `FKonr178imgjksqflate1o6ybim` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post_tag
-- ----------------------------

-- ----------------------------
-- Table structure for `tag`
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `language` varchar(3) NOT NULL,
  `name` varchar(200) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk25qstev2lpae13bk95lxny1y` (`name`,`language`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_id` varchar(100) NOT NULL,
  `login_password` varchar(500) NOT NULL,
  `name_first` varchar(50) NOT NULL,
  `name_last` varchar(50) NOT NULL,
  `nickname` varchar(500) DEFAULT NULL,
  `email` varchar(200) NOT NULL,
  `description` longtext,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK_6ntlp6n5ltjg6hhxl66jj5u0l` (`login_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for `user_invitation`
-- ----------------------------
DROP TABLE IF EXISTS `user_invitation`;
CREATE TABLE `user_invitation` (
  `token` varchar(50) NOT NULL,
  `email` varchar(500) NOT NULL,
  `message` longtext,
  `expired_at` datetime NOT NULL,
  `accepted` bit(1) NOT NULL,
  `accepted_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_invitation
-- ----------------------------

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role`),
  CONSTRAINT `FKhjx9nk20h4mo745tdqj8t8n9d` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
