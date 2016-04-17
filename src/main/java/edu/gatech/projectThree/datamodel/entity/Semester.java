package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.Season;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
public class Semester implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @Column(nullable = false)
    //@Enumerated(EnumType.ORDINAL)
    //@MapKeyEnumerated(EnumType.ORDINAL)
    private Season season;

    @Column(nullable = false)
    private String year;

    @Temporal(TemporalType.DATE)
    private Date start_date;

    @Temporal(TemporalType.DATE)
    private Date end_date;

    @OneToMany(mappedBy="semester", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Offering> offerings = new HashSet<Offering>();

    public Semester() {
    }

    public Semester(int semester_id) {
        this.id = semester_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSemester(Season season) {
        this.season = season;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Set<Offering> getOfferings() {
        return offerings;
    }

    public void setOfferings(Set<Offering> offerings) {
        this.offerings = offerings;
    }

    public void addOffering(Offering offering) {
        offerings.add(offering);
        offering.setSemester(this);
    }

    public void removeOffering(Offering offering) {
        this.offerings.remove(offering);
        offering.setSemester(null);
    }

    @Override
    public String toString() {
        return "Semester{" +
                "semester_id=" + id +
                ", season=" + season +
                ", year=" + year +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                '}';
    }
}