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
public class ProfRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @OneToMany(mappedBy="profRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfessorOffering> profOfferings = new HashSet<ProfessorOffering>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public ProfRequest(){}

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public Set<ProfessorOffering> getPreferences() {
        return profOfferings;
    }

    public void setPreferences(Set<ProfessorOffering> profOfferings) {
        this.profOfferings = profOfferings;
    }

    public void addTaOffering(ProfessorOffering profOffering) {
        profOfferings.add(profOffering);
        profOffering.setProfRequest(this);
    }

    public void removeTaOffering(ProfessorOffering profOffering) {
        profOfferings.remove(profOffering);
        profOffering.setProfRequest(null);
    }

    //http://blog.octo.com/en/audit-with-jpa-creation-and-update-date/
    // timestamp saved upon creation of the object
    @PrePersist
    void onCreate() {
        created = new Timestamp((new Date()).getTime());
    }

    @Override
    public String toString() {
        return "ProfRequest{" +
                "id=" + id +
                ", profOffering='" + profOfferings.toString() + '\'' +
                ", created='" + created.toString() + '\'' +
                '}';
    }
}
