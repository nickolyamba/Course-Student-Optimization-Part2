package edu.gatech.projectThree.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by pjreed on 3/28/16.
 */
@Controller
public class OptimizationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizationController.class);

    @RequestMapping(value = "/optimizations/new", method = RequestMethod.GET)
    public String index() {
        return "optimizations/new";
    }
}
