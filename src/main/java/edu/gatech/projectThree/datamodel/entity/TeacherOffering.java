package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by nick on 3/18/16.
 */

@Entity
public class TeacherOffering implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OFFERING_ID", nullable = false)
    private Offering offering;

	private UserType userType;

    @Transient
	private boolean isAssigned;

    public TeacherOffering(){
    }

	// constructor
	public TeacherOffering(User user, Offering offering) {
		this.user = user;
		this.userType = user.getUserType();
		this.offering = offering;
		isAssigned = false; // by default Student is not assigned to a course, until 
		                    // Compute Engine produces solution that have a Student 
		                    // assigned to the Course. Set upon results of Gurobi computation
	}

	/**
	 * @return the student
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the student to set
	 */
	public void setUser(User user) {
		this.user = user;
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

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Override
    public String toString() {
        return "TeacherOffering{" +
                "id=" + id +
                ", userType='" + userType.name() + '\'' +
                ", user='" + user.toString() + '\'' +
                ", offering='" + offering.toString() + '\'' +
                ", isAssigned='" + isAssigned + '\'' +
                '}';
    }
	
}

