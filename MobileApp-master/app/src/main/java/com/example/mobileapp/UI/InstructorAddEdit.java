package com.example.mobileapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.mobileapp.Database.Repository;
import com.example.mobileapp.Entity.CourseEntity;
import com.example.mobileapp.Entity.InstructorEntity;
import com.example.mobileapp.R;

import java.util.List;

public class InstructorAddEdit extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPhone;
    EditText editTextEmail;
    List<CourseEntity> courseEntityList;
    CourseEntity ccourse;
    InstructorEntity chosenInstructor;
    int courseId;
    int instructorId;
    int assessCourseId;
    Repository repo;
    List<InstructorEntity> instructorEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        repo = new Repository(getApplication());

        courseId = getIntent().getIntExtra("courseId", -1);
        instructorId = getIntent().getIntExtra("InstructorId", -1);
        assessCourseId = getIntent().getIntExtra("assessCourseId", -1);

        instructorEntityList = repo.getAllInstructorsFromRepo();
        for (InstructorEntity instructor: instructorEntityList) {
            if (instructor.getInstructorId() == instructorId) {
                chosenInstructor = instructor;
            }
        }
        editTextName = findViewById(R.id.instructor_name);
        editTextPhone = findViewById(R.id.instructor_phone);
        editTextEmail = findViewById(R.id.instructor_email);

        if (instructorId != -1) {
            editTextName.setText(chosenInstructor.getInstructorName());
            editTextPhone.setText(chosenInstructor.getInstructorPhone());
            editTextEmail.setText(chosenInstructor.getInstructorEmail());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SaveInstructorButton(View view) {
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();

        instructorEntityList = repo.getAllInstructorsFromRepo();
        int instId = 1;
        for (InstructorEntity i : instructorEntityList) {
            if (i.getInstructorId() >= instId) {
                instId = i.getInstructorId();
            }
        }
        if (instructorId != -1) {
            InstructorEntity updateTable = new InstructorEntity(instructorId, name, phone, email, assessCourseId);
            repo.insert(updateTable);
        } else {
            InstructorEntity insertInstructor = new InstructorEntity(++instId, name, phone, email, courseId);
            repo.insert(insertInstructor);
        }
        Intent intent = new Intent(this, CourseAddEdit.class);
        if (courseId != -1) {
            intent.putExtra("courseId", courseId);
        } else {
            intent.putExtra("courseId", assessCourseId);
        }
        startActivity(intent);
    }


}