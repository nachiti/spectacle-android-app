package com.example.spectacleapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.R;
import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.service.NetworkService;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FavorisAdapter extends RecyclerView.Adapter<FavorisAdapter.FavorisViewHolder> {

    private List<Spectacle> spectaclesFavoris;
    private OnItemClickListener favorisListener;

    public FavorisAdapter(){
    }

    public void setSpectaclesFavoris(List<Spectacle> spectaclesFavoris) {
        this.spectaclesFavoris = spectaclesFavoris;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        favorisListener = listener;
    }

    public static class FavorisViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewImage;
        public TextView textViewTitre;
        public TextView textViewType;
        public TextView textViewAdresse;
        public TextView textViewDate;
        public TextView textViewHeure;
        public TextView textViewPrix;
        public Button buttonDelete;

        public FavorisViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageViewImage = itemView.findViewById(R.id.favoris_image);
            textViewTitre = itemView.findViewById(R.id.favoris_titre);
            textViewType = itemView.findViewById(R.id.favoris_type);
            textViewAdresse = itemView.findViewById(R.id.favoris_adresse);
            textViewDate = itemView.findViewById(R.id.favoris_date);
            textViewHeure = itemView.findViewById(R.id.favoris_heure);
            textViewPrix = itemView.findViewById(R.id.favoris_prix);
            buttonDelete = itemView.findViewById(R.id.favoris_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public FavorisAdapter(List<Spectacle> spectacles){
        spectaclesFavoris = spectacles;
    }

    @NonNull
    @Override
    public FavorisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favoris_layer_item,parent,false);
        FavorisViewHolder favorisViewHolder = new FavorisViewHolder(v, favorisListener);
        return favorisViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavorisViewHolder holder, int position) {
        Spectacle currentSpectacle = spectaclesFavoris.get(position);

        List<String> images = currentSpectacle.getPhotosUrl();
        String imageUri = NetworkService.API_IMAGE + images.get(0);
        Picasso.get().load(imageUri).into(holder.imageViewImage);
        holder.textViewTitre.setText(currentSpectacle.getTitre());
        holder.textViewType.setText(String.valueOf(currentSpectacle.getTypeSpectacle()));
        holder.textViewAdresse.setText(currentSpectacle.getAdresse());
        Date dateHeure = currentSpectacle.getDateHeure();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String parseDateHeure = format.format(dateHeure);
        String[] newDateHeure = parseDateHeure.toString().split(" ");
        holder.textViewDate.setText(newDateHeure[0]);
        holder.textViewHeure.setText(newDateHeure[1]);
        double prix = currentSpectacle.getPrix();
        if (prix == 0) {
            holder.textViewPrix.setText( "Gratuit");
        } else {
            holder.textViewPrix.setText(prix + " €");
        }

    }

    @Override
    public int getItemCount() {
        return spectaclesFavoris.size();
    }

}
