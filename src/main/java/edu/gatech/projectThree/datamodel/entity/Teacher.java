package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.UserType;

import javax.persistence.MappedSuperclass;

/**
 * Created by nick on 3/18/16.
 */
@MappedSuperclass
public class Teacher extends User{

    public Teacher(){}

    public Teacher(String userName, String password, String first_name, String last_name, UserType userType) {
        super(userName, password, first_name, last_name, userType);
    }
    /*
    public abstract Set<TeacherOffering> getTeacherOfferings();

    public abstract void setTeacherOfferings(Set<TeacherOffering> profOfferings);

    public abstract void addTeacherPool(TeacherOffering teacherOffering);

    public abstract void removeTeacherPool(TeacherOffering teacherOffering);

    public abstract void addOffering(Offering offering);

    public abstract void removeOffering(Offering offering);*/

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + super.getId() +
                ", first_name='" + super.getFirst_name() + '\'' +
                ", last_name='" + super.getLast_name() + '\'' +
                ", user_name='" + super.getUser_name() + '\'' +
                '}';
    }
}
