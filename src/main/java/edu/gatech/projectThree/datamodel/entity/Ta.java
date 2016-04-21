package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick on 3/18/16.
 */
@Entity
@DiscriminatorValue("3")
@Table(name="TA")
public class Ta extends Teacher {

    @OneToMany(mappedBy="ta", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaOffering> taOfferings = new HashSet<TaOffering>();

    public Ta(){}

    public Ta(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    public Set<TaOffering> getTaOfferings() {
        return taOfferings;
    }

    public void setTaOfferings(Set<TaOffering> taOfferings) {
        this.taOfferings = taOfferings;
    }

    public void addTaOffering(TaOffering teacherOffering) {
        taOfferings.add(teacherOffering);
        teacherOffering.setTa(this);
    }

    public void removeTaOffering(TaOffering teacherOffering) {
        taOfferings.remove(teacherOffering);
        teacherOffering.setTa(null);
    }

    public void addOffering(Offering offering, TaRequest taRequest) {
        TaOffering teacherOffering = new TaOffering(this, offering, taRequest);
        taOfferings.add(teacherOffering);
        teacherOffering.setTa(this);
    }

    public void removeOffering(Offering offering) {
        for(TaOffering to : taOfferings)
        {
            if (to.getOffering() == offering)
            {
                taOfferings.remove(to);
                to.setTa(null);
            }
        }
    }

    @Override
    public String toString() {
        return "Ta{" +
                "id=" + super.getId() +
                ", first_name='" + super.getFirst_name() + '\'' +
                ", last_name='" + super.getLast_name() + '\'' +
                ", userName='" + super.getUserName() + '\'' +
                '}';
    }
}
