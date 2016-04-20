package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Specialization implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @ManyToMany(fetch=FetchType.LAZY) //unidirectional
    @JoinTable(
            name="SPEC_COUR",
            joinColumns=@JoinColumn(name="SPEC_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="COURSE_ID", referencedColumnName="ID"))
    private Set<Course> courses= new HashSet<Course>();

    public Set<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return "Specialization{" +
                "course_id=" + id +
                ", spec_name='" + name + '\'' +
                '}';
    }
}
