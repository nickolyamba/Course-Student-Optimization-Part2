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
    private Set<TeacherPool> professorPools = new HashSet<TeacherPool>();

    public Professor(){}

    public Professor(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    public Set<TeacherPool> getProfessorPools() {
        return professorPools;
    }

    public void setProfessorPools(Set<TeacherPool> professorPools) {
        this.professorPools = professorPools;
    }

    public void addProfessorPool(TeacherPool teacherPool) {
        professorPools.add(teacherPool);
        teacherPool.setUser(this);
    }

    public void removeProfessorPool(TeacherPool teacherPool) {
        professorPools.remove(teacherPool);
        teacherPool.setUser(null);
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
