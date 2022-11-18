package com.example.mobileapp.Database;

import android.app.Application;

import com.example.mobileapp.DAO.AssessmentDAO;
import com.example.mobileapp.DAO.CourseDAO;
import com.example.mobileapp.DAO.InstructorDAO;
import com.example.mobileapp.DAO.TermDAO;
import com.example.mobileapp.Entity.AssessmentEntity;
import com.example.mobileapp.Entity.CourseEntity;
import com.example.mobileapp.Entity.InstructorEntity;
import com.example.mobileapp.Entity.TermEntity;

import java.util.List;

public class Repository {
    private TermDAO termDAO;
    private CourseDAO courseDAO;
    private AssessmentDAO assessmentDAO;
    private InstructorDAO instructorDAO;

    private List<TermEntity> allTermsList;
    private List<CourseEntity> allCoursesList;
    private List<AssessmentEntity> allAssessmentsList;
    private List<InstructorEntity> allInstructorsList;

    public Repository(Application application) {
        DatabaseBuilder database = DatabaseBuilder.getDatabaseInstance(application);
        termDAO = database.termDAO();
        courseDAO = database.courseDAO();
        assessmentDAO = database.assessmentDAO();
        instructorDAO = database.instructorDAO();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<TermEntity> getAllTermsFromRepo() {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            allTermsList = termDAO.getAllTermsFromEntity();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allTermsList;
    }

    public void insert(TermEntity termEntity) {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            termDAO.insert(termEntity);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete (TermEntity termEntity) {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            termDAO.delete(termEntity);
        });
    }

    public void deleteAllTerms() {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            termDAO.deleteAllFromTermsEntity();
        });
    }


    public List<CourseEntity> getAllCoursesFromRepo(){
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            allCoursesList = courseDAO.getAllCoursesFromEntity();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } return allCoursesList;
    }

    public void insert(CourseEntity course) {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            courseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete (CourseEntity course) {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            courseDAO.delete(course);
        });
    }


    public void deleteAllCourses() {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            courseDAO.deleteAllFromCourseEntity();
        });
    }

    public List<AssessmentEntity> getAllAssessmentsFromRepo() {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            allAssessmentsList = assessmentDAO.getAllAssessmentsFromEntity();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } return allAssessmentsList;
    }

    public void insert(AssessmentEntity assessment) {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            assessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(AssessmentEntity assessment) {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            assessmentDAO.delete(assessment);
        });
    }

    public void deleteAllAssessments() {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            courseDAO.deleteAllFromCourseEntity();
        });
    }

    public List<InstructorEntity> getAllInstructorsFromRepo() {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            allInstructorsList = instructorDAO.getInstructorsFromEntity();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } return allInstructorsList;
    }

    public void insert(InstructorEntity instructor) {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            instructorDAO.insert(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(InstructorEntity instructor) {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            instructorDAO.delete(instructor);
        });
    }

    public void deleteAllInstructors() {
        DatabaseBuilder.dbWriteExecutor.execute(()->{
            instructorDAO.deleteAllFromInstructorEntity();
        });
    }










}