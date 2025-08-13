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
import ac.id.binus.labux.model.MangaRelease;

public class MangaReleaseAdapter extends RecyclerView.Adapter<MangaReleaseAdapter.ViewHolder> {
    private List<MangaRelease> mangaReleaseList;
    private Context context;

    public MangaReleaseAdapter(Context context, List<MangaRelease> mangaReleaseList) {
        this.context = context;
        this.mangaReleaseList = mangaReleaseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manga_release, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MangaRelease mangaRelease = mangaReleaseList.get(position);
        holder.mangaTitle.setText(mangaRelease.getTitle());
        holder.description.setText(mangaRelease.getDescription());
        holder.chapter.setText(mangaRelease.getChapter());

        // Set image using resource name (.jpeg files)
        int imageResId = context.getResources().getIdentifier(mangaRelease.getImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.mangaImage.setImageResource(imageResId);
        }

        // Set click listener for the entire item to navigate to detail page
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("dataType", "manga");
            bundle.putString("mangaTitle", mangaRelease.getTitle());
            bundle.putString("mangaChapter", mangaRelease.getChapter());
            bundle.putString("mangaDescription", mangaRelease.getDescription());
            bundle.putString("mangaImage", mangaRelease.getImageResource());
            
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return mangaReleaseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mangaImage;
        TextView mangaTitle;
        TextView description;
        TextView chapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mangaImage = itemView.findViewById(R.id.manga_image);
            mangaTitle = itemView.findViewById(R.id.manga_title);
            description = itemView.findViewById(R.id.description);
            chapter = itemView.findViewById(R.id.chapter);
        }
    }
}
