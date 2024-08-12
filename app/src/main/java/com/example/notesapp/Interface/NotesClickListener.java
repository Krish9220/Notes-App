package com.example.notesapp.Interface;

import androidx.cardview.widget.CardView;

import com.example.notesapp.Model.Notes;

public interface NotesClickListener {

    void onClick(Notes notes);
    void onLongPress(Notes notes, CardView cardView);

}
