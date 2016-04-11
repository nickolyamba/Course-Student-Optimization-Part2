package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by nick on 3/18/16.
 */
@Entity
@DiscriminatorValue("4")
@Table(name="Administrator")
public class Administrator extends User {

    public Administrator(){}

    public Administrator(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + super.getId() +
                ", first_name='" + super.getFirst_name() + '\'' +
                ", last_name='" + super.getLast_name() + '\'' +
                ", user_name='" + super.getUser_name() + '\'' +
                '}';
    }
}
