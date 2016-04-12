package edu.gatech.projectThree.domain;

import edu.gatech.projectThree.datamodel.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Created by pjreed on 4/12/16.
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public CurrentUser(User user) {
        super(user.getUserName(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getUserType().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
