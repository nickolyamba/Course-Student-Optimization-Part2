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
public class TaRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @OneToMany(mappedBy="taRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaOffering> taOfferings = new HashSet<TaOffering>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public TaRequest(){}

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public Set<TaOffering> getPreferences() {
        return taOfferings;
    }

    public void setPreferences(Set<TaOffering> taOfferings) {
        this.taOfferings = taOfferings;
    }

    public void addTaOffering(TaOffering taOffering) {
        taOfferings.add(taOffering);
        taOffering.setTaRequest(this);
    }

    public void removeTaOffering(TaOffering taOffering) {
        taOfferings.remove(taOffering);
        taOffering.setTaRequest(null);
    }

    //http://blog.octo.com/en/audit-with-jpa-creation-and-update-date/
    // timestamp saved upon creation of the object
    @PrePersist
    void onCreate() {
        created = new Timestamp((new Date()).getTime());
    }

    @Override
    public String toString() {
        return "TaRequest{" +
                "id=" + id +
                ", taOffering='" + taOfferings.toString() + '\'' +
                ", created='" + created.toString() + '\'' +
                '}';
    }
}
