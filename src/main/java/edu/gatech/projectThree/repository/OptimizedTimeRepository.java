package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.OptimizedTime;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface OptimizedTimeRepository extends CrudRepository<OptimizedTime, Long> {
    OptimizedTime save(OptimizedTime timestamp);
}
