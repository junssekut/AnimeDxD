package ac.id.binus.labux;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
                Float newsRating = getArguments().getFloat("newsRating");
                
                // Set the data to views
                TextView titleView = view.findViewById(R.id.titledets);
                TextView genreView = view.findViewById(R.id.genredets);
                TextView synopsisView = view.findViewById(R.id.syndets);
                ImageView imageView = view.findViewById(R.id.detailImage);
                TextView typeView = view.findViewById(R.id.typedets);
                TextView authorView = view.findViewById(R.id.authordets);
                TextView ratingView = view.findViewById(R.id.ratingdets);

                // Set title with fallback
                String displayTitle = (newsTitle != null && !newsTitle.isEmpty()) ? newsTitle : "Unknown Title";
                if (titleView != null) titleView.setText(displayTitle);

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

                // Set type
                if (typeView != null) typeView.setText("News");

                // Hide author and rating for news (not applicable)
                if (authorView != null) authorView.setVisibility(View.GONE);

                String displayRating = (newsRating > 0) ? "★ " + newsRating
                        : "No rating available";
                if (ratingView != null) ratingView.setText(displayRating);

            } else if ("anime".equals(dataType)) {
                // Handle anime data
                String animeTitle = getArguments().getString("animeTitle");
                String animeAuthor = getArguments().getString("animeAuthor");
                String animeGenre = getArguments().getString("animeGenre");
                String animeDescription = getArguments().getString("animeDescription");
                String animeImage = getArguments().getString("animeImage");
                float animeRating = getArguments().getFloat("animeRating", 0.0f);
                
                // Debug: Log the received data
                android.util.Log.d("DetailFragment", "Received anime data:");
                android.util.Log.d("DetailFragment", "Title: " + animeTitle);
                android.util.Log.d("DetailFragment", "Author: " + animeAuthor);
                android.util.Log.d("DetailFragment", "Genre: " + animeGenre);
                android.util.Log.d("DetailFragment", "Description: " + animeDescription);
                android.util.Log.d("DetailFragment", "Image: " + animeImage);
                android.util.Log.d("DetailFragment", "Rating: " + animeRating);
                
                // Set the data to views
                TextView titleView = view.findViewById(R.id.titledets);
                TextView genreView = view.findViewById(R.id.genredets);
                TextView synopsisView = view.findViewById(R.id.syndets);
//                TextView pageTitle = view.findViewById(R.id.pageTitle);
                ImageView imageView = view.findViewById(R.id.detailImage);
                TextView typeView = view.findViewById(R.id.typedets);

                TextView authorView = view.findViewById(R.id.authordets);
                TextView ratingView = view.findViewById(R.id.ratingdets);
                
                // Set title with fallback
                String displayTitle = (animeTitle != null && !animeTitle.isEmpty()) ? animeTitle : "Unknown Title";
                if (titleView != null) titleView.setText(displayTitle);
//                if (pageTitle !pageTitle= null) pageTitle.setText(displayTitle);
                
                // Set genre with fallback (include author info)
                String displayGenre = (animeGenre != null && !animeGenre.isEmpty()) ? animeGenre : "Unknown Genre";
                if (genreView != null) genreView.setText(displayGenre);

                String displayAuthor = (animeAuthor != null && !animeAuthor.isEmpty()) ? animeAuthor : "Unknown Author";
                if (authorView != null) authorView.setText(displayAuthor);

                // Set rating with fallback
                String displayRating = (animeRating > 0) ? "★ " + animeRating
                        : "No rating available";
                if (ratingView != null) ratingView.setText(displayRating);
                
                // Set description with fallback
                String displayDescription = (animeDescription != null && !animeDescription.isEmpty()) ? animeDescription : "No description available";
                if (synopsisView != null) synopsisView.setText(displayDescription);
                
                // Set image
                if (imageView != null && animeImage != null && !animeImage.isEmpty()) {
                    int imageResId = getContext().getResources().getIdentifier(animeImage, "drawable", getContext().getPackageName());
                    if (imageResId != 0) {
                        imageView.setImageResource(imageResId);
                    }
                }

                typeView.setText("Anime");
            } else if ("review".equals(dataType)) {
                // Handle review data
                String reviewTitle = getArguments().getString("reviewTitle");
                String reviewSubtitle = getArguments().getString("reviewSubtitle");
                String reviewDescription = getArguments().getString("reviewDescription");
                String reviewImage = getArguments().getString("reviewImage");
                int reviewRating = getArguments().getInt("reviewRating", 0);
                
                // Set the data to views
                TextView titleView = view.findViewById(R.id.titledets);
                TextView genreView = view.findViewById(R.id.genredets);
                TextView synopsisView = view.findViewById(R.id.syndets);
                ImageView imageView = view.findViewById(R.id.detailImage);
                TextView typeView = view.findViewById(R.id.typedets);
                TextView authorView = view.findViewById(R.id.authordets);
                TextView ratingView = view.findViewById(R.id.ratingdets);

                // Set title with fallback
                String displayTitle = (reviewTitle != null && !reviewTitle.isEmpty()) ? reviewTitle : "Unknown Title";
                if (titleView != null) titleView.setText(displayTitle);

                // Set subtitle as genre info
                String displayGenre = (reviewSubtitle != null && !reviewSubtitle.isEmpty()) ? reviewSubtitle : "Dark Action Masterpiece";
                if (genreView != null) genreView.setText(displayGenre);

                // Set description with fallback
                String displayDescription = (reviewDescription != null && !reviewDescription.isEmpty()) ? reviewDescription : "No description available";
                if (synopsisView != null) synopsisView.setText(displayDescription);
                
                // Set image
                if (imageView != null && reviewImage != null && !reviewImage.isEmpty()) {
                    int imageResId = getContext().getResources().getIdentifier(reviewImage, "drawable", getContext().getPackageName());
                    if (imageResId != 0) {
                        imageView.setImageResource(imageResId);
                    }
                }

                // Set type
                if (typeView != null) typeView.setText("Review");

                // Show rating if available, hide if not
                if (reviewRating > 0) {
                    String stars = "★ " + reviewRating;
                    if (ratingView != null) {
                        ratingView.setText(stars);
                        ratingView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (ratingView != null) ratingView.setVisibility(View.GONE);
                }

                // Hide author for reviews (not applicable)
                if (authorView != null) authorView.setVisibility(View.GONE);
            } else if ("manga".equals(dataType)) {
                // Handle manga data
                String mangaTitle = getArguments().getString("mangaTitle");
                String mangaChapter = getArguments().getString("mangaChapter");
                String mangaDescription = getArguments().getString("mangaDescription");
                String mangaImage = getArguments().getString("mangaImage");
                String mangaStatus = getArguments().getString("mangaStatus");
                String mangaUpdateDate = getArguments().getString("mangaUpdateDate");
                String mangaAuthor = getArguments().getString("mangaAuthor");
                String mangaGenre = getArguments().getString("mangaGenre");
                float mangaRating = getArguments().getFloat("mangaRating", 5.3f);

                // Set the data to views
                TextView titleView = view.findViewById(R.id.titledets);
                TextView genreView = view.findViewById(R.id.genredets);
                TextView synopsisView = view.findViewById(R.id.syndets);
                ImageView imageView = view.findViewById(R.id.detailImage);
                TextView typeView = view.findViewById(R.id.typedets);
                TextView authorView = view.findViewById(R.id.authordets);
                TextView ratingView = view.findViewById(R.id.ratingdets);

                // Set title with fallback
                String displayTitle = (mangaTitle != null && !mangaTitle.isEmpty()) ? mangaTitle : "Unknown Manga";
                if (titleView != null) titleView.setText(displayTitle);

                // Set genre info with chapter/status/date as additional info
                String displayInfo = "";
                if (mangaGenre != null && !mangaGenre.isEmpty()) {
                    displayInfo = mangaGenre;
                }
                if (mangaChapter != null && !mangaChapter.isEmpty()) {
                    displayInfo += (displayInfo.isEmpty() ? "" : " • ") + mangaChapter;
                }
                if (mangaStatus != null && !mangaStatus.isEmpty()) {
                    displayInfo += (displayInfo.isEmpty() ? "" : " • ") + mangaStatus;
                }
                if (mangaUpdateDate != null && !mangaUpdateDate.isEmpty()) {
                    displayInfo += (displayInfo.isEmpty() ? "" : " • ") + mangaUpdateDate;
                }
                if (genreView != null) genreView.setText(displayInfo.isEmpty() ? "Manga" : displayInfo);
                
                // Set description with fallback
                String displayDescription = (mangaDescription != null && !mangaDescription.isEmpty()) ? mangaDescription : "No description available";
                if (synopsisView != null) synopsisView.setText(displayDescription);
                
                // Set image
                if (imageView != null && mangaImage != null && !mangaImage.isEmpty()) {
                    int imageResId = getContext().getResources().getIdentifier(mangaImage, "drawable", getContext().getPackageName());
                    if (imageResId != 0) {
                        imageView.setImageResource(imageResId);
                    }
                }

                // Set type
                if (typeView != null) typeView.setText("Manga");

                // Show author if available, hide if not
                if (mangaAuthor != null && !mangaAuthor.isEmpty()) {
                    if (authorView != null) {
                        authorView.setText(mangaAuthor);
                        authorView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (authorView != null) authorView.setVisibility(View.GONE);
                }

                // Show rating if available for manga carousel items
                if (mangaRating > 0) {
                    String displayRating = "★ " + mangaRating;
                    if (ratingView != null) {
                        ratingView.setText(displayRating);
                        ratingView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (ratingView != null) ratingView.setVisibility(View.GONE);
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
            TextView typeView = view.findViewById(R.id.typedets);
//            TextView pageTitle = view.findViewById(R.id.pageTitle);
            
            if (titleView != null) titleView.setText("No Title Available");
            if (genreView != null) genreView.setText("No Genre Available");
            if (synopsisView != null) synopsisView.setText("No synopsis available");
//            if (pageTitle != null) pageTitle.setText("DETAIL");
            if (typeView != null) typeView.setText("No type available");
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
                // Set button to red color for error
                setPopupButtonColor(btnPost, "#D3727E");
                return;
            }
            
            // Hide error message if validation passes
            tvErrorMessage.setVisibility(View.GONE);
            
            // Set button to green color for success
            setPopupButtonColor(btnPost, "#70B603");

            // Disable button to prevent multiple clicks
            btnPost.setEnabled(false);

            // Show success feedback with delay
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // Here you can handle the review submission
                Toast.makeText(getContext(), "Review posted successfully!", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                rootView.removeView(overlayView); // Remove overlay when closing
            }, 1500); // 1.5 second delay to show green color
        });

        // Reset button color when user starts typing
        etReview.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetPopupButtonColor(btnPost);
                btnPost.setEnabled(true);
                tvErrorMessage.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
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

    private void setPopupButtonColor(Button button, String colorHex) {
        // Use background tint to color the existing drawable (same approach as login button)
        android.content.res.ColorStateList colorStateList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(colorHex));
        button.setBackgroundTintList(colorStateList);

        // Debug log
        android.util.Log.d("DetailFragment", "Setting popup button color to: " + colorHex);
    }

    private void resetPopupButtonColor(Button button) {
        // Clear the background tint to restore original appearance
        button.setBackgroundTintList(null);
        android.util.Log.d("DetailFragment", "Resetting popup button color");
    }
}
