package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
@DiscriminatorValue("PROFESSOR")
@Table(name="PROFESSOR")
public class Professor extends User{

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeacherOffering> profOfferings = new HashSet<TeacherOffering>();

    public Professor(){}

    public Professor(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    public Set<TeacherOffering> getProfOfferings() {
        return profOfferings;
    }

    public void setProfOfferings(Set<TeacherOffering> profOfferings) {
        this.profOfferings = profOfferings;
    }

    public void addProfessorPool(TeacherOffering teacherOffering) {
        profOfferings.add(teacherOffering);
        teacherOffering.setUser(this);
    }

    public void removeProfessorPool(TeacherOffering teacherOffering) {
        profOfferings.remove(teacherOffering);
        teacherOffering.setUser(null);
    }

    public void addOffering(Offering offering) {
        TeacherOffering teacherOffering = new TeacherOffering(this, offering);
        profOfferings.add(teacherOffering);
        teacherOffering.setUser(this);
    }

    public void removeOffering(Offering offering) {
        for(TeacherOffering to : profOfferings)
        {
            if (to.getOffering() == offering)
            {
                profOfferings.remove(to);
                to.setUser(null);
            }
        }
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
