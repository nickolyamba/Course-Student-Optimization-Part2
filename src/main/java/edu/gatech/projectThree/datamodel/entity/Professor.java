package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
@DiscriminatorValue("2")
@Table(name="PROFESSOR")
public class Professor extends Teacher{

    @OneToMany(mappedBy="professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfessorOffering> profOfferings = new HashSet<ProfessorOffering>();

    public Professor(){}

    public Professor(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    public Set<ProfessorOffering> getProfOfferings() {
        return profOfferings;
    }

    public void setProfOfferings(Set<ProfessorOffering> profOfferings) {
        this.profOfferings = profOfferings;
    }

    public void addProfessorPool(ProfessorOffering teacherOffering) {
        profOfferings.add(teacherOffering);
        teacherOffering.setProfessor(this);
    }

    public void removeProfessorPool(ProfessorOffering teacherOffering) {
        profOfferings.remove(teacherOffering);
        teacherOffering.setProfessor(null);
    }

    public void addOffering(Offering offering, ProfRequest profRequest) {
        ProfessorOffering teacherOffering = new ProfessorOffering(this, offering, profRequest);
        profOfferings.add(teacherOffering);
        teacherOffering.setProfessor(this);
    }

    public void removeOffering(Offering offering) {
        for(ProfessorOffering to : profOfferings)
        {
            if (to.getOffering() == offering)
            {
                profOfferings.remove(to);
                to.setProfessor(null);
            }
        }
    }

    @Override
    public int hashCode() {
        return profOfferings.hashCode();
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + super.getId() +
                ", first_name='" + super.getFirst_name() + '\'' +
                ", last_name='" + super.getLast_name() + '\'' +
                ", userName='" + super.getUserName() + '\'' +
                '}';
    }
}
