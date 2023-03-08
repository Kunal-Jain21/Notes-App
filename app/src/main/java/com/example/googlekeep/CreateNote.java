package com.example.googlekeep;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;

public class CreateNote extends AppCompatActivity {
    ImageView backButton, deleteBtn, archiveBtn;
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
        deleteBtn = findViewById(R.id.deleteBtn);
        archiveBtn = findViewById(R.id.archiveBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("tesing", "inside delete");
                verifyNote(3);
            }
        });

        archiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("tesing", "inside archive");
                verifyNote(2);
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
                verifyNote(1);
            }
        });
    }

    private void verifyNote(int methodCode) {

        noteTitle = title.getText().toString();
        noteDescription = description.getText().toString();
        if (noteTitle == null || noteTitle.trim().isEmpty()) {
            finish();
            return;
        }
        Notes note = new Notes();
        note.setNoteTitle(noteTitle);
        note.setNoteDesc(noteDescription);
        if (methodCode == 1) {
            saveNoteToFirebase(note);
        }else if (methodCode == 2) {
            archiveNoteToFirebase(note);
        }else if (methodCode == 3) {
            deleteNoteToFirebase(note);
        }
//        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(docId);
//
//        documentReference.delete().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(CreateNote.this, "Note Deleted", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(CreateNote.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void deleteNoteToFirebase(Notes note) {
        DocumentReference deleteDocument;
        deleteDocument = Utility.getCollectionReferenceForDeleted().document();
        if (isEditMode){
            DocumentReference noteDocumentReference = Utility.getCollectionReferenceForNotes().document(docId);
            noteDocumentReference.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    deleteDocument.set(note).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            // note is added
                            String toastText = "Note Deleted successfully";
                            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Failed while Deleting note", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateNote.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            deleteDocument.set(note).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    // note is added
                    String toastText = "Note Deleted successfully";
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed while archiving note", Toast.LENGTH_SHORT).show();
                }
            });
        }

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
        Log.v("testing", "Line 171");
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
        verifyNote(1);
    }
}