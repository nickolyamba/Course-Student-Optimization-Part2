package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.Seniority;
import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
@DiscriminatorValue("1")
@Table(name="STUDENT")
public class Student extends User {
    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;*/

    private Seniority seniority;

    private Double gpa;
    
    @OneToMany(mappedBy="student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Preference> preferences = new HashSet<Preference>();

    @OneToMany(mappedBy="student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Request> requests = new HashSet<Request>();

    // list of courses taken in previous semesters
    //https://en.wikibooks.org/wiki/Java_Persistence/ManyToMany#Example_of_a_ManyToMany_relationship_database
    @ManyToMany(fetch=FetchType.LAZY) //unidirectional
    @JoinTable(
            name="STUD_COUR_TAKEN",
            joinColumns=@JoinColumn(name="STUDENT_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="COURSE_ID", referencedColumnName="ID"))
    private Set<Course> coursesTaken = new HashSet<Course>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SPEC_ID")
    Specialization specialization;

    public Student(){}

    public Student(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }

    public Seniority getSeniority() {
        return seniority;
    }

    public void setSeniority(Seniority seniority) {
        this.seniority = seniority;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public void addPreference(Preference preference) {
        preferences.add(preference);
        preference.setStudent(this);
    }

    public void removePreference(Preference preference) {
        preferences.remove(preference);
        preference.setStudent(null);
    }

    public void addPreference(Offering offering, int priority) {
        Preference preference = new Preference(this, offering, priority);
        preferences.add(preference);
        preference.setStudent(this);
    }

    public void removePreference(Offering offering) {
        // !!! O(n) - expensive, might need define hashCode() & equals()
        for(Preference preference : preferences)
        {
            if(preference.getOffering() == offering)
            {
                preferences.remove(preference);
                preference.setOffering(null);
            }
        }
    }

    public Set<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preference> preferences) {
        for(Preference preference : preferences)
            this.addPreference(preference);
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        for(Request request : requests)
            this.addRequest(request);
    }

    public void addRequest(Request request) {
        requests.add(request);
        request.setStudent(this);
    }

    public void removeRequest(Request request) {
        requests.remove(request);
        request.setStudent(null);
    }

    public void addCourseTaken(Course course){
        coursesTaken.add(course);
    }

    public Set<Course> getCoursesTaken() {
        return coursesTaken;
    }

    public void setCoursesTaken(Set<Course> coursesTaken) {
        this.coursesTaken = coursesTaken;
    }

    public ArrayList<Course> getCoursesNotTaken(Iterable<Course> courseList) {
        ArrayList<Course> coursesNotTaken = new ArrayList<Course>();
        courseList.forEach(course -> {
            if (!getCoursesTaken().contains(course)) {
                coursesNotTaken.add(course);
            }
        });
        return coursesNotTaken;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + super.getId() +
                ", first_name='" + super.getFirst_name() + '\'' +
                ", last_name='" + super.getLast_name() + '\'' +
                ", userName='" + super.getUserName() + '\'' +
                '}';
    }
}

