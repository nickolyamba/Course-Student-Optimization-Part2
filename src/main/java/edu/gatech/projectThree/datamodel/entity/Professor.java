package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
@DiscriminatorValue("PROFESSOR")
@Table(name="PROFESSOR")
public class Professor extends User{
    /*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;*/

    public Professor(){}

    public Professor(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + super.getId() +
                ", first_name='" + super.getFirst_name() + '\'' +
                ", last_name='" + super.getLast_name() + '\'' +
                ", user_name='" + super.getUser_name() + '\'' +
                '}';
    }
}
