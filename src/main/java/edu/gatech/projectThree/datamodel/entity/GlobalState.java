package edu.gatech.projectThree.datamodel.entity;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kolya on 4/18/2016.
 */

@Component
public class GlobalState {

    private List<Preference> preferences = new ArrayList<>();
    private List<ProfessorOffering> profOfferings = new ArrayList<>();
    private List<TaOffering> taOfferings = new ArrayList<>();

    private List<Student> students = new ArrayList<>();
    private List<Professor> professors = new ArrayList<>();
    private List<Ta> tas = new ArrayList<>();

    GlobalState(){}

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public List<ProfessorOffering> getProfOfferings() {
        return profOfferings;
    }

    public void setProfOfferings(List<ProfessorOffering> profOfferings) {
        this.profOfferings = profOfferings;
    }

    public List<TaOffering> getTaOfferings() {
        return taOfferings;
    }

    public void setTaOfferings(List<TaOffering> taOfferings) {
        this.taOfferings = taOfferings;
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
}
