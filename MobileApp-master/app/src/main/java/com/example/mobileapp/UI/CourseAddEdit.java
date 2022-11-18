package com.example.mobileapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.mobileapp.Entity.InstructorEntity;
import com.example.mobileapp.NotificationReceiver;
import com.example.mobileapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseAddEdit extends AppCompatActivity {

    EditText editTextName;
    EditText editStartDate;
    EditText editEndDate;
    //EditText editStatus;
    Spinner spinnerCourseStatus;
    EditText editTextNotes;
    Calendar calendarStart = Calendar.getInstance();
    Calendar calendarEnd = Calendar.getInstance();
    SimpleDateFormat dateFormatter;
    DatePickerDialog.OnDateSetListener startDatePicker;
    DatePickerDialog.OnDateSetListener endDatePicker;

    Repository repo;
    List<CourseEntity> courseEntitiesFromDB;
    CourseEntity chosenCourse;
    List<AssessmentEntity> assessmentEntityList;
    List<InstructorEntity> instructorEntityList;

    RecyclerView recyclerViewAssess;
    RecyclerView recyclerViewInstr;
    AssessmentAdapter assessmentAdapter;
    InstructorAdapter instructorAdapter;
    RecyclerView.LayoutManager layoutManagerInstr;
    RecyclerView.LayoutManager layoutManagerAssess;

    int ttermId;
    int courseId;
    String mStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String myDateFormat = "MM/dd/yyyy";
        dateFormatter = new SimpleDateFormat(myDateFormat, Locale.US);
        ttermId = getIntent().getIntExtra("selectedTermId", -1);
        courseId = getIntent().getIntExtra("courseId", -1);
        repo = new Repository(getApplication());

        getAndSetViewsById();
        getAllCourses();
        getAssessmentsInCourse();
        getInstructorsInCourse();
        setRecyclerViews();
        deleteInstructorHelper();
        deleteAssessmentHelper();
        setDatePicker();

// SPINNER FOR COURSE STATUS SETUP
        Spinner spinner = findViewById(R.id.spinnerCourseStatus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_progress_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinnerCourseStatus.setSelection(0);

        CourseEntity currentCourse = findCourse(courseId);
        if (currentCourse!=null) {
            setCourseStatusSpinner(spinnerCourseStatus, currentCourse);
        }
        spinnerCourseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStatus = spinnerCourseStatus.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_course_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.share_notes:
                Intent notesIntent = new Intent();
                notesIntent.setAction(Intent.ACTION_SEND);
                notesIntent.putExtra(Intent.EXTRA_TEXT, editTextNotes.getText().toString());
                notesIntent.putExtra(Intent.EXTRA_TITLE, "Share Note");
                notesIntent.setType("text/plain");
                Intent noteIntentChooser = Intent.createChooser(notesIntent, null);
                startActivity(noteIntentChooser);
                return true;
            case R.id.set_course_start_alert:
                String coursesStartDate = editStartDate.getText().toString();
                Date start = null;
                try {
                    start = dateFormatter.parse(coursesStartDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long startTrigger = start.getTime();
                Intent startIntent = new Intent(CourseAddEdit.this, NotificationReceiver.class);
                startIntent.putExtra("key", chosenCourse.getCourseName() + " course begins today!");
                Toast.makeText(CourseAddEdit.this, "Starting notification set", Toast.LENGTH_SHORT).show();
                // for course start notification
                PendingIntent startSend = PendingIntent.getBroadcast(CourseAddEdit.this, MainActivity.alertNum++, startIntent, 0);
                AlarmManager startAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                startAlarmManager.set(AlarmManager.RTC_WAKEUP, startTrigger,startSend);
                return true;

            case R.id.set_course_end_alert:
                String courseEndDate = editEndDate.getText().toString();
                Date end = null;
                try {
                    end = dateFormatter.parse(courseEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long endTrigger = end.getTime();
                Intent endIntent = new Intent(CourseAddEdit.this, NotificationReceiver.class);
                endIntent.putExtra("key", chosenCourse.getCourseName() + " convenes today");
                Toast.makeText(CourseAddEdit.this, "Ending notification set", Toast.LENGTH_SHORT);
                //for course end notification
                PendingIntent endSend = PendingIntent.getBroadcast(CourseAddEdit.this, MainActivity.alertNum++, endIntent, 0);
                AlarmManager endAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                endAlarmManager.set(AlarmManager.RTC_WAKEUP, endTrigger, endSend);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void addInstructorButton(View view) {
        Intent intent = new Intent(this, InstructorAddEdit.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    public void addAssessmentButton(View view) {
        int courseAssessments = assessmentEntityList.size();
        if (courseAssessments >= 5) {
            Toast.makeText(CourseAddEdit.this, "No more than 5 assessments are allowed", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, AssessmentAddEdit.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }


    public void getAssessmentsInCourse() {
        assessmentEntityList = new ArrayList<>();
        List<AssessmentEntity> list = repo.getAllAssessmentsFromRepo();
        for (AssessmentEntity a : list){
            if (a.getAssessmentCourseId() == courseId){
                assessmentEntityList.add(a);
            }
        }
    }



    public void getInstructorsInCourse() {
        instructorEntityList = new ArrayList<>();
        List<InstructorEntity> list = repo.getAllInstructorsFromRepo();
        for (InstructorEntity i : list){
            if (i.getInstructorCourseId() == courseId){
                instructorEntityList.add(i);
            }
        }
    }

    public void getSelectedCourse() {
        for (CourseEntity course : courseEntitiesFromDB) {
            if (courseId == course.getCourseId()) {
                chosenCourse = course;
            }
        }

        editTextName.setText(chosenCourse.getCourseName());
        editStartDate.setText(chosenCourse.getCourseStart());
        editEndDate.setText(chosenCourse.getCourseEnd());
        //mStatus.setText(mSelectedCourse.getCourseStatus());
        editTextNotes.setText(chosenCourse.getCourseNotes());
        ttermId = chosenCourse.getCourseTermId();
    }

    // SAVE BUTTON
    public void saveCourseOnClick(View view) {
        String name = editTextName.getText().toString();
        String start = editStartDate.getText().toString();
        String end = editEndDate.getText().toString();
        String status = spinnerCourseStatus.getSelectedItem().toString();
        String note = editTextNotes.getText().toString();
        if (note.trim().isEmpty()) {
            note = " ";
        }
        if (name.trim().isEmpty() || start.trim().isEmpty() || end.trim().isEmpty() || status.trim().isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please fill out all required forms");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
        if (courseId != -1) {
            CourseEntity updateCourse = new CourseEntity(courseId, name, start, end, status, note, ttermId);
            repo.insert(updateCourse);
        } else {
            int newCourseId = courseEntitiesFromDB.size();
            for (CourseEntity course : courseEntitiesFromDB) {
                if (course.getCourseId() >= newCourseId) {
                    newCourseId = course.getCourseId();
                }
            }
            CourseEntity addCourse = new CourseEntity(newCourseId + 1, name, start, end, status, note, ttermId);
            repo.insert(addCourse);
        }
        Intent intent = new Intent(this, TermList.class);
        startActivity(intent);
    }

    public void getAllCourses() {
        courseEntitiesFromDB = repo.getAllCoursesFromRepo();
    }

    public void setDatePicker() {
        startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                updateStartLabel();
            }
        };
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CourseAddEdit.this, startDatePicker,
                        calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH)
                        , calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, month);
                calendarEnd.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                updateEndLabel();
            }
        };
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CourseAddEdit.this, endDatePicker,
                        calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH)
                        , calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void setCourseStatusSpinner(Spinner spnr, CourseEntity course){
        if(course.getCourseStatus().equals("In Progress")){
            spnr.setSelection(0);
        }
        if(course.getCourseStatus().equals("Completed")){
            spnr.setSelection(1);
        }
        if(course.getCourseStatus().equals("Dropped")){
            spnr.setSelection(2);
        }
        if(course.getCourseStatus().equals("Plan To Take")){
            spnr.setSelection(3);
        }
    }

    public void getAndSetViewsById() {
        editTextName = findViewById(R.id.add_course_name);
        editStartDate = findViewById(R.id.add_course_start_date);
        editEndDate = findViewById(R.id.add_course_end_date);
        spinnerCourseStatus = findViewById(R.id.spinnerCourseStatus);
        editTextNotes = findViewById(R.id.add_course_notes);
    }

    private void updateStartLabel() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editStartDate.setText(sdf.format(calendarStart.getTime()));
    }

    private void updateEndLabel() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editEndDate.setText(sdf.format(calendarEnd.getTime()));
    }

    public void deleteInstructorHelper() {
        // swipe to delete
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
                repo.delete(instructorAdapter.getInstructorAt(viewHolder.getAdapterPosition()));
                instructorAdapter.instructorEntityList.remove(viewHolder.getAdapterPosition());
                instructorAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(CourseAddEdit.this, "Instructor Successfully Deleted",
                        Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerViewInstr);
    }


    public CourseEntity findCourse(int courseId) {
        CourseEntity currentCourse=null;
        List<CourseEntity> allCourses = repo.getAllCoursesFromRepo();
        for(CourseEntity course:allCourses){
            if(courseId==course.getCourseId()) {
                currentCourse=course;
            }
        }
        return currentCourse;
    }

    public void deleteAssessmentHelper(){
        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                repo.delete(assessmentAdapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                assessmentAdapter.assessmentEntityList.remove(viewHolder.getAdapterPosition());
                assessmentAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(CourseAddEdit.this, "Assessment Deleted",
                        Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerViewAssess);
    }

    public void setRecyclerViews(){
        if (courseId != -1){
            getSelectedCourse();
            setTitle("Edit Course");
            recyclerViewAssess = findViewById(R.id.assessment_recyclerView);
            recyclerViewInstr = findViewById(R.id.instructor_recyclerView);
            layoutManagerInstr = new LinearLayoutManager(this);
            layoutManagerAssess = new LinearLayoutManager(this);
            assessmentAdapter = new AssessmentAdapter(this);
            instructorAdapter = new InstructorAdapter(this);
            recyclerViewAssess.setLayoutManager(layoutManagerInstr);
            recyclerViewInstr.setLayoutManager(layoutManagerAssess);
            recyclerViewAssess.setAdapter(assessmentAdapter);
            recyclerViewInstr.setAdapter(instructorAdapter);
            assessmentAdapter.assessmentSetter(assessmentEntityList);
            instructorAdapter.instructorSetter(instructorEntityList);
        } else {
            findViewById(R.id.instructorText).setVisibility(View.INVISIBLE);
            findViewById(R.id.assessmentText).setVisibility(View.INVISIBLE);
        }
    }
}