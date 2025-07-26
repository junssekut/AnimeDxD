package ac.id.binus.labux.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
        holder.animeTitle.setText(anime.getTitle());
        holder.animeGenre.setText(anime.getGenre());

        // Set image using resource name (.jpeg files)
        int imageResId = context.getResources().getIdentifier(anime.getImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.animeImage.setImageResource(imageResId);
        }
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView animeImage;
        TextView animeTitle;
        TextView animeGenre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            animeImage = itemView.findViewById(R.id.anime_image);
            animeTitle = itemView.findViewById(R.id.anime_title);
            animeGenre = itemView.findViewById(R.id.anime_genre);
        }
    }
}
