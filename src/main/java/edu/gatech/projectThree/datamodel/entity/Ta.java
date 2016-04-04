package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
@DiscriminatorValue("TA")
@Table(name="TA")
public class Ta extends User {

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeacherPool> taPools = new HashSet<TeacherPool>();

    public Ta(){}

    public Ta(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    public Set<TeacherPool> getTaPools() {
        return taPools;
    }

    public void setTaPools(Set<TeacherPool> taPools) {
        this.taPools = taPools;
    }

    public void addTaPool(TeacherPool teacherPool) {
        taPools.add(teacherPool);
        teacherPool.setUser(this);
    }

    public void removeProfessorPool(TeacherPool teacherPool) {
        taPools.remove(teacherPool);
        teacherPool.setUser(null);
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
