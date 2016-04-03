package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
@DiscriminatorValue("TA")
@Table(name="TA")
public class Ta extends User {
    public Ta(){}

    public Ta(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    @Override
    public String toString() {
        return "Ta{" +
                "id=" + super.getId() +
                ", first_name='" + super.getFirst_name() + '\'' +
                ", last_name='" + super.getLast_name() + '\'' +
                ", user_name='" + super.getUser_name() + '\'' +
                '}';
    }
}
