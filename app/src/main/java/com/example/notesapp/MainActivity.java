package com.example.notesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notesapp.Adapter.NoteListAdapter;
import com.example.notesapp.Database.RoomDB;
import com.example.notesapp.Interface.NotesClickListener;
import com.example.notesapp.Model.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private static final int NOTES_TAKE_CODE =101;
    private static final int NOTES_EDIT_CODE =102;
    RecyclerView recyclerView;
    NoteListAdapter noteListAdapter;
    RoomDB database;
    List<Notes> notes;
    FloatingActionButton addBtn;
    SearchView searchView;
    Notes selectedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.noteRv);
        notes = new ArrayList<>();
        addBtn = findViewById(R.id.addBtn);
        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();
        searchView = findViewById(R.id.search_view);

        updateRecycle(notes);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,NotesTakeActivity.class);
                startActivityForResult(i,NOTES_TAKE_CODE);
            }
        });

//        handing the searching bar.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newtext)
    {
        List<Notes>filerList = new ArrayList<>();
        for(Notes singleNote : notes)
        {
            //checking if the notes title or description matches to the search.
            if(singleNote.getTitle().toLowerCase().contains(newtext.toLowerCase())
                || singleNote.getNotes().toLowerCase().contains(newtext.toLowerCase()))
            {
                filerList.add(singleNote);
            }
        }

        noteListAdapter.filterList(filerList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NOTES_TAKE_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                noteListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == NOTES_EDIT_CODE)//update and edit the notes if note is clicked.
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Notes new_notes = (Notes) data.getSerializableExtra("note");//notes_old
                database.mainDAO().update(new_notes.getID(),new_notes.getTitle(),new_notes.getNotes());//change new_notes.getNotes() to .getTitle();
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                noteListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycle(List<Notes> notes) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        noteListAdapter = new NoteListAdapter(MainActivity.this,notes,notesClickListener);
        recyclerView.setAdapter(noteListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {

            Intent i = new Intent(MainActivity.this, NotesTakeActivity.class);
            i.putExtra("old_notes",notes);
            startActivityForResult(i,NOTES_EDIT_CODE);
        }

        @Override
        public void onLongPress(Notes notes, CardView cardView) {
            selectedNotes = new Notes();
            selectedNotes = notes;
            showPop(cardView);
        }
    };

    private void showPop(CardView cardView)
    {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.pin)
        {
            if(selectedNotes.getPinned())
            {
                database.mainDAO().pin(selectedNotes.getID(),false);
                Toast.makeText(this, "UnPinned", Toast.LENGTH_SHORT).show();
            }
            else
            {
                database.mainDAO().pin(selectedNotes.getID(),true);
                Toast.makeText(this, "Pinned", Toast.LENGTH_SHORT).show();
            }

            notes.clear();
            notes.addAll(database.mainDAO().getAll());
            noteListAdapter.notifyDataSetChanged();
            return true;
        }
        else if(item.getItemId() == R.id.delete)
        {
            database.mainDAO().delete(selectedNotes);
            notes.remove(selectedNotes);
            noteListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Notes is Deleted Successfully...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}

