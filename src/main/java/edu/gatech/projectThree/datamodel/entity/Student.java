package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
@DiscriminatorValue("STUDENT")
@Table(name="STUDENT")
public class Student extends User {
    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;*/
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="student")
    private Set<Preference> preference;

    public Student(){}

    public Student(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    /*
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OFFERING_ID")
    private Offering offeringAssigned;
    
    @ManyToMany(mappedBy="studentsAssigned") 
    private Set<Offering> offeringsAssigned;
    */
    /*
    public void setOfferingAssigned(Offering offering) {
        this.owner = employee;
        if (!employee.getPhones().contains(this)) {
            employee.getPhones().add(this);
        }
    }*/

    @Override
    public String toString() {
        return "Student{" +
                "id=" + super.getId() +
                ", first_name='" + super.getFirst_name() + '\'' +
                ", last_name='" + super.getLast_name() + '\'' +
                ", user_name='" + super.getUser_name() + '\'' +
                '}';
    }
}

