package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Config;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface ConfigRepository extends CrudRepository<Config, Integer> {
    ArrayList<Config> findAll();
    Config findTopByOrderByIdDesc();
}
