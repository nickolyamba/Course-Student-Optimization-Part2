package edu.gatech.projectThree.datamodel.entity;

import java.util.Set;

import javax.persistence.*;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Offering {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
    // https://en.wikibooks.org/wiki/Java_Persistence/OneToMany
    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinColumn(name="SEMESTER_ID")
    private Semester semester;
    
    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinColumn(name="COURSE_ID")
    private Course course;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="offering")
    private Set<Preference> preferences;

    /*
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(
        name="OFFER_STUD",
        joinColumns=@JoinColumn(name="OFFERING_ID", referencedColumnName="ID"),
        inverseJoinColumns=@JoinColumn(name="STUDENT_ID", referencedColumnName="ID"))
    private Set<Student> studentsAssigned;*/

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

    /*public void setSemester(Semester semester) {
        this.semester = semester;
    }*/

    public Course getCourse() {
        return course;
    }

    /*
    public void setCourse(Course course) {
        this.course = course;
    }*/
    
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
