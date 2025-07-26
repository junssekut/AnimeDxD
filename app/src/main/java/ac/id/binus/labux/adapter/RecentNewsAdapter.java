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
import ac.id.binus.labux.model.News;

public class RecentNewsAdapter extends RecyclerView.Adapter<RecentNewsAdapter.ViewHolder> {
    private List<News> newsList;
    private Context context;

    public RecentNewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        holder.newsDescription.setText(news.getDescription());
        holder.newsDate.setText(news.getDate());

        // Set image using resource name with .jpeg extension
        int imageResId = context.getResources().getIdentifier(news.getImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.newsImage.setImageResource(imageResId);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView newsTitle;
        TextView newsDescription;
        TextView newsDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);
            newsDate = itemView.findViewById(R.id.news_date);
        }
    }
}
