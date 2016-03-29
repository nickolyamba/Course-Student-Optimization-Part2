package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.Season;

import java.util.Set;

import javax.persistence.*;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
    @Column
    private Season season;
    
    @Column
    private String year;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="semester")
    private Set<Offering> offering;

    public Semester() {
    }
    
    public Semester(int semester_id) {
        this.id = semester_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSemester(Season season) {
        this.season = season;
    }

    @Override
    public String toString() {
        return "Semester{" +
                "semester_id=" + id +
                ", season=" + season +'}';
    }
}
