package ac.id.binus.labux.adapter;

import android.content.Context;
import android.content.Intent;
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
import ac.id.binus.labux.detail_page;
import ac.id.binus.labux.model.Review;

public class LatestReviewAdapter extends RecyclerView.Adapter<LatestReviewAdapter.ViewHolder> {
    private List<Review> reviewList;
    private Context context;

//    private NavController navController;

    public LatestReviewAdapter(Context context, List<Review> reviewList) {
//        this.navController = navController;
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_latest_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.reviewTitle.setText(review.getTitle());
        holder.reviewSubtitle.setText(review.getSubtitle());
        holder.reviewDescription.setText(review.getDescription());

        // Set rating stars
        String stars = "★".repeat(review.getRating()) + "☆".repeat(5 - review.getRating());
        holder.reviewRating.setText(stars);

        // Set image using resource name (.jpeg files)
        int imageResId = context.getResources().getIdentifier(review.getImageResource(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.reviewImage.setImageResource(imageResId);
        }

        // Set click listener for the entire item to navigate to detail page
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("dataType", "review");
            bundle.putString("reviewTitle", review.getTitle());
            bundle.putString("reviewSubtitle", review.getSubtitle());
            bundle.putString("reviewDescription", review.getDescription());
            bundle.putString("reviewImage", review.getImageResource());
            bundle.putInt("reviewRating", review.getRating());
            
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
        });

        // Set click listener for the image as well
        holder.reviewImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("dataType", "review");
            bundle.putString("reviewTitle", review.getTitle());
            bundle.putString("reviewSubtitle", review.getSubtitle());
            bundle.putString("reviewDescription", review.getDescription());
            bundle.putString("reviewImage", review.getImageResource());
            bundle.putInt("reviewRating", review.getRating());
            
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView reviewImage;
        TextView reviewTitle;
        TextView reviewSubtitle;
        TextView reviewDescription;
        TextView reviewRating;
        TextView replyButton;
        TextView shareButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewImage = itemView.findViewById(R.id.review_image);
            reviewTitle = itemView.findViewById(R.id.review_title);
            reviewSubtitle = itemView.findViewById(R.id.review_subtitle);
            reviewDescription = itemView.findViewById(R.id.review_description);
            reviewRating = itemView.findViewById(R.id.review_rating);
            replyButton = itemView.findViewById(R.id.reply_button);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }
}
