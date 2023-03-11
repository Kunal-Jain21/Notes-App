package com.example.googlekeep;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DeleteFragment extends Fragment implements NotesListener{

    RecyclerView deleteRecycler;
    private NoteAdapter recyclerAdapter;
    ArrayList<Notes> deleteArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Deleted");
        deleteRecycler = view.findViewById(R.id.deleteRecycler);
        deleteArrayList = new ArrayList<>();
        setNotesRecycler();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteForever: {
                deleteAll();
                break;
            }
            case R.id.restore: {
                restoreAll();
                break;
            }
        }
        return true;
    }

    private void restoreAll() {
        Utility.getCollectionReferenceForDeleted().get().addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document();
                documentReference.set(documentSnapshot.toObject(Notes.class)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.v("testing", "Done");
                    }
                });
            }
            deleteAll();
        });
    }

    private void deleteAll() {
        Utility.getCollectionReferenceForDeleted().get().addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                Utility.getCollectionReferenceForDeleted().document(documentSnapshot.getId())
                        .delete().addOnCompleteListener(task -> {
                            if (task.isComplete()){
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        });
                recyclerAdapter.notesArrayList.clear();
            }
            Toast.makeText(requireActivity(), "Deleted ALL", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.item_selected_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setNotesRecycler() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        deleteRecycler.setLayoutManager(staggeredGridLayoutManager);
        recyclerAdapter = new NoteAdapter(requireActivity(), deleteArrayList, this);
        deleteRecycler.setAdapter(recyclerAdapter);
    }

    private void setData() {
        deleteArrayList.clear();
        Utility.getCollectionReferenceForDeleted().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        return;
                    } else {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Notes note = documentSnapshot.toObject(Notes.class);
                            note.setDocumentId(documentSnapshot.getId());
                            deleteArrayList.add(note);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Error in getting data", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onNoteClicked(Notes notes, int position) {

    }

    @Override
    public void onLongClickMenu(boolean isSelected) {
        boolean abc = isSelected;
    }
}