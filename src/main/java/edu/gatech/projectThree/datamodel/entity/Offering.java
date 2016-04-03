package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Offering implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    // https://en.wikibooks.org/wiki/Java_Persistence/OneToMany
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEMESTER_ID", nullable = false)
    private Semester semester;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="COURSE_ID", nullable = false)
    private Course course;
    
    @OneToMany(mappedBy="offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Preference> preferences;

    public Offering(){}

    public Offering(Semester semester, Course course) {
        this.semester = semester;
        this.course = course;
    }
    
    /*
    public void addStudentsAssigned(Student student) {
        this.studentsAssigned.add(student);
        if (student.getOwner() != this) {
        	student.setOwner(this);
        }
    }*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
        if (!semester.getOfferings().contains(this))
            semester.getOfferings().add(this);
    }

    public Course getCourse() {
        return course;
    }


    public void setCourse(Course course) {
        this.course = course;
        if (!course.getOfferings().contains(this))
            course.getOfferings().add(this);
    }
    
    public void addStudent(Student student, int priority){
    	preferences.add(new Preference(student, this, priority));
    	
    }

    @Override
    public String toString() {
        return "Offering{" +
                "id=" + id +
                ", semester=" + semester.getId() + " " + semester.getSeason() +
                ", course=" + course.getCourse_name() + " "  + course.getCourse_num()+
                '}';
    }
}
