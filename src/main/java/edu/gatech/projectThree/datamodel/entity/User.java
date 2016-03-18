package edu.gatech.projectThree.datamodel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.gatech.projectThree.constants.UserType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String userName;

    private UserType userType;

    @JsonIgnore
    private String password;

    public User(String userName, UserType userType, String password) {
        this.userName = userName;
        this.userType = userType;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userType=" + userType +
                '}';
    }
}
