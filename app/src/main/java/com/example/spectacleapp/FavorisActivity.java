package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.spectacleapp.adapters.FavorisAdapter;
import com.example.spectacleapp.models.Spectacle;

import java.util.List;

public class FavorisActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavoris;
    private FavorisAdapter adapterFavoris;
    private RecyclerView.LayoutManager layoutManagerFavoris;
    private List<Spectacle> spectacleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        recyclerViewFavoris = findViewById(R.id.rv_favoris);
        recyclerViewFavoris.setHasFixedSize(true);
        layoutManagerFavoris = new LinearLayoutManager(this);
        adapterFavoris = new FavorisAdapter(spectacleList);

        recyclerViewFavoris.setLayoutManager(layoutManagerFavoris);
        recyclerViewFavoris.setAdapter(adapterFavoris);

        adapterFavoris.setOnItemClickListener(new FavorisAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // faire appel à l'activity detail spectacle
               String id = spectacleList.get(position).getId();
                Intent intent = new Intent(getParent(),DetailActivity.class);
                intent.putExtra("spectacleId",id);
                startActivity(intent);

            }

            @Override
            public void onDeleteClick(int position) {
                //TODO remove spectacle from favoris api
                spectacleList.remove(position);
                adapterFavoris.notifyItemRemoved(position);
            }
        });
    }

}