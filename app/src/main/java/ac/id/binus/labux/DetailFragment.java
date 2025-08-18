package ac.id.binus.labux;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

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

        if (getArguments() != null) {
            String dataType = getArguments().getString("dataType");
            
            if ("news".equals(dataType)) {
                // Handle news data
                String newsTitle = getArguments().getString("newsTitle");
                String newsGenre = getArguments().getString("newsGenre");
                String newsSynopsis = getArguments().getString("newsSynopsis");
                String newsImage = getArguments().getString("newsImage");
                
                // Set the data to views
                TextView titleView = view.findViewById(R.id.titledets);
                TextView genreView = view.findViewById(R.id.genredets);
                TextView synopsisView = view.findViewById(R.id.syndets);
                TextView pageTitle = view.findViewById(R.id.pageTitle);
                ImageView imageView = view.findViewById(R.id.myImage);

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
            } else if ("anime".equals(dataType)) {
                // Handle anime data
                String animeTitle = getArguments().getString("animeTitle");
                String animeAuthor = getArguments().getString("animeAuthor");
                String animeGenre = getArguments().getString("animeGenre");
                String animeDescription = getArguments().getString("animeDescription");
                String animeImage = getArguments().getString("animeImage");
                float animeRating = getArguments().getFloat("animeRating", 0.0f);
                
                // Set the data to views
                TextView titleView = view.findViewById(R.id.titledets);
                TextView genreView = view.findViewById(R.id.genredets);
                TextView synopsisView = view.findViewById(R.id.syndets);
                TextView pageTitle = view.findViewById(R.id.pageTitle);
                ImageView imageView = view.findViewById(R.id.myImage);

                // Set title with fallback
                String displayTitle = (animeTitle != null && !animeTitle.isEmpty()) ? animeTitle : "Unknown Title";
                if (titleView != null) titleView.setText(displayTitle);
                if (pageTitle != null) pageTitle.setText(displayTitle);
                
                // Set genre with fallback (include author info)
                String displayGenre = "";
                if (animeAuthor != null && !animeAuthor.isEmpty()) {
                    displayGenre = animeAuthor + " • ";
                }
                displayGenre += (animeGenre != null && !animeGenre.isEmpty()) ? animeGenre : "Unknown Genre";
                if (animeRating > 0) {
                    displayGenre += " • ★ " + animeRating;
                }
                if (genreView != null) genreView.setText(displayGenre);
                
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
                TextView pageTitle = view.findViewById(R.id.pageTitle);
                ImageView imageView = view.findViewById(R.id.myImage);

                // Set title with fallback
                String displayTitle = (reviewTitle != null && !reviewTitle.isEmpty()) ? reviewTitle : "Unknown Title";
                if (titleView != null) titleView.setText(displayTitle);
                if (pageTitle != null) pageTitle.setText(displayTitle);
                
                // Set subtitle and rating as genre info
                String displayInfo = "";
                if (reviewSubtitle != null && !reviewSubtitle.isEmpty()) {
                    displayInfo = reviewSubtitle;
                }
                if (reviewRating > 0) {
                    String stars = "★".repeat(reviewRating) + "☆".repeat(5 - reviewRating);
                    displayInfo += (displayInfo.isEmpty() ? "" : " • ") + stars;
                }
                if (genreView != null) genreView.setText(displayInfo.isEmpty() ? "No info available" : displayInfo);
                
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
            } else if ("manga".equals(dataType)) {
                // Handle manga data
                String mangaTitle = getArguments().getString("mangaTitle");
                String mangaChapter = getArguments().getString("mangaChapter");
                String mangaDescription = getArguments().getString("mangaDescription");
                String mangaImage = getArguments().getString("mangaImage");
                String mangaStatus = getArguments().getString("mangaStatus");
                String mangaUpdateDate = getArguments().getString("mangaUpdateDate");
                
                // Set the data to views
                TextView titleView = view.findViewById(R.id.titledets);
                TextView genreView = view.findViewById(R.id.genredets);
                TextView synopsisView = view.findViewById(R.id.syndets);
                TextView pageTitle = view.findViewById(R.id.pageTitle);
                ImageView imageView = view.findViewById(R.id.myImage);

                // Set title with fallback
                String displayTitle = (mangaTitle != null && !mangaTitle.isEmpty()) ? mangaTitle : "Unknown Manga";
                if (titleView != null) titleView.setText(displayTitle);
                if (pageTitle != null) pageTitle.setText(displayTitle);
                
                // Set chapter/status info as genre
                String displayInfo = "";
                if (mangaChapter != null && !mangaChapter.isEmpty()) {
                    displayInfo = mangaChapter;
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
    }
}