package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Preference;
import edu.gatech.projectThree.datamodel.entity.Request;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface PreferenceRepository extends CrudRepository<Preference, Long> {
    ArrayList<Preference> findAll();
    ArrayList<Preference> findByOfferingInAndRequestIn(Collection<Offering> offerings, Collection<Request> requests);
    Preference save(Preference preference);
}
