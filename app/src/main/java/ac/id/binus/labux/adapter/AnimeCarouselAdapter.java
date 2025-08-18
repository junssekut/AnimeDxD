package ac.id.binus.labux.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ac.id.binus.labux.R;
import ac.id.binus.labux.model.Anime;

public class AnimeCarouselAdapter extends RecyclerView.Adapter<AnimeCarouselAdapter.CarouselViewHolder> {

    private Context context;
    private List<Anime> animeList;

    public AnimeCarouselAdapter(Context context, List<Anime> animeList) {
        this.context = context;
        this.animeList = animeList;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anime_carousel, parent, false);

        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        Anime anime = animeList.get(position);

        // Set anime title and genre
        holder.animeTitle.setText(anime.getTitle());
        holder.animeGenre.setText(anime.getGenre());

        // Load anime image using wide image resource
        int imageResId = context.getResources().getIdentifier(anime.getWideImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.animeImage.setImageResource(imageResId);
        } else {
            holder.animeImage.setImageResource(R.drawable.featured_anime);
        }

        // Set click listener for the entire carousel item
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("dataType", "anime");
            bundle.putString("animeTitle", anime.getTitle());
            bundle.putString("animeGenre", anime.getGenre());
            bundle.putString("animeImage", anime.getImageResource());
            bundle.putFloat("animeRating", anime.getRating());
            
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView animeImage;
        TextView animeTitle;
        TextView animeGenre;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            animeImage = itemView.findViewById(R.id.carousel_image);
            animeTitle = itemView.findViewById(R.id.carousel_title);
            animeGenre = itemView.findViewById(R.id.carousel_genre);
        }
    }
}
