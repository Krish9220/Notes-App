package com.example.notesapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Interface.NotesClickListener;
import com.example.notesapp.Model.Notes;
import com.example.notesapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteListAdapter extends RecyclerView.Adapter<NotesViewHolder> {


    Context context;
    List<Notes> notesList;
    NotesClickListener listener;

    public NoteListAdapter(Context context, List<Notes> notesList, NotesClickListener listener) {
        this.context = context;
        this.notesList = notesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.title.setText(notesList.get(position).getTitle());
        holder.date.setText(notesList.get(position).getDate());
        holder.notes.setText(notesList.get(position).getNotes());
        holder.date.setSelected(true);

        if (notesList.get(position).getPinned()) {
            holder.pin.setImageResource(R.drawable.pin);
        } else {
            holder.pin.setImageResource(0);
        }

        //setting up the random color to the notes Cards.
        int color_code = getRandomColor();
        holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code));

        //set upping the on note click and on note long press.
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(notesList.get(holder.getAdapterPosition()));
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                listener.onLongPress(notesList.get(holder.getAdapterPosition()), holder.cardView);
                return true;
            }
        });
    }

    //to add random color to the cardView.
    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);
        colorCode.add(R.color.color7);
        colorCode.add(R.color.color8);
        colorCode.add(R.color.color9);
        colorCode.add(R.color.color10);
        colorCode.add(R.color.color11);
        colorCode.add(R.color.color12);

        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());

        return colorCode.get(random_color);
    }


    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void filterList(List<Notes> filterList)//filterList to change to filter may be
    {
        notesList = filterList;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView title, date, notes;
    ImageView pin;


    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.note_container);
        title = itemView.findViewById(R.id.title_tv);
        date = itemView.findViewById(R.id.date_tv);
        notes = itemView.findViewById(R.id.notes_tv);
        pin = itemView.findViewById(R.id.pin_iv);
    }
}
