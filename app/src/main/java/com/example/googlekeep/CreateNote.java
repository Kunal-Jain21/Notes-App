package com.example.googlekeep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;

public class CreateNote extends AppCompatActivity {
    ImageView backButton, pin;
    String noteDescription, noteTitle;
    EditText description, title;
    String docId;
    Boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        backButton = findViewById(R.id.backButton);
        description = findViewById(R.id.noteDescription);
        title = findViewById(R.id.title);
        pin = findViewById(R.id.pin_btn);

        pin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(docId);
                documentReference.delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateNote.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateNote.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        isEditMode = getIntent().getBooleanExtra("isEdited", false);
        if (isEditMode) {
            docId = getIntent().getStringExtra("DocId");
            noteTitle = getIntent().getStringExtra("title");
            noteDescription = getIntent().getStringExtra("desc");
            setViewOrUpdateNote();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteTitle = title.getText().toString();
                noteDescription = description.getText().toString();
                if (noteTitle == null || noteTitle.isEmpty()) {
                    finish();
                    return;
                }
                Notes note = new Notes();
                note.setNoteTitle(noteTitle);
                note.setNoteDesc(noteDescription);
                saveNoteToFirebase(note);
            }
        });
    }

    private void saveNoteToFirebase(Notes note) {
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // note is added
                String toastText="";
                if (!docId.isEmpty())
                    toastText = "Note updated successfully";
                else
                    toastText = "Note added successfully";
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed while adding note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViewOrUpdateNote() {
        title.setText(noteTitle);
        description.setText(noteDescription);
    }
}