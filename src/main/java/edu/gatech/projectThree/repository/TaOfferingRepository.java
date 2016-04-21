package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.TaOffering;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface TaOfferingRepository extends CrudRepository<TaOffering, Long> {
    ArrayList<TaOffering> findAll();
    ArrayList<TaOffering> findByOfferingIn(Collection<Offering> offerings);

    @Query(value = "SELECT * FROM ta_offering WHERE \n" +
                    "(optimized_id = (SELECT OPTIMIZED_ID FROM TA_OFFERING ORDER BY id DESC LIMIT 1)\n" +
                    "OR \n" +
                    "optimized_id IS (SELECT OPTIMIZED_ID FROM TA_OFFERING ORDER BY id DESC LIMIT 1))\n" +
                    "AND offering_id IN ?0", nativeQuery = true)
    ArrayList<TaOffering> findLastTaOfferings(Collection<Offering> offerings);
}
