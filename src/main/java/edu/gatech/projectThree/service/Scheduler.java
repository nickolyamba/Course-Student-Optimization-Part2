package edu.gatech.projectThree.service;

import edu.gatech.projectThree.datamodel.dao.impl.*;
import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.datamodel.entity.Ta;
import edu.gatech.projectThree.repository.OfferingRepository;
import edu.gatech.projectThree.repository.ProfessorRepository;
import edu.gatech.projectThree.repository.StudentRepository;
import edu.gatech.projectThree.repository.TaRepository;
import edu.gatech.projectThree.service.Constraint.Constraint;
import gurobi.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dawu on 3/18/16.
 */
@Service("scheduler")
public class Scheduler{

    @Autowired
    @Qualifier("studentDAO")
    StudentDAO studentDAO;

    @Autowired
    @Qualifier("courseDAO")
    CourseDAO courseDAO;

    @Autowired
    @Qualifier("professorDAO")
    ProfessorDAO professorDAO;
    
    @Autowired
    @Qualifier("offeringDAO")
    OfferingDAO offeringDAO;

    @Autowired
    @Qualifier("semesterDAO")
    SemesterDAO semesterDAO;

    private StudentRepository studRepository;
    private OfferingRepository offeringRepository;
    private ProfessorRepository profRepository;
    private TaRepository taRepository;

    @Autowired
    private ApplicationContext context;

    //http://stackoverflow.com/questions/21553120/how-does-applicationcontextaware-work-in-spring
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Autowired
    public void setStudentRepository(StudentRepository studRepository) {
        this.studRepository = studRepository;
    }

    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) {
        this.offeringRepository = offeringRepository;
    }

    @Autowired
    public void setProfRepository(ProfessorRepository profRepository) {
        this.profRepository = profRepository;
    }

    @Autowired
    public void setTaRepository(TaRepository taRepository) {
        this.taRepository = taRepository;
    }

    // TODO: Redesign constraint template to not use multi-dimensional arrays
    public double schedule() {
        double result = 0;
        try {
            GRBEnv env = new GRBEnv("mip1.log");
            env.set(GRB.IntParam.LogToConsole, 1);
            GRBModel model = new GRBModel(env);

            //Student[] students = studentDAO.getAll();
            ArrayList<Student> studs = studRepository.findAll();
            Student[] students = new Student[studs.size()];
            students = studs.toArray(students);

            // ----------------------------------------------------------------------------------------
            //Course[] courses = courseDAO.getAll();
            // !!!!!!!!!! We deal with Offerings that's a given course at a given semester!!!!!!
            // so students are assigned to Offerings, not Courses;
            // Therefore, semesters are not used because we assign students within one semester only
            // ----------------------------------------------------------------------------------------

            ArrayList<Offering> offers = offeringRepository.findAll();
            Offering[] offerings = new Offering[offers.size()];
            offerings = offers.toArray(offerings);

            //Professor[] professors = professorDAO.getAll();
            ArrayList<Professor> profs = profRepository.findAll();
            Professor[] professors = new Professor[profs.size()];
            professors = profs.toArray(professors);

            ArrayList<Ta> taList = taRepository.findAll();
            Ta[] tas = new Ta[taList.size()];
            tas = taList.toArray(tas);

            //Semester[] semesters = semesterDAO.getAll();

            GRBVar[][][][] yijk = new GRBVar[students.length][offerings.length][professors.length][tas.length];
            // initialize variables here
            for (int i = 0; i < students.length; i++) {
                for (int j = 0; j < offerings.length; j++) {
                    for (int k = 0; k < professors.length; k++) {
                        for (int z = 0; z < tas.length; z++) {
                            GRBVar grbVar = model.addVar(0, 1, 0.0, GRB.BINARY, "");
                            yijk[i][j][k][z] = grbVar;
                        }
                    }
                }
            }

            GRBVar X = model.addVar(0, GRB.INFINITY, 0, GRB.CONTINUOUS, "X");
            model.update();
            GRBLinExpr obj = new GRBLinExpr();
            obj.addTerm(1, X);
            model.setObjective(obj);

            Collection<Constraint> constraints = context.getBeansOfType(Constraint.class).values();

            for (Constraint constraint : constraints) {
                constraint.addConstraint(model, yijk, X);
            }

            model.optimize();
            result = model.get(GRB.DoubleAttr.ObjVal);
        } catch (GRBException e) {
            e.printStackTrace();
        }

        return result;
    }

}
