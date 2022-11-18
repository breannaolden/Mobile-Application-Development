package com.example.mobileapp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mobileapp.Entity.CourseEntity;
import com.example.mobileapp.Entity.TermEntity;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseEntity course);

    @Delete
    void delete(CourseEntity course);

    @Query("DELETE FROM course_table")
    void deleteAllFromCourseEntity();

    @Query("SELECT * FROM course_table ORDER BY courseId ASC")
    List<CourseEntity> getAllCoursesFromEntity();

}