package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Request{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="STUDENT_ID", nullable = false)
    private Student student;

    @OneToMany(mappedBy="request", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Preference> preferences = new HashSet<Preference>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Request(){}

    public Request(Student student) {
        this.student = student;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Date getCreated() {
        return created;
    }

    public Set<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

    public void addPreference(Preference preference) {
        preferences.add(preference);
        preference.setRequest(this);
    }

    public void removePreference(Preference preference) {
        preferences.remove(preference);
        preference.setRequest(null);
    }

    //http://blog.octo.com/en/audit-with-jpa-creation-and-update-date/
    // timestamp saved upon creation of the object
    @PrePersist
    void onCreate() {
        created = new Timestamp((new Date()).getTime());
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", student='" + student.toString() + '\'' +
                ", preferences='" + preferences.toString() + '\'' +
                ", created='" + created.toString() + '\'' +
                '}';
    }
}
