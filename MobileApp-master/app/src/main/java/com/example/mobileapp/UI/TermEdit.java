package com.example.mobileapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobileapp.Database.Repository;
import com.example.mobileapp.Entity.CourseEntity;
import com.example.mobileapp.Entity.TermEntity;
import com.example.mobileapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TermEdit extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CourseAdapter courseAdapter;
    Repository repo;
    EditText editTextName;
    EditText editTextStartDate;
    EditText editTextEndDate;
    Calendar calendarStart = Calendar.getInstance();
    Calendar calendarEnd = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startDatePicker;
    DatePickerDialog.OnDateSetListener endDatePicker;

    int termId;
    int extraTermID;
    String termName;
    String termmStartDate;
    String termmEndDate;
    TermEntity chosenTerm;
    List<CourseEntity> allCoursesList;
    CourseEntity courses;

    List<CourseEntity> courseInTermList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // PREVENTS ON SCREEN KEYBOARD FROM POPPING UP AUTOMATICALLY
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        repo = new Repository(getApplication());

        getAndSetViewsById();
        setRecyclerViewAndAdapter();
        getTerm();
        getAllCourses();

        courseAdapter.courseSetter(courseInTermList);
        setDatePicker();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                repo.delete(courseAdapter.getCourseAt(viewHolder.getAdapterPosition()));
                courseAdapter.courseEntitiesList.remove(viewHolder.getAdapterPosition());
                courseAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(TermEdit.this, "Course Deleted Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void setRecyclerViewAndAdapter() {
        recyclerView = findViewById(R.id.edit_term_recycler);
        layoutManager = new LinearLayoutManager(this);
        courseAdapter = new CourseAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(courseAdapter);
    }
    public void getTerm() {
        termId = getIntent().getIntExtra("termId", -1);
        for (TermEntity t : repo.getAllTermsFromRepo()) {
            if (t.getTermId() == termId) {
                chosenTerm = t;
                extraTermID = t.getTermId();
            }
        }
        if (chosenTerm != null) {
            termName = chosenTerm.getTermTitle();
            termmStartDate = chosenTerm.getTermStart();
            termmEndDate = chosenTerm.getTermEnd();
        }
        editTextName.setText(termName);
        editTextStartDate.setText(termmStartDate);
        editTextEndDate.setText(termmEndDate);
    }

    public void getAllCourses() {
        allCoursesList = repo.getAllCoursesFromRepo();
        courseInTermList = new ArrayList<>();
        for (CourseEntity course : allCoursesList) {
            if (course.getCourseTermId() == extraTermID) {
                courseInTermList.add(course);
            }
        }
    }
    //add course to term button
    public void addCourseOnClick(View view) {
        Intent intent = new Intent(this, CourseAddEdit.class);
        intent.putExtra("selectedTermId", termId);
        startActivity(intent);
    }
    //save changes to term button
    public void saveTermOnClickEdit(View view) {
        String name = editTextName.getText().toString();
        String start = editTextStartDate.getText().toString();
        String end = editTextEndDate.getText().toString();
        TermEntity updatedTerm = new TermEntity(termId, name, start, end);
        repo.insert(updatedTerm);
        Intent intent = new Intent(this, TermList.class);
        startActivity(intent);
    }
    private void updateLabelStartDate() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editTextStartDate.setText(sdf.format(calendarStart.getTime()));
    }
    private void updateLabelEndDate() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editTextEndDate.setText(sdf.format(calendarEnd.getTime()));
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

    public void getAndSetViewsById() {
        editTextName = findViewById(R.id.edit_text_title);
        editTextName.setShowSoftInputOnFocus(false);
        editTextStartDate = findViewById(R.id.edit_text_startDate);
        editTextEndDate = findViewById(R.id.edit_text_endDate);
    }
    public void setDatePicker() {
        startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, day);
                String  myFormat = "MM/dd/yyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                updateLabelStartDate();
            }
        };
        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TermEdit.this, startDatePicker,
                        calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TermEdit.this, endDatePicker,
                        calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}