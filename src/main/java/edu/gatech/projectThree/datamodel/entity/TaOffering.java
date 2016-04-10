package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;

/**
 * Created by nick on 3/18/16.
 */

@Entity
public class TaOffering {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TA_ID", nullable = false)
	private Ta ta;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OFFERING_ID", nullable = false)
	private Offering offering;

	//private UserType userType;

	@Transient
	private boolean isAssigned;

	public TaOffering(){
	}

	// constructor
	public TaOffering(Ta ta, Offering offering) {
		this.ta = ta;
		//this.userType = ta.getUserType();
		this.offering = offering;
		isAssigned = false; // by default Student is not assigned to a course, until
		// Compute Engine produces solution that have a Student
		// assigned to the Course. Set upon results of Gurobi computation
	}

	/**
	 * @return the student
	 */
	public Ta getTa() {
		return ta;
	}

	/**
	 * @param ta the student to set
	 */
	public void setTa(Ta ta) {
		this.ta = ta;
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
/*
    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
*/

	@Override
	public String toString() {
		return "TeacherOffering{" +
				"id=" + id +
				//", userType='" + userType.name() + '\'' +
				", ta='" + ta.toString() + '\'' +
				", offering='" + offering.toString() + '\'' +
				", isAssigned='" + isAssigned + '\'' +
				'}';
	}
}

