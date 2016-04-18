package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.ProfessorOffering;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface ProfessorOfferingRepository extends CrudRepository<ProfessorOffering, Long> {
    ArrayList<ProfessorOffering> findAll();
    ArrayList<ProfessorOffering> findByOfferingIn(Collection<Offering> offerings);
}
