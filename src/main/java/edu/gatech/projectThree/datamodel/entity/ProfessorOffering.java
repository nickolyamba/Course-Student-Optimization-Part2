package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;

/**
 * Created by nick on 3/18/16.
 */

@Entity
public class ProfessorOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROF_ID", nullable = false)
    private Professor professor;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OFFERING_ID", nullable = false)
    private Offering offering;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isAssigned;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="OPTIMIZED_ID")
    private OptimizedTime optimizedTime;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="PROFREQUEST_ID") //set to nullable to allow flexibility
    private ProfRequest profRequest;

    public ProfessorOffering(){
    }

    // constructor
    public ProfessorOffering(Professor professor, Offering offering) {
        this.professor = professor;
        //this.userType = professor.getUserType();
        this.offering = offering;
        isAssigned = false; // by default Prof is not assigned to a course, until
        // Compute Engine produces solution that have a Student
        // assigned to the Course. Set upon results of Gurobi computation
    }

    /**
     * @return the student
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * @param professor the student to set
     */
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    /**
     * @return the offering
     */
    public Offering getOffering() {
        return offering;
    }

    /**
     * @param offering the offering to set
     */
    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the isAssigned
     */
    public boolean isAssigned() {
        return isAssigned;
    }

    /**
     * @param isAssigned the isAssigned to set
     */
    public void setAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public OptimizedTime getOptimizedTime() {
        return optimizedTime;
    }

    public void setOptimizedTime(OptimizedTime optimizedTime) {
        this.optimizedTime = optimizedTime;
    }

    public ProfRequest getProfRequest() {
        return profRequest;
    }

    public void setProfRequest(ProfRequest profRequest) {
        this.profRequest = profRequest;
    }

    @Override
    public String toString() {
        return "TeacherOffering{" +
                "id=" + id +
                //", userType='" + userType.name() + '\'' +
                ", professor='" + professor.toString() + '\'' +
                ", offering='" + offering.toString() + '\'' +
                ", isAssigned='" + isAssigned + '\'' +
                '}';
    }
	
}

