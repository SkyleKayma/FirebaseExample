package fr.skyle.firebaseexample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fr.skyle.firebaseexample.R;
import fr.skyle.firebaseexample.adapter.AdapterArticles;
import fr.skyle.firebaseexample.model.Article;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;

    private DatabaseReference mDatabase;
    private AdapterArticles mAdapter;

    private ArrayList<Article> articles;

    // --- LifeCycle

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupération des vues
        mRecyclerView = findViewById(R.id.recyclerViewActivityMainArticles);
        mFab = findViewById(R.id.fabActivityMainAddArticles);

        // Récupération de la database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initAdapter();
        setListeners();
    }

    // --- Other methods

    private void initAdapter() {
        mAdapter = new AdapterArticles(new ArrayList<>(), new AdapterArticles.AdapterCallback() {
            @Override
            public void onMethodCallback(String key) {
                // Suppression d'un article
                mDatabase.child("articles").child(key).removeValue();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListeners() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityAddArticle.class);
                startActivity(intent);
            }
        });

        ValueEventListener listChangeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                articles = new ArrayList<>();

                // On itère sur les objets de la liste
                // Nos objets sont des Article
                // Nous avons besoin de récupérer la clé que Firebase Database a généré si on veut pouvoir interragir avec l'article plus tard
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Article article = childSnapshot.getValue(Article.class);
                    article.setKey(childSnapshot.getKey());

                    articles.add(article);
                }

                mAdapter.refreshData(articles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Erreur pendant que l'on écoute les changements
            }
        };
        mDatabase.child("articles").addValueEventListener(listChangeListener);
    }
}
