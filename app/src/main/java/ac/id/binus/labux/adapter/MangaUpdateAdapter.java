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
            status = itemView.findViewById(R.id.status);
        }
    }
}
