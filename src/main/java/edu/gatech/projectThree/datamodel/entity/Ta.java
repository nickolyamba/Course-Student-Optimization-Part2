package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick on 3/18/16.
 */
@Entity
@DiscriminatorValue("TA")
@Table(name="TA")
public class Ta extends User {

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeacherOffering> taOfferings = new HashSet<TeacherOffering>();

    public Ta(){}

    public Ta(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    public Set<TeacherOffering> getTaOfferings() {
        return taOfferings;
    }

    public void setTaOfferings(Set<TeacherOffering> taOfferings) {
        this.taOfferings = taOfferings;
    }

    public void addTaOffering(TeacherOffering teacherOffering) {
        taOfferings.add(teacherOffering);
        teacherOffering.setUser(this);
    }

    public void removeTaOffering(TeacherOffering teacherOffering) {
        taOfferings.remove(teacherOffering);
        teacherOffering.setUser(null);
    }

    public void addOffering(Offering offering) {
        TeacherOffering teacherOffering = new TeacherOffering(this, offering);
        taOfferings.add(teacherOffering);
        teacherOffering.setUser(this);
    }

    public void removeOffering(Offering offering) {
        for(TeacherOffering to : taOfferings)
        {
            if (to.getOffering() == offering)
            {
                taOfferings.remove(to);
                to.setUser(null);
            }
        }
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
