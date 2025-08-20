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
import ac.id.binus.labux.model.MangaUpdate;

public class MangaUpdateAdapter extends RecyclerView.Adapter<MangaUpdateAdapter.ViewHolder> {
    private List<MangaUpdate> mangaUpdateList;
    private Context context;

    public MangaUpdateAdapter(Context context, List<MangaUpdate> mangaUpdateList) {
        this.context = context;
        this.mangaUpdateList = mangaUpdateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manga_update, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MangaUpdate mangaUpdate = mangaUpdateList.get(position);
        holder.mangaTitle.setText(mangaUpdate.getTitle());
        holder.updateDate.setText(mangaUpdate.getUpdateDate());
        holder.status.setText(mangaUpdate.getStatus());

        // Set image using resource name (.jpeg files)
        int imageResId = context.getResources().getIdentifier(mangaUpdate.getImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.mangaImage.setImageResource(imageResId);
        }

        // Set click listener for the entire item to navigate to detail page
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("dataType", "manga");
            bundle.putString("mangaTitle", mangaUpdate.getTitle());
            bundle.putString("mangaStatus", mangaUpdate.getStatus());
            bundle.putString("mangaUpdateDate", mangaUpdate.getUpdateDate());
            bundle.putString("mangaImage", mangaUpdate.getImageResource());
            // Add a default description for manga updates
            bundle.putString("mangaDescription", "Latest updates for " + mangaUpdate.getTitle() + ". Check out " + mangaUpdate.getStatus() + " released on " + mangaUpdate.getUpdateDate());
            
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return mangaUpdateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mangaImage;
        TextView mangaTitle;
        TextView updateDate;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mangaImage = itemView.findViewById(R.id.manga_image);
            mangaTitle = itemView.findViewById(R.id.manga_title);
            updateDate = itemView.findViewById(R.id.update_date);
//            status = itemView.findViewById(R.id.status);
        }
    }
}
