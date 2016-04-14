package edu.gatech.projectThree.datamodel.entity;

/**
 * Created by nikolay on 4/14/2016.
 */
public class CurrentSemester {

    private int semester_id;
    private Semester semester;

    public CurrentSemester(){}

    public CurrentSemester(Semester semester)
    {
        this.semester = semester;
        this.semester_id = semester.getId();
    }

    public int getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(int semester_id) {
        this.semester_id = semester_id;
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
                "semester_id=" + semester_id +
                ",season=" + semester.getSeason() +
                ",year=" + semester.getYear() +
                '}';
    }
}
