package com.example.mobileapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mobileapp.Database.Repository;
import com.example.mobileapp.Entity.CourseEntity;
import com.example.mobileapp.Entity.TermEntity;
import com.example.mobileapp.R;

import java.util.List;

public class TermList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TermAdapter termAdapter;
    //
    private RecyclerView.LayoutManager rvLayoutManager;
    Repository repo;
    public List<TermEntity> termEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        repo = new Repository(getApplication());

        getTermsList();
        buildRecyclerView();

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
                for (CourseEntity course: repo.getAllCoursesFromRepo()) {
                    if (course.getCourseTermId() == termAdapter.getTermAt(viewHolder.getAdapterPosition()).getTermId())
                        if (termAdapter.getTermAt(viewHolder.getAdapterPosition()).getTermId() == course.getCourseTermId()) {
                            termAdapter.notifyDataSetChanged();
                            Toast.makeText(TermList.this, "Cannot delete term which has associated courses.", Toast.LENGTH_LONG).show();
                            return;
                        }
                }
                repo.delete(termAdapter.getTermAt(viewHolder.getAdapterPosition()));
                termAdapter.mTermsList.remove(viewHolder.getAdapterPosition());
                termAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(TermList.this, "Term Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void getTermsList() {
        termEntityList = repo.getAllTermsFromRepo();
    }
    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_terms);
        recyclerView.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(this);
        termAdapter = new TermAdapter(this);
        recyclerView.setLayoutManager(rvLayoutManager);
        recyclerView.setAdapter(termAdapter);
        termAdapter.termsSetter(repo.getAllTermsFromRepo());
    }
    public void addTermOnClick(View view) {
        Intent intent = new Intent(this, TermAdd.class);
        startActivity(intent);
    }

    // makes the back button function
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}