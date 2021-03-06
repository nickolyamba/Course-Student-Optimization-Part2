package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.constants.UserType;
import edu.gatech.projectThree.datamodel.entity.User;
import edu.gatech.projectThree.service.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by dawu on 3/18/16.
 */

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    Scheduler scheduler;

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @RequestMapping(value = "/", method = GET)
    public String index(ModelMap model, Authentication authentication) {
        return "index";
    }

    @RequestMapping(value = "/user", produces = "application/json")
    public @ResponseBody User getUser() {
        User user = new User("dwu45", "password", "David", "Wu", UserType.STUDENT);
        LOGGER.info("user={}", user);
        return user;
    }

    // !!!Testing Gurobi engine!!!
    @RequestMapping(value = "/user/optimize", produces = "application/json")
    public @ResponseBody User optimize() {
        User user = new User("dwu45", "password", "David", "Wu", UserType.STUDENT);
        LOGGER.info("user={}", user);

        scheduler.schedule();

        return user;
    }

    @RequestMapping(value = "/login", method = GET)
    public String login(ModelMap model) {
        return "login";
    }


}

