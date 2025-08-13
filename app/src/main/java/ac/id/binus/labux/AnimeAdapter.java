package ac.id.binus.labux;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ac.id.binus.labux.model.Anime;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {
    private Context context;
    private List<Anime> animeList;

    public AnimeAdapter(Context context, List<Anime> animeList) {
        this.context = context;
        this.animeList = animeList;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anime, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        Anime anime = animeList.get(position);
        holder.title.setText(anime.getTitle());
        holder.author.setText(anime.getAuthor());
        holder.description.setText(anime.getDescription());
        holder.rating.setText("★ " + anime.getRating());
        holder.genre.setText(anime.getGenre().toUpperCase());
        
        // Set image
        int imageResId = context.getResources().getIdentifier(
            anime.getImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.poster.setImageResource(imageResId);
        }
        
        // Set bookmark button click listener
        holder.bookmarkBtn.setOnClickListener(v -> {
            // Toggle bookmark state (you can implement actual bookmark functionality here)
            android.widget.Toast.makeText(context, "Bookmark toggled for " + anime.getTitle(), android.widget.Toast.LENGTH_SHORT).show();
        });
        
        // Set click listener to navigate to detail page
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("dataType", "anime");
            bundle.putString("animeTitle", anime.getTitle());
            bundle.putString("animeAuthor", anime.getAuthor());
            bundle.putString("animeGenre", anime.getGenre());
            bundle.putString("animeDescription", anime.getDescription());
            bundle.putString("animeImage", anime.getImageResource());
            bundle.putFloat("animeRating", anime.getRating());
            
            Navigation.findNavController(v).navigate(R.id.action_animeListFragment_to_detailFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, description, rating, genre;
        ImageView poster, bookmarkBtn;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            author = itemView.findViewById(R.id.textAuthor);
            description = itemView.findViewById(R.id.textDescription);
            rating = itemView.findViewById(R.id.textRating);
            genre = itemView.findViewById(R.id.textGenre);
            poster = itemView.findViewById(R.id.imagePoster);
            bookmarkBtn = itemView.findViewById(R.id.btn_bookmark);
        }
    }
}
