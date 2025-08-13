package ac.id.binus.labux;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ac.id.binus.labux.databinding.FragmentDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get arguments passed from previous fragment
        if (getArguments() != null) {
            String dataType = getArguments().getString("dataType");
            
            if ("news".equals(dataType)) {
                // Handle news data
                String newsTitle = getArguments().getString("newsTitle");
                String newsGenre = getArguments().getString("newsGenre");
                String newsSynopsis = getArguments().getString("newsSynopsis");
                String newsImage = getArguments().getString("newsImage");
                
                // Debug: Log the received data
                android.util.Log.d("DetailFragment", "Received data:");
                android.util.Log.d("DetailFragment", "Title: " + newsTitle);
                android.util.Log.d("DetailFragment", "Genre: " + newsGenre);
                android.util.Log.d("DetailFragment", "Synopsis: " + newsSynopsis);
                android.util.Log.d("DetailFragment", "Image: " + newsImage);
                
                // Set the data to views
                TextView titleView = view.findViewById(R.id.titledets);
                TextView genreView = view.findViewById(R.id.genredets);
                TextView synopsisView = view.findViewById(R.id.syndets);
                TextView pageTitle = view.findViewById(R.id.pageTitle);
                ImageView imageView = view.findViewById(R.id.detailImage);
                
                // Set title with fallback
                String displayTitle = (newsTitle != null && !newsTitle.isEmpty()) ? newsTitle : "Unknown Title";
                if (titleView != null) titleView.setText(displayTitle);
                if (pageTitle != null) pageTitle.setText(displayTitle);
                
                // Set genre with fallback
                String displayGenre = (newsGenre != null && !newsGenre.isEmpty()) ? newsGenre : "Unknown Genre";
                if (genreView != null) genreView.setText(displayGenre);
                
                // Set synopsis with fallback
                String displaySynopsis = (newsSynopsis != null && !newsSynopsis.isEmpty()) ? newsSynopsis : "No synopsis available";
                if (synopsisView != null) synopsisView.setText(displaySynopsis);
                
                // Set image
                if (imageView != null && newsImage != null && !newsImage.isEmpty()) {
                    int imageResId = getContext().getResources().getIdentifier(newsImage, "drawable", getContext().getPackageName());
                    if (imageResId != 0) {
                        imageView.setImageResource(imageResId);
                    }
                }
            } else {
                // Handle review data (existing functionality)
                String reviewId = getArguments().getString("reviewId");
                if (reviewId != null && !reviewId.isEmpty()) {
                    binding.setReviewId(reviewId);
                }
            }
        } else {
            // No arguments received - set default values
            TextView titleView = view.findViewById(R.id.titledets);
            TextView genreView = view.findViewById(R.id.genredets);
            TextView synopsisView = view.findViewById(R.id.syndets);
            TextView pageTitle = view.findViewById(R.id.pageTitle);
            
            if (titleView != null) titleView.setText("No Title Available");
            if (genreView != null) genreView.setText("No Genre Available");
            if (synopsisView != null) synopsisView.setText("No synopsis available");
            if (pageTitle != null) pageTitle.setText("DETAIL");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Setup back button
        ImageView backButton = view.findViewById(R.id.btn_back);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                Navigation.findNavController(v).navigateUp();
            });
        }

        // Setup Post Review button click
        FrameLayout postReviewFrame = view.findViewById(R.id.post_review_frame);
        
        if (postReviewFrame != null) {
            postReviewFrame.setOnClickListener(v -> showReviewPopup(v));
        }
    }

    private void showReviewPopup(View anchorView) {
        // Inflate the popup layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View popupView = inflater.inflate(R.layout.popup_review, null);

        // Calculate popup width (80% of screen width)
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int popupWidth = (int) (screenWidth * 0.85); // 85% of screen width
        
        // Create popup window with calculated width
        PopupWindow popupWindow = new PopupWindow(
            popupView,
            popupWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        );

        // Set up popup properties
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(10.0f);

        // Create overlay view for dark background
        View overlayView = new View(getContext());
        overlayView.setBackgroundColor(0x80000000); // Semi-transparent black (50% opacity)
        overlayView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Add overlay to the root view
        ViewGroup rootView = (ViewGroup) getActivity().findViewById(android.R.id.content);
        rootView.addView(overlayView);

        // Set transparent background for popup window itself
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));

        // Get views from popup
        EditText etReview = popupView.findViewById(R.id.et_review);
        Button btnPost = popupView.findViewById(R.id.btn_post_review);
        ImageView btnClose = popupView.findViewById(R.id.btn_close_popup);
        TextView tvErrorMessage = popupView.findViewById(R.id.tv_error_message);

        // Set up close button
        btnClose.setOnClickListener(v -> {
            popupWindow.dismiss();
            rootView.removeView(overlayView); // Remove overlay when closing
        });

        // Set up post button
        btnPost.setOnClickListener(v -> {
            String reviewText = etReview.getText().toString().trim();
            
            // Simple validation: just check if empty
            if (reviewText.isEmpty()) {
                tvErrorMessage.setVisibility(View.VISIBLE);
                return;
            }
            
            // Hide error message if validation passes
            tvErrorMessage.setVisibility(View.GONE);
            
            // Here you can handle the review submission
            Toast.makeText(getContext(), "Review posted successfully!", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
            rootView.removeView(overlayView); // Remove overlay when closing
        });

        // Set up dismiss listener to remove overlay
        popupWindow.setOnDismissListener(() -> {
            if (overlayView.getParent() != null) {
                rootView.removeView(overlayView);
            }
        });

        // Show popup at center of screen
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }
}