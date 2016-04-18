package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Request;

import edu.gatech.projectThree.datamodel.entity.Student;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface RequestRepository extends CrudRepository<Request, Long> {
    ArrayList<Request> findAll();

    ArrayList<Request> findByStudent(Student student);
    @Query(value = "SELECT RQ.id, RQ.created, RQ.student_id\n" +
            "FROM REQUEST RQ\n" +
            "INNER JOIN \n" +
            "(\n" +
            "    SELECT max(created) as MaxDate, student_id\n" +
            "    FROM request\n" +
            "    GROUP BY student_id\n" +
            ") tb on RQ.created = tb.MaxDate", nativeQuery = true)
    ArrayList<Request> findLastReuestsByStudent();
}
