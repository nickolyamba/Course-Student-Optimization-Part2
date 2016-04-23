package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Demand implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int priority;

    private int demand;

    private int total;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OFFERING_ID", nullable = false)
    private Offering offering;

    public Demand(){}

    public Demand(Offering offering, int priority, int demand) {
        this.offering = offering;
        this.priority = priority;
        this.demand = demand;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Demand{" +
                "id=" + id +
                ", offering=" + offering.toString()+
                ", priority=" + priority +
                ", demand=" + demand +
                '}';
    }
}
