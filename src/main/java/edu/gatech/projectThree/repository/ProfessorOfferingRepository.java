package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.ProfessorOffering;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface ProfessorOfferingRepository extends CrudRepository<ProfessorOffering, Long> {
    ArrayList<ProfessorOffering> findAll();
    ArrayList<ProfessorOffering> findByOfferingIn(Collection<Offering> offerings);
    @Query(value = "SELECT * FROM PROFESSOR_OFFERING PO WHERE PO.profrequest_id =\n" +
                    "(SELECT RQ.id FROM prof_request RQ ORDER BY RQ.created DESC LIMIT 1) \n" +
                    "AND PO.offering_id in ?1", nativeQuery = true)
    ArrayList<ProfessorOffering> findLastProfOfferings(Collection<Long> offeringIDs);
}
