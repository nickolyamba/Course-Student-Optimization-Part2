package edu.gatech.projectThree.datamodel.dao.impl;

import edu.gatech.projectThree.datamodel.dao.DataAccessObject;
import edu.gatech.projectThree.datamodel.entity.Offering;

import org.springframework.stereotype.Component;

/**
 * Created by dawu on 3/18/16.
 */
@Component("offeringDAO")
public class OfferingDAO implements DataAccessObject<Offering> {

    @Override
    public void insert(Offering obj) {
    	System.out.println("OfferingDAO insert method...\n");
    }

    @Override
    public Offering update(Offering obj) {
        return null;
    }

    @Override
    public void delete(Offering obj) {

    }

    @Override
    public Offering get(long index) {
        return null;
    }

    @Override
    public Offering[] getAll() {
        return new Offering[0];
    }
}
