package com.example.googlekeep;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer_layout;
    NavigationView navigationView;
    FrameLayout frame_layout;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    ImageView profile;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frame_layout = findViewById(R.id.frame_layout);
        drawer_layout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        search = findViewById(R.id.search);
        profile = findViewById(R.id.profile);
        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignIn.class));
                finish();
            }
        });
        drawer_layout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Setting Custom Action Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);




        replaceFragment(new NotesFragment());

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.notes);
        }


        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.notes: {
                    replaceFragment(new NotesFragment());
                    Toast.makeText(this, "Notes Activity", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.archive: {
                    replaceFragment(new ArchiveFragment());
                    Toast.makeText(this, "Archive Activity", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.delete: {
                    Toast.makeText(this, "Delete Activity", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}