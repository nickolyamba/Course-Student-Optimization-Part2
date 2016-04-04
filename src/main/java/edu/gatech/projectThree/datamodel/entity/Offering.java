package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Offering implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    // https://en.wikibooks.org/wiki/Java_Persistence/OneToMany
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEMESTER_ID", nullable = false)
    private Semester semester;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="COURSE_ID", nullable = false)
    private Course course;
    
    @OneToMany(mappedBy="offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Preference> preferences = new HashSet<Preference>();

    @OneToMany(mappedBy="offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeacherOffering> teacherPool = new HashSet<TeacherOffering>();

    @OneToMany(mappedBy="offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Demand> demands = new HashSet<Demand>();

    public Offering(){}

    public Offering(Semester semester, Course course) {
        this.semester = semester;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
        if (!semester.getOfferings().contains(this))
            semester.getOfferings().add(this);
    }

    public Course getCourse() {
        return course;
    }


    public void setCourse(Course course) {
        this.course = course;
        if (!course.getOfferings().contains(this))
            course.getOfferings().add(this);
    }
    
    public void addStudent(Student student, int priority){
        Preference preference = new Preference(student, this, priority);
        preferences.add(preference);
        preference.setOffering(this);
    	
    }

    public void removeStudent(Student student){
        // O(n) - expensive,
        // so if we going to use this method, we can override
        // equals() and hashCode() in Preference
        for(Preference preference : preferences)
        {
            if(preference.getStudent() == student)
            {
                preferences.remove(preference);
                preference.setOffering(null);
            }
        }
    }

    public void addPreference(Preference preference) {
        preferences.add(preference);
        preference.setOffering(this);
    }

    public void removePreference(Preference preference) {
        preferences.remove(preference);
        preference.setOffering(null);
    }

    public Set<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

    public Set<TeacherOffering> getTeacherPool() {
        return teacherPool;
    }

    public void setTeacherPool(Set<TeacherOffering> teacherPool) {
        this.teacherPool = teacherPool;
    }

    public void addTecherOffering(TeacherOffering teacherOffering) {
        teacherPool.add(teacherOffering);
        teacherOffering.setOffering(this);
    }

    public void removeTeacherOffering(TeacherOffering teacherOffering) {
        teacherPool.remove(teacherOffering);
        teacherOffering.setOffering(null);
    }

    public void addTecher(User user) {
        TeacherOffering teacherOffering = new TeacherOffering(user, this);
        teacherPool.add(teacherOffering);
        teacherOffering.setOffering(this);
    }

    public void removeTecher(User user) {
        for(TeacherOffering to : teacherPool)
        {
            if (to.getUser() == user)
            {
                teacherPool.remove(to);
                to.setOffering(null);
            }
        }
    }

    public Set<Demand> getDemands() {
        return demands;
    }

    public void setDemands(Set<Demand> demands) {
        this.demands = demands;
    }

    public void addDemand(Demand demand) {
        demands.add(demand);
        demand.setOffering(this);
    }

    public void removeDemand(Demand demand) {
        demands.remove(demand);
        demand.setOffering(null);
    }

    @Override
    public String toString() {
        return "Offering{" +
                "id=" + id +
                ", semester=" + semester.getId() + " " + semester.getSeason() +
                ", course=" + course.getCourse_name() + " "  + course.getCourse_num()+
                '}';
    }
}
