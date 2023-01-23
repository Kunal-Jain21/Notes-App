package com.example.googlekeep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    private RecyclerView notesRecycler;
    private NoteAdapter noteAdapter;
    private FloatingActionButton createNoteFab;
    private EditText search;
    ArrayList<Notes> notesArrayList = new ArrayList<>();
    ImageView profile;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        notesRecycler = findViewById(R.id.notes_recycler);
        createNoteFab = findViewById(R.id.fab_btn);
        search = findViewById(R.id.search);
        profile = findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignIn.class));
                finish();
            }
        });

        // for removing keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        // Setting Custom Action Bar
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notes: {
                        Toast.makeText(MainActivity.this, "Notes Activity", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.archive: {
                        Toast.makeText(MainActivity.this, "Archive Activity", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.delete: {
                        Toast.makeText(MainActivity.this, "Delete Activity", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        createNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNoteIntent = new Intent(MainActivity.this, CreateNote.class);
                startActivity(createNoteIntent);
            }
        });

        // Recycler View
        setNotesRecycler();


    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }

    private void setData() {
        notesArrayList.clear();
        Utility.getCollectionReferenceForNotes().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        return;
                    } else {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Notes note = documentSnapshot.toObject(Notes.class);
                            note.setDocumentId(documentSnapshot.getId());
                            notesArrayList.add(note);
                        }
                        noteAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error in getting data", Toast.LENGTH_SHORT).show();
                });
    }

    private void setNotesRecycler() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecycler.setLayoutManager(staggeredGridLayoutManager);
        noteAdapter = new NoteAdapter(this, notesArrayList, this);
        notesRecycler.setAdapter(noteAdapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onNoteClicked(Notes notes, int position) {
        Intent intent = new Intent(MainActivity.this, CreateNote.class);
        intent.putExtra("isEdited", true);
        intent.putExtra("DocId", notes.getDocumentId());
        intent.putExtra("title", notes.getNoteTitle());
        intent.putExtra("desc", notes.getNoteDesc());
        startActivity(intent);
    }


    @Override
    public void onLongClickMenu(boolean isSelected) {
        if (isSelected){
            getSupportActionBar().hide();
        }else {
            getSupportActionBar().show();
        }
    }

//    @Override
//    public void onLongClick() {
//        isSelected = true;
//        if (selectedItems.contains(notesArrayList.get(index))){
//            holder.noteLayout.setBackgroundColor(Color.TRANSPARENT);
//            selectedItems.remove(notesArrayList.get(index));
//        }else {
//            holder.noteLayout.setBackgroundColor(Color.red(2));
//            selectedItems.add(notesArrayList.get(index));
//        }
//        if (selectedItems.size() == 0){
//            isSelected = false;
//        }
//    }
}