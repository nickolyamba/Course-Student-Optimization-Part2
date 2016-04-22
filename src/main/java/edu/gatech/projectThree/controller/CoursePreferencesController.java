package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.repository.*;
import edu.gatech.projectThree.service.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by pjreed on 4/7/16.
 */
@Controller
public class CoursePreferencesController {
    private CourseRepository courseRepository;
    private StudentRepository studRepository;
    private SemesterRepository semesterRepository;
    private OfferingRepository offeringRepository;
    private RequestRepository requestRepository;
    private PreferenceRepository preferenceRepository;
    private GlobalState state;
    private Scheduler scheduler;
    private CurrentSemesterRepository currentSemesterRepository;

    @Autowired
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setStudRepository(StudentRepository studRepository) {
        this.studRepository = studRepository;
    }

    @Autowired
    public void setSemesterRepository(SemesterRepository semesterRepository) { this.semesterRepository = semesterRepository; }

    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) { this.offeringRepository = offeringRepository; }

    @Autowired
    public void setRequestRepository(RequestRepository requestRepository) { this.requestRepository = requestRepository; }

    @Autowired
    public void setPreferenceRepository(PreferenceRepository preferenceRepository) { this.preferenceRepository = preferenceRepository; }

    @Autowired
    public void setCurrentSemesterRepository(CurrentSemesterRepository currentSemesterRepository) {
        this.currentSemesterRepository = currentSemesterRepository;
    }

    @Autowired
    public void setState(GlobalState state) {
        this.state = state;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CoursePreferencesController.class);

    @RequestMapping(value = "/course_preferences/edit", method = RequestMethod.GET)
    public String editCoursePreferences(Model model, Authentication authentication) {

        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        Student currentStudent = studRepository.findByUserName(currentUser.getUsername());


        CurrentSemester currSemester = currentSemesterRepository.findTopByOrderBySemesterIdDesc();
        Set<Offering> allCurrentOfferings = currSemester.getSemester().getOfferings();
        ArrayList<Offering> offeringsNotTaken = currentStudent.getCoursesNotTakenAsOfferings(allCurrentOfferings);

        model.addAttribute("offeringsNotTaken", offeringsNotTaken);
        model.addAttribute("semester", currSemester.getSemester());
        return "course_preferences/edit";
    }

    @RequestMapping(value = "/course_preferences/edit", method = RequestMethod.POST)
    @ResponseBody
    public String editCoursePreferencesPost(Model model, Authentication authentication, @RequestBody Map<String, ArrayList<String>> json) {
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        Student currentStudent = studRepository.findByUserName(currentUser.getUsername());

        Request request = new Request(currentStudent);
        requestRepository.save(request);

        Semester semester = semesterRepository.findOne(Integer.parseInt(json.get("semester").get(0)));
        LOGGER.info("Semester requested:");
        LOGGER.info(semester.toString());

        final int[] index = {0};
        json.get("offerings").forEach(offeringId -> {
            Offering offering = offeringRepository.findOne(Long.valueOf(offeringId));
            Preference preference = new Preference(currentStudent, offering, index[0] + 1, request);
            preferenceRepository.save(preference);
            //state.getPreferences().add(preference); --- experiment with Global State - works well
            index[0]++;
        });

        scheduler.schedule(); // we can populate the lists we need right from here if we choose so
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
