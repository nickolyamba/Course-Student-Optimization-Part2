#http://www.mysqltutorial.org/import-csv-file-mysql-table/

# SEMESTER
ALTER TABLE semester AUTO_INCREMENT = 1

LOAD DATA INFILE 'c:/semesters.csv'
INTO TABLE semester
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(id, season, year, @start_date, @end_date)
SET start_date = STR_TO_DATE(@start_date,'%m/%d/%Y'), 
	end_date = STR_TO_DATE(@end_date, '%m/%d/%Y');


# Course
LOAD DATA INFILE 'c:/courses.csv'
INTO TABLE course
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(id, course_name, course_num, @fall_term, @spring_term, @summer_term)
fall_term=cast(@fall_term as signed),
spring_term=cast(@spring_term as signed),
summer_term=cast(@summer_term as signed)
show warnings;

#pre_course
INSERT INTO pre_course VALUES(4,16), (2,1), (9,13), (3,7)

CREATE PROCEDURE insert_range(IN start INT, IN end INT)
BEGIN
  DECLARE i INT DEFAULT start;
  WHILE i <= end DO
    INSERT tablename (id) VALUES (i);
    SET i = i + 1;
  END WHILE;
END

DROP PROCEDURE `insert_range`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_range`(IN `start` INT, IN `end` INT, IN `table_name` VARCHAR(25)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN
  DECLARE i INT DEFAULT start;
  WHILE i <= end DO
    INSERT table_name (id) VALUES (i);
    SET i = i + 1;
  END WHILE;
END