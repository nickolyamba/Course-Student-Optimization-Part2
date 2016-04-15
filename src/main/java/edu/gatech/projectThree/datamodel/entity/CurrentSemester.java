package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by nikolay on 4/14/2016.
 */
@Entity
public class CurrentSemester implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @Transient
    private int curSemId;

    @Id
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEMESTER_ID", nullable = false)
    private Semester semester;

    public CurrentSemester(){}

    public CurrentSemester(Semester semester)
    {
        this.semester = semester;
        this.curSemId = semester.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurSemId() {
        return curSemId;
    }

    public void setCurSemId(int curSemId) {
        this.curSemId = curSemId;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "CuurentSemester{" +
                "curSemId=" + curSemId +
                ",season=" + semester.getSeason() +
                ",year=" + semester.getYear() +
                '}';
    }
}
