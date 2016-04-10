-- phpMyAdmin SQL Dump
-- version 4.2.7
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Apr 10, 2016 at 12:06 AM
-- Server version: 5.5.41-log
-- PHP Version: 7.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `projectdb`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_range`(IN `start` INT, IN `end` INT)
BEGIN
  DECLARE i INT DEFAULT start;
  WHILE i <= end DO
    INSERT ta (id) VALUES (i);
    SET i = i + 1;
  END WHILE;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE IF NOT EXISTS `course` (
`id` int(11) NOT NULL,
  `course_name` varchar(255) NOT NULL,
  `course_num` varchar(255) NOT NULL,
  `fall_term` bit(1) NOT NULL,
  `spring_term` bit(1) NOT NULL,
  `summer_term` bit(1) NOT NULL,
  `type` int(11) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`id`, `course_name`, `course_num`, `fall_term`, `spring_term`, `summer_term`, `type`) VALUES
(1, 'Advanced Operating Systems', '6210', b'1', b'0', b'0', NULL),
(2, 'Computer Networks', '6250', b'1', b'1', b'1', NULL),
(3, 'Software Development Process', '6300', b'1', b'1', b'1', NULL),
(4, 'Machine Learning', '7641', b'1', b'1', b'1', NULL),
(5, 'High Performance Computer Architecture', '6290', b'0', b'1', b'0', NULL),
(6, 'Software Architecture and Design', '6310', b'1', b'1', b'1', NULL),
(7, 'Intro to Health Informatics', '6440', b'1', b'0', b'0', NULL),
(8, 'Computability, Complexity and Algorithms', '6505', b'1', b'1', b'1', NULL),
(9, 'Knowledge-Based Artificial Intelligence, Cognitive Systems', '7637', b'1', b'1', b'1', NULL),
(10, 'Computer Vision', '4495', b'0', b'1', b'0', NULL),
(11, 'Computational Photography', '6475', b'1', b'0', b'0', NULL),
(12, 'Introduction to Operating Systems', '8803-002', b'1', b'1', b'1', NULL),
(13, 'Artificial Intelligence for Robotics', '8803-001', b'1', b'1', b'1', NULL),
(14, 'Introduction to Information Security', '6035', b'0', b'1', b'0', NULL),
(15, 'High-Performance Computing', '6220', b'1', b'0', b'0', NULL),
(16, 'Machine Learning for Trading', '7646', b'0', b'1', b'0', NULL),
(17, 'Special Topics: Reinforcement Learning', '8803', b'1', b'0', b'0', NULL),
(18, 'Special Topics: Big Data', '8803', b'0', b'1', b'0', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `demand`
--

CREATE TABLE IF NOT EXISTS `demand` (
`id` bigint(20) NOT NULL,
  `demand` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  `offering_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `offering`
--

CREATE TABLE IF NOT EXISTS `offering` (
`id` bigint(20) NOT NULL,
  `course_id` int(11) NOT NULL,
  `semester_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `preference`
--

CREATE TABLE IF NOT EXISTS `preference` (
`id` bigint(20) NOT NULL,
  `priority` int(11) NOT NULL,
  `recommend` varchar(255) DEFAULT NULL,
  `offering_id` bigint(20) NOT NULL,
  `request_id` int(11) NOT NULL,
  `student_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `pre_course`
--

CREATE TABLE IF NOT EXISTS `pre_course` (
  `course` int(11) NOT NULL,
  `prereq` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pre_course`
--

INSERT INTO `pre_course` (`course`, `prereq`) VALUES
(2, 1),
(3, 7),
(9, 13),
(4, 16);

-- --------------------------------------------------------

--
-- Table structure for table `professor`
--

CREATE TABLE IF NOT EXISTS `professor` (
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `professor`
--

INSERT INTO `professor` (`id`) VALUES
(4),
(5),
(6),
(7),
(8),
(9);

-- --------------------------------------------------------

--
-- Table structure for table `professor_offering`
--

CREATE TABLE IF NOT EXISTS `professor_offering` (
`id` bigint(20) NOT NULL,
  `offering_id` bigint(20) NOT NULL,
  `prof_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `request`
--

CREATE TABLE IF NOT EXISTS `request` (
`id` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  `student_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `semester`
--

CREATE TABLE IF NOT EXISTS `semester` (
`id` int(11) NOT NULL,
  `end_date` date DEFAULT NULL,
  `season` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `year` varchar(255) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `semester`
--

INSERT INTO `semester` (`id`, `end_date`, `season`, `start_date`, `year`) VALUES
(1, '2015-12-01', 1, '2015-08-01', '2015'),
(2, '2016-06-01', 2, '2016-02-01', '2016'),
(3, '2016-08-01', 3, '2016-06-01', '2016'),
(4, '2016-12-01', 1, '2016-08-01', '2016'),
(5, '2017-06-01', 2, '2017-02-01', '2017'),
(6, '2017-08-01', 3, '2017-06-01', '2017'),
(7, '2017-12-01', 1, '2017-08-01', '2017'),
(8, '2018-06-01', 2, '2018-02-01', '2018'),
(9, '2018-08-01', 3, '2018-06-01', '2018'),
(10, '2018-12-01', 1, '2018-08-01', '2018'),
(11, '2019-06-01', 2, '2019-02-01', '2019'),
(12, '2019-08-01', 3, '2019-06-01', '2019');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE IF NOT EXISTS `student` (
  `id` bigint(20) NOT NULL,
  `seniority` int(11) DEFAULT NULL,
  `gpa` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`id`, `seniority`, `gpa`) VALUES
(1, NULL, NULL),
(2, NULL, NULL),
(3, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `stud_cour_taken`
--

CREATE TABLE IF NOT EXISTS `stud_cour_taken` (
  `student_id` bigint(20) NOT NULL,
  `course_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ta`
--

CREATE TABLE IF NOT EXISTS `ta` (
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ta`
--

INSERT INTO `ta` (`id`) VALUES
(12),
(13),
(14),
(15),
(16),
(17),
(18),
(19),
(20),
(21),
(22),
(23);

-- --------------------------------------------------------

--
-- Table structure for table `ta_offering`
--

CREATE TABLE IF NOT EXISTS `ta_offering` (
`id` bigint(20) NOT NULL,
  `offering_id` bigint(20) NOT NULL,
  `ta_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`id` bigint(20) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_type` varchar(31) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=24 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `first_name`, `last_name`, `user_name`, `password`, `user_type`) VALUES
(1, 'AUDREY', 'BERGER', 'ABERGE1', '123', '1'),
(2, 'ROBERT', 'ORTIZ', 'RORTIZ1', '123', '1'),
(3, 'CLARENCE', 'MILLS', 'CMILLS1', '123', '1'),
(4, 'ANDREW', 'BAILEY', 'ABAILE1', '123', '3'),
(5, 'DENISE', 'WILSON', 'DWILSO1', '123', '3'),
(6, 'LAURA', 'GREEN', 'LGREEN1', '123', '3'),
(7, 'PAMELA', 'BOOTH', 'PBOOTH1', '123', '3'),
(8, 'JUDITH', 'HEBERT', 'JHEBER1', '123', '3'),
(9, 'Robert', 'Payne', 'Youstaired', '123', '3'),
(10, 'Helen', 'Arnold', 'Shavan', '123', '4'),
(11, 'Virginia', 'Webb', 'Aniced', '123', '4'),
(12, 'Edward', 'Snook', 'Hilooppat', '123', '2'),
(13, 'Edward', 'Demello', 'Ancomettiody', '123', '2'),
(14, 'Forrest', 'Lopez', 'Castand', '123', '2'),
(15, 'Steve', 'Floyd', 'Alask1980', '123', '2'),
(16, 'Mary', 'Basquez', 'Fectionce', '123', '2'),
(17, 'Andrew', 'Skaggs', 'Dord1973', '123', '2'),
(18, 'Timothy', 'Moore', 'Waallovar', '123', '2'),
(19, 'Jeffery', 'Dodson', 'Makess', '123', '2'),
(20, 'Pam', 'Knowles', 'Roing1964', '123', '2'),
(21, 'James', 'Curtis', 'Vaid1950', '123', '2'),
(22, 'Becky', 'Rubino', 'Tride1994', '123', '2'),
(23, 'Ralph', 'Pearson', 'Thettles', '123', '2');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `course`
--
ALTER TABLE `course`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `demand`
--
ALTER TABLE `demand`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_qaeq98v5dx0c5aqx3e9u4ul3w` (`offering_id`);

--
-- Indexes for table `offering`
--
ALTER TABLE `offering`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_ok9s69mlqxinf8li6eg4dr8ag` (`course_id`), ADD KEY `FK_31yxdqbyn00r34he5aq5lmogf` (`semester_id`);

--
-- Indexes for table `preference`
--
ALTER TABLE `preference`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_pv0ro6y374qoy634xq464v2m8` (`offering_id`), ADD KEY `FK_b5bh78giy66tlnre47twugb86` (`request_id`), ADD KEY `FK_a6wg3920410naau30ioyk43d8` (`student_id`);

--
-- Indexes for table `pre_course`
--
ALTER TABLE `pre_course`
 ADD PRIMARY KEY (`course`,`prereq`), ADD KEY `FK_9jk1uhfx3g7njr0v0hqf9yltb` (`prereq`);

--
-- Indexes for table `professor`
--
ALTER TABLE `professor`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `professor_offering`
--
ALTER TABLE `professor_offering`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_fclmm8gdj7flmrmuscqivates` (`offering_id`), ADD KEY `FK_e7w5t86fmnwkl1cvmyjsso9eb` (`prof_id`);

--
-- Indexes for table `request`
--
ALTER TABLE `request`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_7c7xi6ev88bqtvds23arc3mkb` (`student_id`);

--
-- Indexes for table `semester`
--
ALTER TABLE `semester`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stud_cour_taken`
--
ALTER TABLE `stud_cour_taken`
 ADD PRIMARY KEY (`student_id`,`course_id`), ADD KEY `FK_ssbctl5hljvfr2k1mn30n3qvp` (`course_id`);

--
-- Indexes for table `ta`
--
ALTER TABLE `ta`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ta_offering`
--
ALTER TABLE `ta_offering`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_6acxwx2jbmnelacdfocacioq` (`offering_id`), ADD KEY `FK_19ii2vfwg83r5rsyfjxbqrbqw` (`ta_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `course`
--
ALTER TABLE `course`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT for table `demand`
--
ALTER TABLE `demand`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `offering`
--
ALTER TABLE `offering`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `preference`
--
ALTER TABLE `preference`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `professor_offering`
--
ALTER TABLE `professor_offering`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `request`
--
ALTER TABLE `request`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `semester`
--
ALTER TABLE `semester`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT for table `ta_offering`
--
ALTER TABLE `ta_offering`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=24;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `demand`
--
ALTER TABLE `demand`
ADD CONSTRAINT `FK_qaeq98v5dx0c5aqx3e9u4ul3w` FOREIGN KEY (`offering_id`) REFERENCES `offering` (`id`);

--
-- Constraints for table `offering`
--
ALTER TABLE `offering`
ADD CONSTRAINT `FK_31yxdqbyn00r34he5aq5lmogf` FOREIGN KEY (`semester_id`) REFERENCES `semester` (`id`),
ADD CONSTRAINT `FK_ok9s69mlqxinf8li6eg4dr8ag` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`);

--
-- Constraints for table `preference`
--
ALTER TABLE `preference`
ADD CONSTRAINT `FK_a6wg3920410naau30ioyk43d8` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
ADD CONSTRAINT `FK_b5bh78giy66tlnre47twugb86` FOREIGN KEY (`request_id`) REFERENCES `request` (`id`),
ADD CONSTRAINT `FK_pv0ro6y374qoy634xq464v2m8` FOREIGN KEY (`offering_id`) REFERENCES `offering` (`id`);

--
-- Constraints for table `pre_course`
--
ALTER TABLE `pre_course`
ADD CONSTRAINT `FK_16djov16erac2yh55rtlws705` FOREIGN KEY (`course`) REFERENCES `course` (`id`),
ADD CONSTRAINT `FK_9jk1uhfx3g7njr0v0hqf9yltb` FOREIGN KEY (`prereq`) REFERENCES `course` (`id`);

--
-- Constraints for table `professor`
--
ALTER TABLE `professor`
ADD CONSTRAINT `FK_mw7fqu259ujbc08jyrlecjrm4` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- Constraints for table `professor_offering`
--
ALTER TABLE `professor_offering`
ADD CONSTRAINT `FK_e7w5t86fmnwkl1cvmyjsso9eb` FOREIGN KEY (`prof_id`) REFERENCES `professor` (`id`),
ADD CONSTRAINT `FK_fclmm8gdj7flmrmuscqivates` FOREIGN KEY (`offering_id`) REFERENCES `offering` (`id`);

--
-- Constraints for table `request`
--
ALTER TABLE `request`
ADD CONSTRAINT `FK_7c7xi6ev88bqtvds23arc3mkb` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`);

--
-- Constraints for table `student`
--
ALTER TABLE `student`
ADD CONSTRAINT `FK_m4oyvjystgi94h8yo4v8oijrr` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- Constraints for table `stud_cour_taken`
--
ALTER TABLE `stud_cour_taken`
ADD CONSTRAINT `FK_k9h37xk75g950v0wu6hwmfl38` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
ADD CONSTRAINT `FK_ssbctl5hljvfr2k1mn30n3qvp` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`);

--
-- Constraints for table `ta`
--
ALTER TABLE `ta`
ADD CONSTRAINT `FK_17v02tgqleaku2aic44s1dq5b` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- Constraints for table `ta_offering`
--
ALTER TABLE `ta_offering`
ADD CONSTRAINT `FK_19ii2vfwg83r5rsyfjxbqrbqw` FOREIGN KEY (`ta_id`) REFERENCES `ta` (`id`),
ADD CONSTRAINT `FK_6acxwx2jbmnelacdfocacioq` FOREIGN KEY (`offering_id`) REFERENCES `offering` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
