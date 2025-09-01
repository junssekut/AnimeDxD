package ac.id.binus.labux.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import ac.id.binus.labux.LoginActivity;
import ac.id.binus.labux.R;
import ac.id.binus.labux.databinding.FragmentHomeBinding;
import ac.id.binus.labux.model.Anime;
import ac.id.binus.labux.model.News;
import ac.id.binus.labux.model.Review;
import ac.id.binus.labux.model.MangaUpdate;
import ac.id.binus.labux.model.MangaRelease;
import ac.id.binus.labux.adapter.WeeklyTopAdapter;
import ac.id.binus.labux.adapter.RecentNewsAdapter;
import ac.id.binus.labux.adapter.LatestReviewAdapter;
import ac.id.binus.labux.adapter.MangaUpdateAdapter;
import ac.id.binus.labux.adapter.MangaReleaseAdapter;
import ac.id.binus.labux.adapter.AnimeCarouselAdapter;
import ac.id.binus.labux.utils.UserSession;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private boolean isMangaTabSelected = false;
    private List<Anime> carouselAnimeList;
    private AnimeCarouselAdapter carouselAdapter;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;
    private int currentPage = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerViews();
        setupAnimeCarousel();
        setupTabSwitching();
        setupMenuDropdown();
        setupWelcomeMessage();

        // Load anime content by default
        loadAnimeContent();

        return root;
    }

    private void setupWelcomeMessage() {
        String username = UserSession.getInstance().getUsername();
        if (username != null && !username.isEmpty()) {
            // Capitalize first letter of username
            String capitalizedUsername = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
            binding.welcomeTitle.setText("Welcome, " + capitalizedUsername);
        } else {
            // Fallback if no username is stored
            binding.welcomeTitle.setText("Welcome, User");
        }
    }

    private void setupAnimeCarousel() {
        // Create anime list for carousel
        carouselAnimeList = new ArrayList<>();
        carouselAnimeList.add(new Anime(
            "Shingeki No Kyojin", 
            "Hajime Isayama",
            "ACTION", 
            "Humanity lives within enormous walls that protect them from gigantic man-eating humanoids called Titans. When the outermost wall is breached, young Eren Yeager's life changes forever as he witnesses the death of his mother.",
            "shingeki_no_kyojin", 
            "shingeki_no_kyojin_wide",
            9.0f,
            15420,
            32000,
            87
        ));
        carouselAnimeList.add(new Anime(
            "Kimetsu No Yaiba", 
            "Koyoharu Gotouge",
            "ACTION", 
            "Tanjiro Kamado's peaceful life is shattered when demons kill his family and transform his sister Nezuko into a demon. Determined to find a cure for Nezuko and avenge his family, Tanjiro trains to become a demon slayer.",
            "kimetsu_no_yaiba", 
            "kimetsu_no_yaiba_wide",
            8.6f,
            12340,
            28000,
            44
        ));
        carouselAnimeList.add(new Anime(
            "Tokyo Ghoul", 
            "Sui Ishida",
            "DARK FANTASY", 
            "Ken Kaneki, a bookish college student, barely survives a date with Rize, a beautiful woman who reveals herself to be a flesh-eating ghoul. After being transplanted with Rize's organs, Kaneki becomes a half-ghoul.",
            "tokyo_ghoul", 
            "tokyo_ghoul_wide",
            8.4f,
            11200,
            25000,
            24
        ));
        carouselAnimeList.add(new Anime(
            "Chainsaw Man", 
            "Tatsuki Fujimoto",
            "ACTION", 
            "Denji lives in poverty, hunting devils to pay off his father's debt to the yakuza. His only companion is Pochita, a devil dog with a chainsaw for a head. When betrayed by the yakuza and killed, Denji merges with Pochita.",
            "chainsaw_man", 
            "chainsaw_man_wide",
            8.7f,
            18500,
            45000,
            12
        ));
        carouselAnimeList.add(new Anime(
            "Sword Art Online", 
            "Reki Kawahara",
            "ADVENTURE", 
            "In the year 2022, virtual reality gaming reaches new heights with Sword Art Online, a VRMMORPG that allows players to control their avatars with their minds. However, when the game launches, players discover they cannot log out.",
            "sword_art_online", 
            "sword_art_online_wide",
            8.5f,
            16800,
            38000,
            96
        ));

        // Get carousel elements from anime content layout
        View animeContent = binding.animeContent.getRoot();
        ViewPager2 carouselViewPager = animeContent.findViewById(R.id.anime_carousel_viewpager);
        LinearLayout carouselIndicators = animeContent.findViewById(R.id.carousel_indicators);

        // Setup ViewPager2 with adapter
        carouselAdapter = new AnimeCarouselAdapter(getContext(), carouselAnimeList);
        carouselViewPager.setAdapter(carouselAdapter);

        // Set starting position to middle for infinite scrolling
        int startPosition = carouselAdapter.getStartPosition();
        carouselViewPager.setCurrentItem(startPosition, false);

        // Setup indicators
        setupCarouselIndicators(carouselIndicators);

        // Setup page change callback for updating indicators
        carouselViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Get real position for indicators
                int realPosition = carouselAdapter.getRealPosition(position);
                currentPage = realPosition;
                updateIndicators(realPosition, carouselIndicators);
            }
        });

        // Setup navigation button click listeners
        setupCarouselNavigation(animeContent, carouselViewPager);

        // Setup auto-scroll
        setupAutoScroll(carouselViewPager);
    }

    private void setupCarouselIndicators(LinearLayout carouselIndicators) {
        carouselIndicators.removeAllViews();

        ImageView[] indicators = new ImageView[carouselAnimeList.size()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(4, 0, 4, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getContext(),
                i == 0 ? R.drawable.indicator_active : R.drawable.indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            carouselIndicators.addView(indicators[i]);
        }
    }

    private void updateIndicators(int position, LinearLayout carouselIndicators) {
        for (int i = 0; i < carouselIndicators.getChildCount(); i++) {
            ImageView indicator = (ImageView) carouselIndicators.getChildAt(i);
            if (i == position) {
                indicator.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.indicator_active));
            } else {
                indicator.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.indicator_inactive));
            }
        }
    }

    private void setupAutoScroll(ViewPager2 carouselViewPager) {
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (carouselAnimeList != null && carouselAnimeList.size() > 0) {
                    int currentItem = carouselViewPager.getCurrentItem();
                    carouselViewPager.setCurrentItem(currentItem + 1, true);
                    autoScrollHandler.postDelayed(this, 5000); // 5 seconds
                }
            }
        };
        startAutoScroll();
    }

    private void startAutoScroll() {
        stopAutoScroll();
        autoScrollHandler.postDelayed(autoScrollRunnable, 5000);
    }

    private void stopAutoScroll() {
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    private void setupRecyclerViews() {
        // Setup Anime Content RecyclerViews
        setupAnimeContent();

        // Setup Manga Content RecyclerViews
        setupMangaContent();
    }

    private void setupAnimeContent() {
        // Setup Weekly Top Data
        setupWeeklyTopData();

        // Setup Recent News RecyclerView
        View animeContent = binding.animeContent.getRoot();
        RecyclerView recentNewsRecyclerView = animeContent.findViewById(R.id.recent_news_recyclerview);
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(getContext());
        recentNewsRecyclerView.setLayoutManager(newsLayoutManager);

        List<News> recentNews = new ArrayList<>();
        recentNews.add(new News(
            "Attack On Titan",
            "In a world where humanity is on the brink of extinction due to giant humanoid creatures known as Titans...",
            "Posted on May 30, 2025",
            "shingeki_no_kyojin",
            "Humanity lives within enormous walls that protect them from gigantic man-eating humanoids called Titans. When the outermost wall is breached, young Eren Yeager's life changes forever as he witnesses the death of his mother. Driven by revenge, he joins the Survey Corps to fight against the Titans and uncover the truth behind their existence. As mysteries unfold, Eren discovers shocking secrets about the Titans, the walls, and his own destiny that will change everything he thought he knew about his world.",
            "Action, Drama, Fantasy",
                7.1f
        ));
        recentNews.add(new News(
            "Demon Slayer",
            "After his family is slaughtered by demons and his sister is turned into one, Tanjiro Kamado becomes a demon slayer...",
            "Posted on June 3, 2025",
            "kimetsu_no_yaiba",
            "Tanjiro Kamado's peaceful life is shattered when demons kill his family and transform his sister Nezuko into a demon. Determined to find a cure for Nezuko and avenge his family, Tanjiro trains to become a demon slayer. Armed with his compassionate nature and newfound skills, he embarks on a dangerous journey filled with deadly demons, powerful breathing techniques, and the hope of restoring his sister's humanity while protecting others from the same tragic fate.",
            "Action, Supernatural, Historical",
                8.1f
        ));
        recentNews.add(new News(
            "Chainsaw Man",
            "In a world where humanity battles devil creatures, Denji becomes the Chainsaw Man after merging with his devil dog Pochita...",
            "Posted on June 15, 2025",
            "chainsaw_man",
            "Denji lives in poverty, hunting devils to pay off his father's debt to the yakuza. His only companion is Pochita, a devil dog with a chainsaw for a head. When betrayed by the yakuza and killed, Denji merges with Pochita to become Chainsaw Man, a hybrid with the power to transform parts of his body into chainsaws. Now working for the government's Public Safety Devil Hunters, Denji must navigate a world of dangerous devils while pursuing his simple dreams of a normal life.",
            "Action, Supernatural, Dark Fantasy",
                5.3f
        ));
        recentNews.add(new News(
            "Tokyo Ghoul",
            "Ken Kaneki's life changes forever when he encounters his date, Rize, who reveals herself to be a flesh-eating ghoul...",
            "Posted on June 20, 2025",
            "tokyo_ghoul",
            "Ken Kaneki, a bookish college student, barely survives a date with Rize, a beautiful woman who reveals herself to be a flesh-eating ghoul. After being transplanted with Rize's organs, Kaneki becomes a half-ghoul, struggling to maintain his humanity while dealing with his new craving for human flesh. Caught between the human and ghoul worlds, he must learn to navigate this dangerous new existence while uncovering the dark secrets of both species.",
            "Dark Fantasy, Supernatural, Thriller",
                8.8f
        ));
        recentNews.add(new News(
            "Sword Art Online",
            "Players become trapped in a virtual reality MMORPG where death in the game means death in real life...",
            "Posted on June 25, 2025",
            "sword_art_online",
            "In the year 2022, virtual reality gaming reaches new heights with Sword Art Online, a VRMMORPG that allows players to control their avatars with their minds. However, when the game launches, players discover they cannot log out and that dying in the game means dying in real life. Kirito, a beta tester, must survive the 100 floors of Aincrad and defeat the final boss to free all trapped players, forming bonds and facing challenges that blur the line between virtual and reality.",
            "Adventure, Romance, Sci-Fi",
                9.0f
        ));

        RecentNewsAdapter newsAdapter = new RecentNewsAdapter(getContext(), recentNews);
        recentNewsRecyclerView.setAdapter(newsAdapter);

        // Setup Latest Review RecyclerView
        RecyclerView latestReviewRecyclerView = animeContent.findViewById(R.id.latest_review_recyclerview);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext());
        latestReviewRecyclerView.setLayoutManager(reviewLayoutManager);

        List<Review> latestReviews = new ArrayList<>();
        latestReviews.add(new Review(
            "Shingeki No Kyojin",
            "Attack On Titan",
            "Whispers from the Abyss: A harrowing tale that gripped us from the first episode. The story of humanity's fight against titans delivers exceptional character development and plot twists that redefine the genre.",
            5,
            "shingeki_no_kyojin"
        ));
        latestReviews.add(new Review(
            "Tokyo Ghoul",
            "Dark Fantasy Masterpiece",
            "A perfect blend of horror and humanity. Tokyo Ghoul explores the thin line between monster and human through Ken Kaneki's transformation. The psychological depth is unmatched.",
            4,
            "tokyo_ghoul"
        ));

        LatestReviewAdapter reviewAdapter = new LatestReviewAdapter(getContext(), latestReviews);
        latestReviewRecyclerView.setAdapter(reviewAdapter);
    }

    private void setupMangaContent() {
        View mangaContent = binding.mangaContent.getRoot();

        // Setup Manga Carousel Click Functionality
        setupMangaCarouselClickListeners(mangaContent);

        // Setup Manga Updates Container (horizontal layout)
        LinearLayout mangaUpdatesContainer = mangaContent.findViewById(R.id.manga_updates_container);
        mangaUpdatesContainer.removeAllViews();

        List<MangaUpdate> mangaUpdates = new ArrayList<>();
        mangaUpdates.add(new MangaUpdate(
            "Shingeki No Kyojin",
            "Hajime Isayama",
            "In a world where humanity is on the brink of extinction due to giant humanoid creatures known as Titans, the story follows Eren Yeager and his friends as they join the fight for survival and uncover the dark secrets behind the Titans.",
            "Action, Drama, Fantasy, Military",
            9.2f,
            "Ch.139",
            "Dec 7, 2024",
            "shingeki_no_kyojin_manga"
        ));
        mangaUpdates.add(new MangaUpdate(
            "Kimetsu No Yaiba",
            "Koyoharu Gotouge",
            "After his family is slaughtered by demons and his sister Nezuko is turned into one, Tanjiro Kamado becomes a demon slayer to find a cure for his sister and avenge his family.",
            "Action, Historical, Supernatural, Shounen",
            8.9f,
            "Ch.205",
            "Feb 14, 2025",
            "kimetsu_no_yaiba_manga"
        ));
        mangaUpdates.add(new MangaUpdate(
            "Tokyo Ghoul",
            "Sui Ishida",
            "Ken Kaneki, a bookish college student, meets Rize, a girl his own age with whom he shares compatibility. However, Rize is not exactly who she seems, and this unfortunate meeting pushes Kaneki into the dark depths of the ghouls' inhuman world.",
            "Action, Horror, Supernatural, Seinen",
            8.7f,
            "Ch.143",
            "May 11, 2025",
            "tokyo_ghoul_manga"
        ));

        // Add each manga update item to the horizontal container
        for (MangaUpdate mangaUpdate : mangaUpdates) {
            View itemView = getLayoutInflater().inflate(R.layout.item_manga_update, mangaUpdatesContainer, false);

            // Set layout params to distribute items evenly
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f // Equal weight for each item
            );
            layoutParams.setMargins(8, 0, 8, 0); // Add some spacing between items
            itemView.setLayoutParams(layoutParams);

            // Bind data to the views
            ImageView mangaImage = itemView.findViewById(R.id.manga_image);
            TextView mangaTitle = itemView.findViewById(R.id.manga_title);
            TextView updateDate = itemView.findViewById(R.id.update_date);
//            TextView status = itemView.findViewById(R.id.status);

            // Set the data
            if (mangaTitle != null) mangaTitle.setText(mangaUpdate.getTitle());
            if (updateDate != null) updateDate.setText(mangaUpdate.getUpdateDate());
//            if (status != null) status.setText(mangaUpdate.getStatus());

            // Set image
            if (mangaImage != null && mangaUpdate.getImageResource() != null) {
                int imageResId = getContext().getResources().getIdentifier(
                    mangaUpdate.getImageResource(), "drawable", getContext().getPackageName());
                if (imageResId != 0) {
                    mangaImage.setImageResource(imageResId);
                } else {
                    mangaImage.setImageResource(R.drawable.featured_anime);
                }
            }

            // Add click listener for navigation to detail page
            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("dataType", "manga");
                bundle.putString("mangaTitle", mangaUpdate.getTitle());
//                bundle.putString("mangaChapter", mangaUpdate.getStatus());
//                bundle.putString("mangaUpdateDate", mangaUpdate.getUpdateDate());
                bundle.putString("mangaImage", mangaUpdate.getImageResource());
                bundle.putString("mangaAuthor", mangaUpdate.getAuthor());
                bundle.putFloat("mangaRating", mangaUpdate.getRating());
                bundle.putString("mangaGenre", mangaUpdate.getGenres());
                bundle.putString("mangaDescription", mangaUpdate.getDescription());

                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
            });

            mangaUpdatesContainer.addView(itemView);
        }

        // Setup Manga Releases RecyclerView
        RecyclerView mangaReleasesRecyclerView = mangaContent.findViewById(R.id.manga_releases_recyclerview);
        LinearLayoutManager mangaReleaseLayoutManager = new LinearLayoutManager(getContext());
        mangaReleasesRecyclerView.setLayoutManager(mangaReleaseLayoutManager);

        List<MangaRelease> mangaReleases = new ArrayList<>();
        mangaReleases.add(new MangaRelease(
            "Attack On Titan",
            "In a world where memories can be bought and sold, Eiji wakes up with no past—only a haunting image of a city in flames. As he searches for the truth, he uncovers a memory-altering AI and begins to question whether his identity is even real.",
            "Chapter 110 - Hells of Tokyo",
            "shingeki_no_kyojin_manga"
        ));
        mangaReleases.add(new MangaRelease(
            "Demon Slayer",
            "Akari, cursed to bloom blood-red flowers under the full moon, meets a wandering samurai who sees beyond her curse. Together, they defy warlords and fate in a tale of forbidden love, ancient spirits and sacrifice.",
            "Chapter 262 - Domain Collapse",
            "kimetsu_no_yaiba_manga"
        ));
        mangaReleases.add(new MangaRelease(
            "Neon Orphans",
            "In the neon-soaked underworld of Neo-Tokyo, a group of orphaned hackers discover they're the key to stopping a corporate conspiracy that threatens to erase human consciousness itself.",
            "Chapter 15 - Digital Dreams",
            "chainsaw_man_manga"
        ));

        MangaReleaseAdapter mangaReleaseAdapter = new MangaReleaseAdapter(getContext(), mangaReleases);
        mangaReleasesRecyclerView.setAdapter(mangaReleaseAdapter);
    }

    private void setupWeeklyTopData() {
        List<Anime> weeklyTopAnime = new ArrayList<>();
        weeklyTopAnime.add(new Anime(
            "Shingeki No Kyojin", 
            "Hajime Isayama",
            "ACTION", 
            "Humanity lives within enormous walls that protect them from gigantic man-eating humanoids called Titans. When the outermost wall is breached, young Eren Yeager's life changes forever.",
            "shingeki_no_kyojin", 
            "shingeki_no_kyojin_wide",
            9.0f,
            1200,
            3200,
            25
        ));
        weeklyTopAnime.add(new Anime(
            "Kimetsu No Yaiba", 
            "Koyoharu Gotouge",
            "ACTION", 
            "Tanjiro Kamado's peaceful life is shattered when demons kill his family and transform his sister Nezuko into a demon. Determined to find a cure for Nezuko and avenge his family.",
            "kimetsu_no_yaiba", 
            "kimetsu_no_yaiba_wide",
            8.6f,
            1200,
            3200,
            25
        ));
        weeklyTopAnime.add(new Anime(
            "Tokyo Ghoul", 
            "Sui Ishida",
            "DARK FANTASY", 
            "Ken Kaneki, a bookish college student, barely survives a date with Rize, a beautiful woman who reveals herself to be a flesh-eating ghoul.",
            "tokyo_ghoul", 
            "tokyo_ghoul_wide",
            8.4f,
            1200,
            3200,
            25
        ));

        // Get the weekly top container from anime content
        View animeContent = binding.animeContent.getRoot();
        LinearLayout weeklyTopContainer = animeContent.findViewById(R.id.weekly_top_container);
        weeklyTopContainer.removeAllViews();

        // Add each card manually to the LinearLayout
        for (Anime anime : weeklyTopAnime) {
            View cardView = getLayoutInflater().inflate(R.layout.item_weekly_top, weeklyTopContainer, false);

            // Set up the card data
            ImageView animeImage = cardView.findViewById(R.id.anime_image);
            TextView animeTitle = cardView.findViewById(R.id.anime_title);
            TextView animeGenre = cardView.findViewById(R.id.anime_genre);
            TextView animeReviews = cardView.findViewById(R.id.anime_reviews);
            // Load image
            int imageResId = getContext().getResources().getIdentifier(anime.getImageResource(), "drawable", getContext().getPackageName());
            if (imageResId != 0) {
                animeImage.setImageResource(imageResId);
            }

            // Set text data
            animeTitle.setText(anime.getTitle());
            animeGenre.setText(anime.getGenre());
            animeReviews.setText("(" + anime.getReviewsCount() + " reviews)");

            // Set click listener for the entire card to navigate to detail page
            cardView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("dataType", "anime");
                bundle.putString("animeTitle", anime.getTitle());
                bundle.putString("animeAuthor", anime.getAuthor());
                bundle.putString("animeGenre", anime.getGenre());
                bundle.putString("animeDescription", anime.getDescription());
                bundle.putString("animeImage", anime.getImageResource());
                bundle.putFloat("animeRating", anime.getRating());
                
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
            });

            // Add to container
            weeklyTopContainer.addView(cardView);
        }
    }

    private void setupTabSwitching() {
        binding.newsTab.setOnClickListener(v -> {
            // Switch to news tab - golden background with white text
            binding.newsTab.setBackgroundResource(R.drawable.tab_selected_yellow);
            binding.newsTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

            // Reset manga tab - no background with secondary text color
            binding.mangaTab.setBackground(null);
            binding.mangaTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));

            isMangaTabSelected = false;
            loadAnimeContent();
        });

        binding.mangaTab.setOnClickListener(v -> {
            // Switch to manga tab - golden background with white text
            binding.mangaTab.setBackgroundResource(R.drawable.tab_selected_yellow);
            binding.mangaTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

            // Reset news tab - no background with secondary text color
            binding.newsTab.setBackground(null);
            binding.newsTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));

            isMangaTabSelected = true;
            loadMangaContent();
        });
    }

    private void loadAnimeContent() {
        // Show anime content, hide manga content
        binding.animeContent.getRoot().setVisibility(View.VISIBLE);
        binding.mangaContent.getRoot().setVisibility(View.GONE);


        // Start auto-scroll for carousel
        startAutoScroll();
    }

    private void loadMangaContent() {
        // Hide anime content, show manga content
        binding.animeContent.getRoot().setVisibility(View.GONE);
        binding.mangaContent.getRoot().setVisibility(View.VISIBLE);

        // Stop auto-scroll when in manga mode
        stopAutoScroll();
    }


    private void setupMenuDropdown() {
        binding.menuIcon.setOnClickListener(v -> showDropdownMenu(v));
    }

    private void showDropdownMenu(View anchorView) {
        // Inflate the dropdown menu layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dropdownView = inflater.inflate(R.layout.dropdown_menu, null);

        // Create popup window
        PopupWindow popupWindow = new PopupWindow(
            dropdownView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        );

        // Set up logout click listener
        dropdownView.findViewById(R.id.menu_logout).setOnClickListener(v -> {
            popupWindow.dismiss();
            logout();
        });

        // Set popup window properties
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // Show popup below and to the right of the menu icon
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        popupWindow.showAtLocation(anchorView,
            android.view.Gravity.NO_GRAVITY,
            location[0],
            location[1] + anchorView.getHeight() + 8);
    }

    private void logout() {
        // Stop any ongoing auto-scroll
        stopAutoScroll();

        // Create intent to go back to LoginActivity
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

        // Finish the current activity
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void setupCarouselNavigation(View animeContent, ViewPager2 carouselViewPager) {
        ImageView carouselNavLeft = animeContent.findViewById(R.id.carousel_nav_left);
        ImageView carouselNavRight = animeContent.findViewById(R.id.carousel_nav_right);

        carouselNavLeft.setOnClickListener(v -> {
            int currentItem = carouselViewPager.getCurrentItem();
            carouselViewPager.setCurrentItem(currentItem - 1, true);
            // Reset auto-scroll timer when user manually navigates
            startAutoScroll();
        });

        carouselNavRight.setOnClickListener(v -> {
            int currentItem = carouselViewPager.getCurrentItem();
            carouselViewPager.setCurrentItem(currentItem + 1, true);
            // Reset auto-scroll timer when user manually navigates
            startAutoScroll();
        });
    }

    private void setupMangaCarouselClickListeners(View mangaContent) {
        // Find the manga carousel cards from the included layout
        View mangaCarouselLayout = mangaContent.findViewById(R.id.manga_carousel);
        if (mangaCarouselLayout == null) {
            // If not found, try to get the include directly
            View includeLayout = getLayoutInflater().inflate(R.layout.manga_carousel, null);
            setupCarouselCardClickListeners(includeLayout);
        } else {
            setupCarouselCardClickListeners(mangaCarouselLayout);
        }
    }

    private void setupCarouselCardClickListeners(View carouselLayout) {
        // Find the first card (Hot Manga 2025)
        androidx.cardview.widget.CardView hotMangaCard = carouselLayout.findViewById(R.id.hot_manga_card);
        if (hotMangaCard == null) {
            // If ID doesn't exist, find by layout structure - first CardView
            ViewGroup rootLayout = (ViewGroup) carouselLayout;
            for (int i = 0; i < rootLayout.getChildCount(); i++) {
                View child = rootLayout.getChildAt(i);
                if (child instanceof androidx.cardview.widget.CardView) {
                    hotMangaCard = (androidx.cardview.widget.CardView) child;
                    break;
                }
            }
        }

        // Find the second card (Top 10 Anime Guys)
        androidx.cardview.widget.CardView topGuysCard = carouselLayout.findViewById(R.id.top_guys_card);
        if (topGuysCard == null) {
            // If ID doesn't exist, find by layout structure - second CardView
            ViewGroup rootLayout = (ViewGroup) carouselLayout;
            int cardCount = 0;
            for (int i = 0; i < rootLayout.getChildCount(); i++) {
                View child = rootLayout.getChildAt(i);
                if (child instanceof androidx.cardview.widget.CardView) {
                    cardCount++;
                    if (cardCount == 2) {
                        topGuysCard = (androidx.cardview.widget.CardView) child;
                        break;
                    }
                }
            }
        }

        // Set click listener for Hot Manga 2025 card
        if (hotMangaCard != null) {
            hotMangaCard.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("dataType", "manga");
                bundle.putString("mangaTitle", "Kimtesu No Yaiba: Hot Manga 2025");
                bundle.putString("mangaAuthor", "Koyoharu Gotouge");
                bundle.putString("mangaGenre", "Action, Adventure, Fantasy");
                bundle.putString("mangaDescription", "In a world where demons prey on humans, Tanjiro Kamado becomes a demon slayer to avenge his family and save his sister Nezuko, who has been turned into a demon. With the help of fellow slayers, he battles powerful demons while uncovering the secrets of their existence.");
                bundle.putString("mangaImage", "kimetsu_no_yaiba_manga");
                bundle.putFloat("mangaRating", 8.8f);

                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
            });
        }

        // Set click listener for Top 10 Anime Guys card
        if (topGuysCard != null) {
            topGuysCard.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("dataType", "manga");
                bundle.putString("mangaTitle", "Tokyo Ghoul: Top 10 Anime Guys With Handsome Looks");
                bundle.putString("mangaAuthor", "Sui Ishida");
                bundle.putString("mangaGenre", "Romance, Drama, Action");
                bundle.putString("mangaDescription", "In a world where flesh-eating ghouls exist alongside humans, Ken Kaneki becomes a half-ghoul after a near-fatal encounter. Struggling with his new identity, he navigates the dangerous world of ghouls while trying to maintain his humanity and protect those he loves.");
                bundle.putString("mangaImage", "tokyo_ghoul_manga");
                bundle.putFloat("mangaRating", 8.5f);

                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAutoScroll();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isMangaTabSelected) {
            startAutoScroll();
        }
    }
}
