package com.example.mobileapp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mobileapp.Entity.InstructorEntity;

import java.util.List;

@Dao
public interface InstructorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InstructorEntity instructor);

    @Delete
    void delete(InstructorEntity instructor);

    @Query("DELETE FROM instructor_table")
    void deleteAllFromInstructorEntity();

    @Query("SELECT * FROM instructor_table ORDER BY  instructorId ASC")
    List<InstructorEntity> getInstructorsFromEntity();


}
