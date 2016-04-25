package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.Application;
import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.repository.OfferingRepository;
import edu.gatech.projectThree.repository.OptimizedTimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by pjreed on 4/24/16.
 */
@Controller
public class OfferingController {
    private OfferingRepository offeringRepository;
    private OptimizedTimeRepository optimizedTimeRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);


    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) { this.offeringRepository = offeringRepository; }

    @Autowired
    public void setOptimizedTimeRepository(OptimizedTimeRepository optimizedTimeRepository) {
        this.optimizedTimeRepository = optimizedTimeRepository;
    }


    @RequestMapping(value = "/offering/{offeringId}/students", method = RequestMethod.GET)
    public String coursePreferencesByRequest(@PathVariable String offeringId, Model model) {
        Offering offering = offeringRepository.findOne(Long.valueOf(offeringId));
        PrintOffering printOffering = new PrintOffering(offering);
        OptimizedTime lastOptimized = optimizedTimeRepository.findTopByOrderByTimestampDesc();

        List<Student> students = new ArrayList<>();
        List<Preference> preferenceList = new ArrayList<>();
        Demand demand = new Demand(offering);
        Set<Preference> preferences = offering.getPreferences();
        demand.getDemandMap().put("total", preferences.size());
        //LOGGER.info("Offering: " + offering.getId());
        for(Preference preference :  preferences)
        {
            //LOGGER.info("Preference: " + preference.toString());
            if(preference.getOptimizedTime() == lastOptimized &&
                    preference.isAssigned())
            {
                students.add(preference.getStudent());
                preferenceList.add(preference);

            }
        }
        printOffering.setStudents(students);
        printOffering.setCapacity(offering.getCapacity());
        printOffering.setPreferences(preferenceList);

        model.addAttribute("preferences", preferenceList);
        //model.addAttribute("offering", printOffering);
        //model.addAttribute("students", offering.getPreferences());
        return "offering/students";
    }
}
