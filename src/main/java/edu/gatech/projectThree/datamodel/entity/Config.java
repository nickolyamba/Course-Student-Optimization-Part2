package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by nikolay on 4/22/2016.
 */

@Entity
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double taCoeff;
    private boolean isGpa;
    private boolean isSeniority;
    private int minTa;
    private int maxTa;

    Config(){}

    Config(double taCoeff, boolean isGpa, boolean isSeniority, int minTa, int maxTa){
        this.taCoeff = taCoeff;
        this.isGpa = isGpa;
        this.isSeniority = isSeniority;
        this.minTa = minTa;
        this.maxTa = maxTa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTaCoeff() {
        return taCoeff;
    }

    public void setTaCoeff(double taCoeff) {
        this.taCoeff = taCoeff;
    }

    public boolean isGpa() {
        return isGpa;
    }

    public void setGpa(boolean gpa) {
        isGpa = gpa;
    }

    public boolean isSeniority() {
        return isSeniority;
    }

    public void setSeniority(boolean seniority) {
        isSeniority = seniority;
    }

    public int getMinTa() {
        return minTa;
    }

    public void setMinTa(int minTa) {
        this.minTa = minTa;
    }

    public int getMaxTa() {
        return maxTa;
    }

    public void setMaxTa(int maxTa) {
        this.maxTa = maxTa;
    }

    @Override
    public String toString() {
        return "Configs{" +
                "config_id=" + id +
                ", taCoeff='" + taCoeff + '\'' +
                ", isGpa='" + isGpa + '\'' +
                ", isSeniority=" + isSeniority +
                ", minTA=" + minTa +
                ", maxTA=" + maxTa +
                '}';
    }
}
