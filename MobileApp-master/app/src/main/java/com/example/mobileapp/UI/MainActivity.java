package com.example.mobileapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobileapp.Database.Repository;
import com.example.mobileapp.R;

public class MainActivity extends AppCompatActivity {

    static int alertNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repository repository = new Repository(getApplication());
        repository.getAllTermsFromRepo();

    }


    public void termButtonClick(View view) {
        Intent intent = new Intent(this,TermList.class);
        startActivity(intent);

    }


}