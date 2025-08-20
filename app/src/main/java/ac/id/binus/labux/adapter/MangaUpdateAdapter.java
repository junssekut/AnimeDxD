package ac.id.binus.labux.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ac.id.binus.labux.DetailFragment;
import ac.id.binus.labux.R;
import ac.id.binus.labux.model.MangaUpdate;

public class MangaUpdateAdapter extends RecyclerView.Adapter<MangaUpdateAdapter.MangaUpdateViewHolder> {
    private final List<MangaUpdate> mangaList;
    private final Context context;
    private final boolean showDate; // Flag to control date visibility

    public MangaUpdateAdapter(Context context, List<MangaUpdate> mangaList, boolean showDate) {
        this.context = context;
        this.mangaList = mangaList;
        this.showDate = showDate; // Show date only on home page, hide in manga tab
    }

    @NonNull
    @Override
    public MangaUpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manga_update, parent, false);
        return new MangaUpdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaUpdateViewHolder holder, int position) {
        MangaUpdate manga = mangaList.get(position);

        // Set all required manga details consistently
        holder.titleTextView.setText(manga.getTitle());
//        holder.authorTextView.setText("by " + manga.getAuthor());
//        holder.ratingBar.setRating(manga.getRating());
//        holder.genresTextView.setText(manga.getGenres());
//        holder.descriptionTextView.setText(manga.getDescription());
//        holder.chapterTextView.setText("Latest: " + manga.getChapter());

        // Conditionally show/hide update date based on context
        if (showDate && manga.getUpdateDate() != null && !manga.getUpdateDate().isEmpty()) {
//            holder.updateDateTextView.setVisibility(View.VISIBLE);
//            holder.updateDateTextView.setText(manga.getUpdateDate());
        } else {
//            holder.updateDateTextView.setVisibility(View.GONE);
        }

        // Set image using resource name
        int imageResId = context.getResources().getIdentifier(
                manga.getImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.imageView.setImageResource(imageResId);
        }

        // Set click listener to navigate to manga detail page with complete manga information
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("dataType", "manga");
            bundle.putString("mangaTitle", manga.getTitle());
            bundle.putString("mangaAuthor", manga.getAuthor());
            bundle.putString("mangaDescription", manga.getDescription());
            bundle.putString("mangaGenre", manga.getGenres());
            bundle.putFloat("mangaRating", manga.getRating());
            bundle.putString("mangaChapter", manga.getChapter());
            bundle.putString("mangaUpdateDate", manga.getUpdateDate());
            bundle.putInt("mangaImage", imageResId);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            FragmentActivity activity = (FragmentActivity) context;
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    public static class MangaUpdateViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
//        TextView authorTextView;
//        TextView genresTextView;
//        TextView descriptionTextView;
//        TextView chapterTextView;
//        TextView updateDateTextView;
//        RatingBar ratingBar;
        ImageView imageView;

        public MangaUpdateViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.manga_title);
//            authorTextView = itemView.findViewById(R.id.manga_author);
//            genresTextView = itemView.findViewById(R.id.manga_genres);
//            descriptionTextView = itemView.findViewById(R.id.manga_description);
//            chapterTextView = itemView.findViewById(R.id.manga_chapter);
//            updateDateTextView = itemView.findViewById(R.id.manga_update_date);
//            ratingBar = itemView.findViewById(R.id.manga_rating);
            imageView = itemView.findViewById(R.id.manga_image);
        }
    }
}
