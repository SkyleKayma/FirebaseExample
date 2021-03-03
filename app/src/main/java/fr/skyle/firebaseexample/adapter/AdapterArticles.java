package fr.skyle.firebaseexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.skyle.firebaseexample.R;
import fr.skyle.firebaseexample.model.Article;

public class AdapterArticles extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Article> articles;
    private AdapterCallback adapterCallback;

    // Constructor
    public AdapterArticles(ArrayList<Article> articles, AdapterCallback adapterCallback) {
        this.articles = articles;
        this.adapterCallback = adapterCallback;
    }

    // Creation des views en fonction du ViewType qui est retourné
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW_TYPE) {
            return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_badges_item_empty, parent, false));
        } else {
            return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_badges_item, parent, false));
        }
    }

    // Gestion de l'affichage
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            Article article = articles.get(position);
            ((ArticleViewHolder) holder).bindView(article, adapterCallback);
        }
    }

    // Gestion du nombre d'objet à afficher (On gère le cas liste vide)
    @Override
    public int getItemCount() {
        if (articles.size() == 0) {
            return SIZE_OF_EMPTY_ITEM;
        } else return articles.size();
    }

    // Méthode qui va nous permettre de mettre à jour la liste
    public void refreshData(ArrayList<Article> articles) {
        this.articles = articles;

        // This is needed to refresh the list
        notifyDataSetChanged();
    }

    // On renvoie le bon ViewType en fonction de ce contient la liste
    @Override
    public int getItemViewType(int position) {
        if (articles.size() == 0) {
            return EMPTY_VIEW_TYPE;
        } else return ARTICLE_VIEW_TYPE;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mDate;

        ArticleViewHolder(@NonNull View view) {
            super(view);
            mTitle = itemView.findViewById(R.id.textViewAdapterArticleTitle);
            mDate = itemView.findViewById(R.id.textViewAdapterArticleDate);
        }

        public void bindView(Article article, AdapterCallback adapterCallback) {
            mTitle.setText(article.getTitle());
            mDate.setText(article.getDate().toString());

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    adapterCallback.onMethodCallback(article.getKey());
                    return true;
                }
            });
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(@NonNull View view) {
            super(view);
        }
    }

    private static final int SIZE_OF_EMPTY_ITEM = 1;

    private static final int EMPTY_VIEW_TYPE = 0;
    private static final int ARTICLE_VIEW_TYPE = 1;

    public interface AdapterCallback {
        void onMethodCallback(String key);
    }
}