package ac.id.binus.labux.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ac.id.binus.labux.R;
import ac.id.binus.labux.model.Anime;

public class WeeklyTopAdapter extends RecyclerView.Adapter<WeeklyTopAdapter.ViewHolder> {
    private List<Anime> animeList;
    private Context context;

    public WeeklyTopAdapter(Context context, List<Anime> animeList) {
        this.context = context;
        this.animeList = animeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weekly_top, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anime anime = animeList.get(position);

        // Set basic info
        holder.animeTitle.setText(anime.getTitle());
        holder.animeGenre.setText(anime.getGenre());

        // Set image using resource name (.jpeg files)
        int imageResId = context.getResources().getIdentifier(anime.getImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.animeImage.setImageResource(imageResId);
        }

        // Set reviews count dynamically
        holder.animeReviews.setText("(" + anime.getReviewsCount() + " reviews)");

        // Set watching and episodes count dynamically
        holder.animeWatchingCount.setText(String.valueOf(anime.getWatchingCount()));
        holder.animeEpisodesCount.setText(String.valueOf(anime.getEpisodesCount()));

        // Dynamically create star rating based on anime rating
        setupStarRating(holder.starRatingLayout, anime.getRating());
    }

    private void setupStarRating(LinearLayout starContainer, float rating) {
        starContainer.removeAllViews(); // Clear existing stars

        int fullStars = (int) rating;
        boolean hasHalfStar = (rating - fullStars) >= 0.5f;
        int totalStars = 5;

        // Add full stars
        for (int i = 0; i < fullStars; i++) {
            ImageView star = createStarImageView();
            star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_filled));
            starContainer.addView(star);
        }

        // Add half star if needed
        if (hasHalfStar && fullStars < totalStars) {
            ImageView star = createStarImageView();
            star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_half));
            starContainer.addView(star);
            fullStars++;
        }

        // Add empty stars for remaining
        for (int i = fullStars; i < totalStars; i++) {
            ImageView star = createStarImageView();
            star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_empty));
            starContainer.addView(star);
        }
    }

    private ImageView createStarImageView() {
        ImageView star = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            context.getResources().getDimensionPixelSize(R.dimen.star_size),
            context.getResources().getDimensionPixelSize(R.dimen.star_size)
        );
        params.setMarginEnd(context.getResources().getDimensionPixelSize(R.dimen.star_margin));
        star.setLayoutParams(params);
        return star;
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView animeImage;
        TextView animeTitle;
        TextView animeGenre;
        TextView animeReviews;
        LinearLayout starRatingLayout;
        TextView animeWatchingCount;
        TextView animeEpisodesCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            animeImage = itemView.findViewById(R.id.anime_image);
            animeTitle = itemView.findViewById(R.id.anime_title);
            animeGenre = itemView.findViewById(R.id.anime_genre);
            animeReviews = itemView.findViewById(R.id.anime_reviews);
            starRatingLayout = itemView.findViewById(R.id.star_rating_layout);
        }
    }
}
