package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.repository.ConfigRepository;
import edu.gatech.projectThree.repository.CurrentSemesterRepository;
import edu.gatech.projectThree.repository.OfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kolya on 4/18/2016.
 */
@Component
public class GlobalState {

    // these two used often, so makes sense to store them in the global state
    private CurrentSemesterRepository currentSemesterRepository;
    private OfferingRepository offeringRepository;
    private ConfigRepository configRepository;

    private CurrentSemester currentSemObject;
    private Config config;
    private Semester currentSemester;
    private List<Offering> offerings = new ArrayList<>();
    private List<Preference> preferences = new ArrayList<>();
    private List<ProfessorOffering> profOfferings = new ArrayList<>();
    private List<TaOffering> taOfferings = new ArrayList<>();

    private List<Student> students = new ArrayList<>();
    private List<Professor> professors = new ArrayList<>();
    private List<Ta> tas = new ArrayList<>();

    GlobalState(){
    }

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

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public Semester getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(Semester currentSemester) {
        this.currentSemester = currentSemester;
    }

    public CurrentSemester getCurrentSemObject() {
        return currentSemObject;
    }

    public void setCurrentSemObject(CurrentSemester currentSemObject) {
        this.currentSemObject = currentSemObject;
        this.currentSemester = currentSemObject.getSemester();
    }

    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) {
        this.offeringRepository = offeringRepository;
    }

    @Autowired
    public void setCurrentSemesterRepository(CurrentSemesterRepository currentSemesterRepository) {
        this.currentSemesterRepository = currentSemesterRepository;
    }

    @Autowired
    public void setConfigRepository(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //http://stackoverflow.com/questions/30454643
    @PostConstruct
    @Transactional
    private void initState(){
        // can't access Prefernces or semester.getYear() due to LAZY fetch in other beans...
        currentSemObject = currentSemesterRepository.findTopByOrderBySemesterIdDesc();
        currentSemester = currentSemObject.getSemester();
        config = configRepository.findTopByOrderByIdDesc();
        //offerings =  offeringRepository.findBySemesterOrderByIdAsc(currentSemester);

    }
}
