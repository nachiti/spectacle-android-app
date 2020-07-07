package com.example.spectacleapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.R;
import com.example.spectacleapp.models.Commentaire;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentaireAdapter extends RecyclerView.Adapter<CommentaireAdapter.CommentaireViewHolder> {

    private List<Commentaire> commentaireList;

    public CommentaireAdapter(List<Commentaire> commentaireList) {
        this.commentaireList = commentaireList;
    }

    @NonNull
    @Override
    public CommentaireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.detail_layer_item_commentaire,parent,false);
        return new CommentaireViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentaireViewHolder holder, int position) {
        holder.display(commentaireList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentaireList.size();
    }


    class CommentaireViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPseudo;
        private RatingBar ratingBarNote;
        private TextView textViewDate;
        private TextView textViewTexte;

        CommentaireViewHolder(@NonNull View itemView) {
            super(itemView);
             textViewPseudo = itemView.findViewById(R.id.tv_c_pseudo);
             ratingBarNote = itemView.findViewById(R.id.rb_c_note);
             textViewDate = itemView.findViewById(R.id.tv_c_date);
             textViewTexte = itemView.findViewById(R.id.tv_c_texte);
        }

        void display(Commentaire commentaire){

            textViewPseudo.setText(commentaire.getPseudonyme());

            Date dateHeure = commentaire.getDate();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            textViewDate.setText(format.format(dateHeure));

            textViewTexte.setText(commentaire.getTexte());
        }
    }
}
