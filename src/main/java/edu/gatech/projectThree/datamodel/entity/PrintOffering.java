package edu.gatech.projectThree.datamodel.entity;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick on 3/18/16.
 */
@Component
public class PrintOffering implements Serializable {

    private Offering offering;

    private Set<Student> students = new HashSet<>();
    private Set<Professor> professors = new HashSet<>();
    private Set<Ta> tas = new HashSet<>();

    public PrintOffering(){}
    public PrintOffering(Offering offering){this.offering = offering;}
    
    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }

    public Set<Ta> getTas() {
        return tas;
    }

    public void setTas(Set<Ta> tas) {
        this.tas = tas;
    }

    @Override
    public String toString() {
        return "Offering{" +
                "offering=" + offering.toString() +
                '}';
    }
}
