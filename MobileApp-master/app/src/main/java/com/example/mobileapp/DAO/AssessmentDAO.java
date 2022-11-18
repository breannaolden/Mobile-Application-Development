package com.example.mobileapp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mobileapp.Entity.AssessmentEntity;
import com.example.mobileapp.Entity.CourseEntity;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AssessmentEntity course);

    @Delete
    void delete(AssessmentEntity assessment);

    @Query("DELETE FROM assessment_table")
    void deleteAllFromAssessmentEntity();

    @Query("SELECT * FROM assessment_table ORDER BY assessmentId ASC")
    List<AssessmentEntity> getAllAssessmentsFromEntity();
}
