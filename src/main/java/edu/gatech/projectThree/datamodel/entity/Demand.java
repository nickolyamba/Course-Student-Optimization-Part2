package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Demand implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ElementCollection
    @MapKeyColumn(name="demand")
    @Column(name="priority")
    @CollectionTable(name="demand_priority", joinColumns=@JoinColumn(name="priority_id"))
    Map<String, Integer> demand = new HashMap<String, Integer>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OFFERING_ID", nullable = false)
    private Offering offering;

    public Demand(){}

    public Demand(Offering offering) {
        this.offering = offering;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public Map<String, Integer> getDemandMap() {
        return demand;
    }

    public void setDemandMap(Map<String, Integer> demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        return "Demand{" +
                "id=" + id +
                ", offering=" + offering.toString()+
                ", demand=" + demand.toString() +
                '}';
    }
}
