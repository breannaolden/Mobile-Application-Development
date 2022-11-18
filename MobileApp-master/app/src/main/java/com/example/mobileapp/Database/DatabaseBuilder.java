package com.example.mobileapp.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mobileapp.DAO.AssessmentDAO;
import com.example.mobileapp.DAO.CourseDAO;
import com.example.mobileapp.DAO.InstructorDAO;
import com.example.mobileapp.DAO.TermDAO;
import com.example.mobileapp.Entity.AssessmentEntity;
import com.example.mobileapp.Entity.CourseEntity;
import com.example.mobileapp.Entity.InstructorEntity;
import com.example.mobileapp.Entity.TermEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class, InstructorEntity.class}, version = 7)
public abstract class DatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract InstructorDAO instructorDAO();

    private static int NUMBER_OF_THREADS = 4;
    static ExecutorService dbWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile DatabaseBuilder instance;


    public static DatabaseBuilder getDatabaseInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseBuilder.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "database_scheduler")
                        .fallbackToDestructiveMigration()
                        .addCallback(dbCallback)
                        .build();
            }
        }
        return instance;
    }


    private static RoomDatabase.Callback dbCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            TermDAO termDAO = instance.termDAO();
            CourseDAO courseDAO = instance.courseDAO();
            AssessmentDAO assessmentDAO = instance.assessmentDAO();
            InstructorDAO instructorDAO = instance.instructorDAO();

            dbWriteExecutor.execute(()->{
                //termDAO.deleteAllFromTermsEntity();
                //courseDAO.deleteAllFromCourseEntity();
                //assessmentDAO.deleteAllFromAssessmentEntity();
                //instructorDAO.deleteAllFromInstructorEntity();

                TermEntity termDbTable = new TermEntity(1, "Term 1", "04/01/2022", "04/30/2022");
                termDAO.insert(termDbTable);
                TermEntity termDbTable2 = new TermEntity(2, "Term 2", "05/01/2022", "05/31/2022");
                termDAO.insert(termDbTable2);
                TermEntity termDbTable3 = new TermEntity(3, "Term 3", "06/01/2022", "06/30/2022");
                termDAO.insert(termDbTable3);

                CourseEntity courseDbTable = new CourseEntity(101, "Psychology 101", "04/01/2022", "05/01/2022", "Complete", "PS101", 1);
                courseDAO.insert(courseDbTable);
                CourseEntity courseDbTable2 = new CourseEntity(102, "Chemistry 101", "05/01/2022", "06/01/2022", "Complete", "CH102", 2);
                courseDAO.insert(courseDbTable2);
                CourseEntity courseDbTable3 = new CourseEntity(103, "Programming 101", "04/02/2022", "04/02/2022", "In Progress", "CS103", 1);
                courseDAO.insert(courseDbTable3);
                CourseEntity courseDbTable4 = new CourseEntity(104, "English 101", "05/02/2022", "05/02/2022", "Plan To Take", "EN104", 2);
                courseDAO.insert(courseDbTable4);
                CourseEntity courseDbTable5 = new CourseEntity(105, "Art History 101", "04/03/2022", "04/03/2022", "Complete", "AH101", 1);
                courseDAO.insert(courseDbTable5);
                CourseEntity courseDbTable6 = new CourseEntity(106, "History 101", "05/03/2022", "05/03/2022", "Complete", "HI102", 2);
                courseDAO.insert(courseDbTable6);
                CourseEntity courseDbTable7 = new CourseEntity(107, "Data 101", "04/04/2022", "04/04/2022", "In Progress", "DT103", 1);
                courseDAO.insert(courseDbTable7);
                CourseEntity courseDbTable8 = new CourseEntity(108, "Math 101", "06/04/2022", "06/04/2022", "Plan To Take", "EN104", 3);
                courseDAO.insert(courseDbTable8);

                AssessmentEntity assessmentTable = new AssessmentEntity(201, "Assessment 1", "Objective", "04/01/2022", "04/01/2022", 101);
                assessmentDAO.insert(assessmentTable);
                AssessmentEntity assessmentTable2 = new AssessmentEntity(202, "Assessment 2", "Performance", "05/01/2022", "05/01/2022", 102);
                assessmentDAO.insert(assessmentTable2);
                AssessmentEntity assessmentTable3 = new AssessmentEntity(203, "Assessment 3", "Objective", "04/02/2022", "04/02/2022", 103);
                assessmentDAO.insert(assessmentTable3);
                AssessmentEntity assessmentTable4 = new AssessmentEntity(204, "Assessment 4", "Performance", "05/02/2022", "05/02/2022", 104);
                assessmentDAO.insert(assessmentTable4);
                AssessmentEntity assessmentTable5 = new AssessmentEntity(205, "Assessment 5", "Objective", "04/03/2022", "04/03/2022", 105);
                assessmentDAO.insert(assessmentTable5);
                AssessmentEntity assessmentTable6 = new AssessmentEntity(206, "Assessment 6", "Performance", "05/03/2022", "05/03/2022", 106);
                assessmentDAO.insert(assessmentTable6);
                AssessmentEntity assessmentTable7 = new AssessmentEntity(207, "Assessment 7", "Objective", "04/04/2022", "04/04/2022", 107);
                assessmentDAO.insert(assessmentTable7);
                AssessmentEntity assessmentTable8 = new AssessmentEntity(208, "Assessment 8", "Performance", "06/04/2022", "06/04/2022", 108);
                assessmentDAO.insert(assessmentTable8);
                AssessmentEntity assessmentTable9 = new AssessmentEntity(209, "Assessment 9", "Objective", "04/01/2022", "04/01/2022", 101);
                assessmentDAO.insert(assessmentTable9);

                InstructorEntity instructorTable = new InstructorEntity(301, "Dr. One", "303-303-3031", "drone@wgu.edu", 101);
                instructorDAO.insert(instructorTable);
                InstructorEntity instructorTable2 = new InstructorEntity(302, "Dr. Two", "303-303-3032", "drtwo@wgu.edu", 102);
                instructorDAO.insert(instructorTable2);
                InstructorEntity instructorTable3 = new InstructorEntity(303, "Dr. Three", "303-303-3033", "drthree@wgu.edu", 103);
                instructorDAO.insert(instructorTable3);
                InstructorEntity instructorTable4 = new InstructorEntity(304, "Dr. Four", "303-303-3034", "drfour@wgu.edu", 104);
                instructorDAO.insert(instructorTable4);
                InstructorEntity instructorTable5 = new InstructorEntity(305, "Dr. Five", "303-303-3035", "drfive@wgu.edu", 105);
                instructorDAO.insert(instructorTable5);
                InstructorEntity instructorTable6 = new InstructorEntity(306, "Dr. Six", "303-303-3036", "drsix@wgu.edu", 106);
                instructorDAO.insert(instructorTable6);
                InstructorEntity instructorTable7 = new InstructorEntity(307, "Dr. Seven", "303-303-3037", "drseven@wgu.edu", 107);
                instructorDAO.insert(instructorTable7);
                InstructorEntity instructorTable8 = new InstructorEntity(308, "Dr. Eight", "303-303-3038", "dreight@wgu.edu", 108);
                instructorDAO.insert(instructorTable8);
            });
        }
    };




}
