package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.TaOffering;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface TaOfferingRepository extends CrudRepository<TaOffering, Long> {
    ArrayList<TaOffering> findAll();
}
