package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.CurrentSemester;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Kolya on 3/31/2016.
 */
public interface CurrentSemesterRepository extends CrudRepository<CurrentSemester, Integer> {
    ArrayList<CurrentSemester> findAll();
    CurrentSemester findTopByOrderBySemesterIdDesc();//find current semester

    //@Query(value = "SELECT * FROM SEMESTER WHERE id = (SELECT semester_id FROM current_semester ORDER BY id DESC LIMIT 1);", nativeQuery = true)
    //@Query("select top from CurrentSemester sem order by sem.id desc")
    //@Query(value = "SELECT TOP FROM CurrentSemester AS cur ORDER BY cur.id DESC")
    //CurrentSemester findCurrSemester();
}
