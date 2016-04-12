package edu.gatech.projectThree.datamodel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.gatech.projectThree.constants.UserType;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dawu on 3/18/16.
 */

// https://en.wikibooks.org/wiki/Java_Persistence/Inheritance#Joined.2C_Multiple_Table_Inheritance
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="userType")
@DiscriminatorOptions(force=true)
@Table(name="USER")
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long id;
    @Column(name="user_name")
    private String userName;
    private String first_name;
    private String last_name;

    @JsonIgnore
    private String password;

    private UserType userType;

    public User(){}

    public User(String userName, String password, String first_name, String last_name, UserType userType) {
        this.userName = userName;
        this.userType = userType;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
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

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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
