package com.example.googlekeep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class CreateNote extends AppCompatActivity {
    ImageView backButton;
    String noteDescription, noteTitle;
    EditText description, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        backButton = findViewById(R.id.backButton);
        description = findViewById(R.id.noteDescription);
        title = findViewById(R.id.title);


        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            noteTitle = getIntent().getStringExtra("title");
            noteDescription = getIntent().getStringExtra("desc");
            setViewOrUpdateNote();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setViewOrUpdateNote() {
        title.setText(noteTitle);
        description.setText(noteDescription);
    }
}