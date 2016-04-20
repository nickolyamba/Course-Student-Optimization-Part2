package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by pjreed on 4/7/16.
 */
@Controller
public class CoursePreferencesController {
    private CourseRepository courseRepository;
    private StudentRepository studRepository;
    private OfferingRepository offeringRepository;
    private RequestRepository requestRepository;
    private PreferenceRepository preferenceRepository;
    private CurrentSemesterRepository currentSemesterRepository;

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setStudRepository(StudentRepository studRepository) {
        this.studRepository = studRepository;
    }

    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) { this.offeringRepository = offeringRepository; }

    @Autowired
    public void setRequestRepository(RequestRepository requestRepository) { this.requestRepository = requestRepository; }

    @Autowired
    public void setPreferenceRepository(PreferenceRepository preferenceRepository) { this.preferenceRepository = preferenceRepository; }

    @Autowired
    public void setCurrentSemesterRepository(CurrentSemesterRepository currentSemesterRepository) { this.currentSemesterRepository = currentSemesterRepository; }

    private static final Logger LOGGER = LoggerFactory.getLogger(CoursePreferencesController.class);

    @RequestMapping(value = "/course_preferences/edit", method = RequestMethod.GET)
    public String editCoursePreferences(Model model, Authentication authentication) {

        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        Student currentStudent = studRepository.findByUserName(currentUser.getUsername());
        Semester semester = currentSemesterRepository.findTopByOrderBySemesterIdDesc().getSemester();

        Set<Offering> allCurrentOfferings = semester.getOfferings();

        model.addAttribute(
                "offeringsNotTaken",
                currentStudent.getCoursesNotTakenAsOfferings(allCurrentOfferings)
        );
        model.addAttribute("semester", semester);
        return "course_preferences/edit";
    }

    @RequestMapping(value = "/course_preferences/edit", method = RequestMethod.POST)
    @ResponseBody
    public String editCoursePreferencesPost(Model model, Authentication authentication, @RequestBody Map<String, ArrayList<String>> json) {
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        Student currentStudent = studRepository.findByUserName(currentUser.getUsername());

        Request request = new Request(currentStudent);
        requestRepository.save(request);

        final int[] index = {0};
        json.get("offerings").forEach(courseId -> {
            Offering offering = offeringRepository.findOne(Long.valueOf(courseId));
            Preference preference = new Preference(currentStudent, offering, index[0] + 1, request);
            preferenceRepository.save(preference);
            index[0]++;
        });
        return request.toString();
    }

    @RequestMapping(value = "/course_preferences", method = RequestMethod.GET)
    public String indexCoursePreferenced(Model model, Authentication authentication) {
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        Student currentStudent = studRepository.findByUserName(currentUser.getUsername());

        ArrayList<Request> requests = requestRepository.findByStudent(currentStudent);
        model.addAttribute("requests", requests);
        return "course_preferences/index";
    }

    @RequestMapping(value = "/course_preferences/{requestId}", method = RequestMethod.GET)
    public String coursePreferencesByRequest(@PathVariable String requestId, Model model) {
        Request request = requestRepository.findOne(Long.valueOf(requestId));
        Set<Preference> prefs = request.getPreferences();

        model.addAttribute("preferences", prefs);
        return "course_preferences/request";
    }
}
