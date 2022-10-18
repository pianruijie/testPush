-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `c_id`         int(11) DEFAULT NULL,
    `c_name`       varchar(255) DEFAULT NULL,
    `credit_point` int(11) DEFAULT NULL,
    `deletedAt`    timestamp(6) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for courses_teachers_junction
-- ----------------------------
DROP TABLE IF EXISTS `courses_teachers_junction`;
CREATE TABLE `courses_teachers_junction`
(
    `courses_fk`  int(11) NOT NULL,
    `teachers_fk` int(11) NOT NULL,
    PRIMARY KEY (`courses_fk`, `teachers_fk`),
    KEY           `IDX_bd79e6123743819c0c11c1728d` (`courses_fk`),
    KEY           `IDX_ca538cf531dc38ec5e62a710f5` (`teachers_fk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for scores
-- ----------------------------
DROP TABLE IF EXISTS `scores`;
CREATE TABLE `scores`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `s_id`        int(11) DEFAULT NULL,
    `c_id`        int(11) DEFAULT NULL,
    `deletedAt`   timestamp(6) NULL DEFAULT NULL,
    `score`       decimal(2, 0) DEFAULT NULL,
    `courses_fk`  int(11) NOT NULL,
    `students_fk` int(11) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT,
    `deletedAt`     timestamp(6) NULL DEFAULT NULL,
    `s_id`          int(11) DEFAULT NULL,
    `s_name`        varchar(255) DEFAULT NULL,
    `s_class`       varchar(255) DEFAULT NULL,
    `s_gender`      varchar(255) DEFAULT NULL,
    `s_major`       varchar(255) DEFAULT NULL,
    `s_birthday`    timestamp NULL DEFAULT NULL,
    `credit_points` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `t_id`      int(11) DEFAULT NULL,
    `t_name`    varchar(255) DEFAULT NULL,
    `deletedAt` timestamp(6) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;