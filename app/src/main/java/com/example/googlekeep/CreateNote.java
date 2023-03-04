package com.example.googlekeep;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;

public class CreateNote extends AppCompatActivity {
    ImageView backButton, pin, archiveButton;
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
        archiveButton = findViewById(R.id.archiveButton);

        pin.setOnClickListener(new View.OnClickListener() {
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

        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                archiveNote();
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
                saveNote();
            }
        });
    }

    private void saveNote() {
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

    private void archiveNote() {
        noteTitle = title.getText().toString();
        noteDescription = description.getText().toString();
        if (noteTitle == null || noteTitle.isEmpty()) {
            finish();
            return;
        }
        Notes note = new Notes();
        note.setNoteTitle(noteTitle);
        note.setNoteDesc(noteDescription);
        archiveNoteToFirebase(note);
    }

    private void archiveNoteToFirebase(Notes note) {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForArchive().document();
        if (isEditMode){
            DocumentReference documentReference2 = Utility.getCollectionReferenceForNotes().document(docId);
            documentReference2.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateNote.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    documentReference.set(note).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            // note is added
                            String toastText = "Note archived successfully";
                            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Failed while archiving note", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateNote.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            documentReference.set(note).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    // note is added
                    String toastText = "Note archived successfully";
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed while archiving note", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private void saveNoteToFirebase(Notes note) {
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // note is added
                String toastText = "";
                if (isEditMode)
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }
}