package com.example.googlekeep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;

public class ArchiveEdit extends AppCompatActivity {

    ImageView backButton, archiveDeleteBtn, unArchiveBtn;
    EditText archiveDescription, archiveTitle;
    String description, title;
    String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_edit);

        backButton = findViewById(R.id.archiveBackButton);
        archiveDescription = findViewById(R.id.archiveDescription);
        archiveTitle = findViewById(R.id.archiveTitle);
        archiveDeleteBtn = findViewById(R.id.archiveDeleteBtn);
        unArchiveBtn = findViewById(R.id.unarchiveBtn);

        archiveDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyNote(3);
            }
        });

        unArchiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyNote(2);
            }
        });


        docId = getIntent().getStringExtra("DocId");
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("desc");
        setViewOrUpdateNote();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyNote(1);
            }
        });
    }

    private void verifyNote(int methodCode) {

        title = archiveTitle.getText().toString();
        description = archiveDescription.getText().toString();
        if (title == null || title.trim().isEmpty()) {
            finish();
            return;
        }
        Notes note = new Notes();
        note.setNoteTitle(title);
        note.setNoteDesc(description);
        if (methodCode == 1) {
            saveArchiveNoteToFirebase(note);
        }
        else if (methodCode == 2) {
            unArchiveNoteToFirebase(note);
        } else if (methodCode == 3) {
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

        DocumentReference documentReference = Utility.getCollectionReferenceForDeleted().document();

        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.getCollectionReferenceForArchive().document(docId).delete().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        finish();
                        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void unArchiveNoteToFirebase(Notes note) {

        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document();
        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.getCollectionReferenceForArchive().document(docId).delete().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        finish();
                        Toast.makeText(this, "Note Unarchived", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void saveArchiveNoteToFirebase(Notes note) {
        DocumentReference documentReference= Utility.getCollectionReferenceForArchive().document(docId);

        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // note is added
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed while adding note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViewOrUpdateNote() {
        archiveTitle.setText(title);
        archiveDescription.setText(description);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        verifyNote(1);
    }
}