package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Preference;
import edu.gatech.projectThree.datamodel.entity.Request;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface PreferenceRepository extends CrudRepository<Preference, Long> {
    ArrayList<Preference> findAll();
    ArrayList<Preference> findByOfferingInAndRequestInOrderByIdAsc(Collection<Offering> offerings, Collection<Request> requests);
    ArrayList<Preference> findByOfferingInAndRequestIn(Collection<Offering> offerings, Collection<Request> requests);

    @Query(value = "SELECT * FROM PREFERENCE WHERE optimized_id = \n" +
                    "(SELECT OPT.id FROM optimized_time OPT ORDER BY OPT.TIMESTAMP DESC LIMIT 1)\n" +
                    "AND is_assigned = true",
            nativeQuery = true)
    ArrayList<Preference> findLastOptimizedPreferences();
}
