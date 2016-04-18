package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Ta;
import edu.gatech.projectThree.datamodel.entity.TaOffering;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface TaRepository extends CrudRepository<Ta, Long> {
    ArrayList<Ta> findAll();
    ArrayList<Ta> findAllByOrderByIdAsc();
    ArrayList<Ta> findDistinctByTaOfferingsInOrderByIdAsc(Collection<TaOffering> profOffering);
}
