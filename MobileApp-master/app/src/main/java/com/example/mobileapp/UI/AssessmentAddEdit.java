package com.example.mobileapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mobileapp.Database.Repository;
import com.example.mobileapp.Entity.AssessmentEntity;
import com.example.mobileapp.Entity.CourseEntity;
import com.example.mobileapp.NotificationReceiver;
import com.example.mobileapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssessmentAddEdit extends AppCompatActivity {
//establish variables
    EditText editTextName;
    EditText editTextStart;
    EditText editTextEnd;
    Spinner spinnerAssessmentType;
    Calendar calendarStart = Calendar.getInstance();
    Calendar calendarEnd = Calendar.getInstance();
    SimpleDateFormat dateFormatter;
    DatePickerDialog.OnDateSetListener startDatePicker;
    DatePickerDialog.OnDateSetListener endDatePicker;
    Repository repo;
    List<AssessmentEntity> assesmentList;
    AssessmentEntity chosenAssessment;
    List<CourseEntity> courseEntityList;

    int courseId;
    int assessmentId;
    int assessmentCourseId;
    String assessmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_add_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String myDateFormat = "MM/dd/yyyy";
        dateFormatter = new SimpleDateFormat(myDateFormat, Locale.US);
        courseId = getIntent().getIntExtra("courseId", -1);
        assessmentId = getIntent().getIntExtra("assessmentId", -1);
        assessmentCourseId = getIntent().getIntExtra("assessCourseId", -1);
        repo = new Repository(getApplication());

        getAllCourses();
        getAndSetViewsById();
        getAssessmentsInCourse();
        //setRecyclerViews();

// PREVENTS NULL OBJECT REFERENCE ERROR
        assesmentList = repo.getAllAssessmentsFromRepo();
        for (AssessmentEntity i : assesmentList) {
            if (i.getAssessmentId() == assessmentId) {
                chosenAssessment = i;
            }
        }
        if (assessmentId != -1) {
            editTextName.setText(chosenAssessment.getAssessmentTitle());
            //editTextType.setText(assessment.getAssessmentType());
            editTextStart.setText(chosenAssessment.getAssessmentStartDate());
            editTextEnd.setText(chosenAssessment.getAssessmentEndDate());
        }

        // SPINNER FOR ASSESSMENT TYPE SETUP
        Spinner spnnr = findViewById(R.id.spinnerAssessmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnnr.setAdapter(adapter);
        spinnerAssessmentType.setSelection(0);

        AssessmentEntity currentAssessment = findAssessment(assessmentId);
        if (currentAssessment!=null) {
            setAssessmentTypeSpinner(spinnerAssessmentType, currentAssessment);
        }
        spinnerAssessmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assessmentType = spinnerAssessmentType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, day);
                updateStartLabel();
            }
        };

        endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, month);
                calendarEnd.set(Calendar.DAY_OF_MONTH, day);
                updateEndLabel();
            }
        };

        editTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AssessmentAddEdit.this, startDatePicker,
                        calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editTextEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AssessmentAddEdit.this, endDatePicker,
                        calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assessment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.assessment_date_alert:
                String editTextDate = editTextStart.getText().toString();
                Date startDate = null;
                try {
                    startDate = dateFormatter.parse(editTextDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long timeTrigger = startDate.getTime();
                Intent intent = new Intent(AssessmentAddEdit.this, NotificationReceiver.class);
                intent.putExtra("key", chosenAssessment.getAssessmentTitle() +
                        " assessment starts today");
                Toast.makeText(AssessmentAddEdit.this, "Assessment notification set",
                        Toast.LENGTH_SHORT).show();

                PendingIntent send = PendingIntent.getBroadcast(AssessmentAddEdit.this,
                        MainActivity.alertNum++, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeTrigger, send);
                return true;

            case R.id.assessment_end_date_alert:
                String editTextEndDate = editTextEnd.getText().toString();
                Date endDate = null;

                try {
                    endDate = dateFormatter.parse(editTextEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long endTrigger = endDate.getTime();
                Intent intent2 = new Intent(AssessmentAddEdit.this,
                        NotificationReceiver.class);
                intent2.putExtra("key", chosenAssessment.getAssessmentTitle() +
                        " assessment is due today");
                Toast.makeText(AssessmentAddEdit.this, "Notification set",
                        Toast.LENGTH_SHORT).show();
                PendingIntent sendEnd = PendingIntent.getBroadcast(AssessmentAddEdit.this,
                        MainActivity.alertNum++, intent2, 0);
                AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager1.set(AlarmManager.RTC_WAKEUP, endTrigger, sendEnd);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // list of assessments associated with the course
    public void getAssessmentsInCourse() {
        assesmentList = new ArrayList<>();
        List<AssessmentEntity> list = repo.getAllAssessmentsFromRepo();
        for (AssessmentEntity l : list) {
            if (l.getAssessmentCourseId() == courseId) {
                assesmentList.add(l);
            }
        }
        assesmentList = repo.getAllAssessmentsFromRepo();
    }

    // get the current assessment
    public AssessmentEntity findAssessment(int mAssessmentId){
        AssessmentEntity currentAssessment=null;
        List<AssessmentEntity> allAssessments= repo.getAllAssessmentsFromRepo();
        for(AssessmentEntity assessment:allAssessments){
            if(mAssessmentId==assessment.getAssessmentId()){
                currentAssessment=assessment;
            }
        }
        return currentAssessment;
    }

    // get all courses
    public void getAllCourses() {
        courseEntityList = repo.getAllCoursesFromRepo();
    }


    public void getSelectedAssessment() {
        for (AssessmentEntity assessment : assesmentList) {
            if (assessmentId == assessment.getAssessmentId()) {
                chosenAssessment = assessment;
            }
        }
        editTextName.setText(chosenAssessment.getAssessmentTitle());
        editTextStart.setText(chosenAssessment.getAssessmentStartDate());
        editTextEnd.setText(chosenAssessment.getAssessmentEndDate());
    }

    public void saveAssessmentButtonClick(View view) {
        String title = editTextName.getText().toString();
        String type = spinnerAssessmentType.getSelectedItem().toString();
        String date = editTextStart.getText().toString();
        String endDate = editTextEnd.getText().toString();

        assesmentList = repo.getAllAssessmentsFromRepo();
        int assessmentId = 1;
        for (AssessmentEntity assessment : assesmentList) {
            if (assessment.getAssessmentId() >= assessmentId) {
                assessmentId = assessment.getAssessmentId();
            }
        }
        if (this.assessmentId != 1) {
            AssessmentEntity updateTable = new AssessmentEntity(this.assessmentId, title, type, date, endDate, assessmentCourseId);
            repo.insert(updateTable);
        } else {
            AssessmentEntity insertTable = new AssessmentEntity(++assessmentId, title, type, date, endDate, courseId);
            repo.insert(insertTable);
        }

        Intent intent = new Intent(this, CourseAddEdit.class);
        if (courseId != -1) {
            intent.putExtra("courseId", courseId);
        } else {
            intent.putExtra("courseId", assessmentCourseId);
        }
        startActivity(intent);
    }

    public void setAssessmentTypeSpinner(Spinner spnnr, AssessmentEntity course) {
        if (course.getAssessmentType().equals("Performance")) {
            spnnr.setSelection(0);
        }
        if (course.getAssessmentType().equals("Objective")) {
            spnnr.setSelection(1);
        }
    }

    public void getAndSetViewsById() {
        editTextName = findViewById(R.id.assessment_name);
        spinnerAssessmentType = findViewById(R.id.spinnerAssessmentType);
        editTextStart = findViewById(R.id.assessment_date);
        editTextEnd = findViewById(R.id.assessment_end);
    }

    public void updateStartLabel() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editTextStart.setText(sdf.format(calendarStart.getTime()));
    }
    public void updateEndLabel() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editTextEnd.setText(sdf.format(calendarEnd.getTime()));
    }

}