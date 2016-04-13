package edu.gatech.projectThree.service;

import org.springframework.beans.factory.annotation.Autowired;
import edu.gatech.projectThree.repository.UserRepository;
import edu.gatech.projectThree.datamodel.entity.User;
import edu.gatech.projectThree.domain.CurrentUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by pjreed on 4/12/16.
 */
@Component("userDetailsService")
public class CurrentUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CurrentUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CurrentUser loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        return new CurrentUser(user);
    }
}
