package edu.gatech.projectThree.datamodel.entity;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 3/18/16.
 */
@Component
public class PrintOffering implements Serializable {

    private Offering offering;
    private int capacity;
    private Demand demand;

    private List<Student> students = new ArrayList<>();
    private List<Professor> professors = new ArrayList<>();
    private List<Ta> tas = new ArrayList<>();
    private List<Preference> preferences = new ArrayList<>();

    public PrintOffering(){}
    public PrintOffering(Offering offering){this.offering = offering;}

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public List<Ta> getTas() {
        return tas;
    }

    public void setTas(List<Ta> tas) {
        this.tas = tas;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        return "PrintOffering{" +
            "offering=" + getOffering().getId() +
            ", tas='" + tas.toString() + '\'' +
            ", prof='" + professors.toString() + '\'' +
            ", studs=" + students.toString() +
            //", offering=" + offerings +
            '}';
    }
}
