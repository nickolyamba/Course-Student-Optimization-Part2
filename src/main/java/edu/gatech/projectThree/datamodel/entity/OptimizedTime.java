package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by nikolay on 4/14/2016.
 */
@Entity
public class OptimizedTime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Id
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEMESTER_ID", nullable = false)
    private Semester semester;

    public OptimizedTime(){}

    public OptimizedTime(Semester semester) {
        this.semester = semester;
    }

    // won't be used most likely. Created just in case, there's a need
    public OptimizedTime(Semester semester, Date timestamp) {
        this.semester = semester;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    //http://blog.octo.com/en/audit-with-jpa-creation-and-update-date/
    // timestamp saved upon creation of the object
    @PrePersist
    void onCreate() {
        timestamp = new Timestamp((new Date()).getTime());
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "OptimizedTime{" +
                ", id='" + id + '\'' +
                ", semester='" + semester.toString() + '\'' +
                ", timestamp='" + timestamp.toString() + '\'' +
                '}';
    }
}
