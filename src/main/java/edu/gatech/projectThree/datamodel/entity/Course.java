package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.CourseType;
import edu.gatech.projectThree.constants.Semester;

import javax.persistence.*;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private Semester semester;

    @Column(nullable = false)
    private CourseType type;

    public Course(Semester semester, CourseType type) {
        this.semester = semester;
        this.type = type;
    }

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
    }

    public CourseType getType() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", semester=" + semester +
                ", type=" + type +
                '}';
    }
}
