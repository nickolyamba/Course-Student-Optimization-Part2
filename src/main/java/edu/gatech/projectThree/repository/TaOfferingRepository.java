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

    @Query(value = "SELECT * FROM ta_offering TAOFF WHERE TAOFF.tarequest_id =\n" +
                    "(SELECT RQ.id FROM ta_request RQ ORDER BY RQ.created DESC LIMIT 1)\n" +
                    "AND TAOFF.offering_id in ?1",
                    nativeQuery = true)
    ArrayList<TaOffering> findLastTaOfferings(Collection<Long> offeringIDs);
}
