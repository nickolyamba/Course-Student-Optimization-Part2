package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
    private String firstName;
    private String lastName;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="student")
    private Set<Preference> preference;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

