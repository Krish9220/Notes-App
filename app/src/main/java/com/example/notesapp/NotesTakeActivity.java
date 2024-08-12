package com.example.notesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notesapp.Model.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakeActivity extends AppCompatActivity {

    EditText title_et,note_et;
    ImageView save;
    Notes notes;
    boolean isOldNotes = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_take);

        title_et = findViewById(R.id.title_et);
        note_et = findViewById(R.id.note_et);
        save = findViewById(R.id.saveBtn);

        notes = new Notes();

        try{
            notes = (Notes) getIntent().getSerializableExtra("old_notes");
            title_et.setText(notes.getTitle());
            note_et.setText(notes.getNotes());
            isOldNotes = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isOldNotes)
                {
                    notes = new Notes();
                }
                String title = title_et.getText().toString();
                String note = note_et.getText().toString();

                if(note.isEmpty())
                {
                    Toast.makeText(NotesTakeActivity.this, "Please note something", Toast.LENGTH_LONG).show();
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("EEE , d MMM YYYY HH:mm a");

                Date date = new Date();

                notes.setTitle(title);
                notes.setNotes(note);
                notes.setDate(format.format(date));

                Intent intent = new Intent();
                intent.putExtra("note",notes);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }

        });
    }
}