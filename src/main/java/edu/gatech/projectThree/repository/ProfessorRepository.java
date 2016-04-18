package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.ProfessorOffering;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface ProfessorRepository extends CrudRepository<Professor, Long> {
    ArrayList<Professor> findAll();
    ArrayList<Professor> findAllByOrderByIdAsc();
    ArrayList<Professor> findDistinctByProfOfferingsInOrderByIdAsc(Collection<ProfessorOffering> profOffering);
}
