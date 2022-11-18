package com.example.mobileapp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mobileapp.Entity.TermEntity;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TermEntity term);

    @Delete
    void delete(TermEntity term);

    @Query("DELETE FROM terms_table")
    void deleteAllFromTermsEntity();

    @Query("SELECT * FROM terms_table ORDER BY termId ASC")
    List<TermEntity> getAllTermsFromEntity();
}
