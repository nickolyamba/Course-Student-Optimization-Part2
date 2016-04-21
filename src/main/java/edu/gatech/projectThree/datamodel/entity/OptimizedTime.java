package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEMESTER_ID", nullable = false)
    private Semester semester;

    @OneToMany(mappedBy="optimizedTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Preference> preferences = new HashSet<Preference>();

    @OneToMany(mappedBy="optimizedTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaOffering> taOfferings = new HashSet<TaOffering>();

    @OneToMany(mappedBy="optimizedTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfessorOffering> profOfferings = new HashSet<ProfessorOffering>();

    public OptimizedTime(){}

    public OptimizedTime(Semester semester) {
        this.semester = semester;
    }

    // won't be used most likely. Created just in case there's a need
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

    public void addPreference(Preference preference) {
        preferences.add(preference);
        preference.setOptimizedTime(this);
    }

    public void removePreference(Preference preference) {
        preferences.remove(preference);
        preference.setOptimizedTime(null);
    }

    public Set<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preference> preferences) {
        for(Preference preference : preferences)
            this.addPreference(preference);
    }

    public Set<TaOffering> getTaOfferings() {
        return taOfferings;
    }

    public void setTaOfferings(Set<TaOffering> taOfferings) {
        for(TaOffering taOffering : taOfferings)
            this.addTaOffering(taOffering);
    }

    public void addTaOffering(TaOffering taOffering) {
        taOfferings.add(taOffering);
        taOffering.setOptimizedTime(this);
    }

    public Set<ProfessorOffering> getProfOfferings() {
        return profOfferings;
    }

    public void setProfOfferings(Set<ProfessorOffering> profOfferings) {
        for(ProfessorOffering profOffering : profOfferings)
            this.addProfOffering(profOffering);
    }

    public void addProfOffering(ProfessorOffering profOffering) {
        profOfferings.add(profOffering);
        profOffering.setOptimizedTime(this);
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
