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
        carouselAnimeList.add(new Anime("Shingeki No Kyojin", "ACTION", "shingeki_no_kyojin", "shingeki_no_kyojin_wide"));
        carouselAnimeList.add(new Anime("Kimetsu No Yaiba", "ACTION", "kimetsu_no_yaiba", "kimetsu_no_yaiba_wide"));
        carouselAnimeList.add(new Anime("Tokyo Ghoul", "DARK FANTASY", "tokyo_ghoul", "tokyo_ghoul_wide"));
        carouselAnimeList.add(new Anime("Chainsaw Man", "ACTION", "chainsaw_man", "chainsaw_man_wide"));
        carouselAnimeList.add(new Anime("Sword Art Online", "ADVENTURE", "sword_art_online", "sword_art_online_wide"));

        // Get carousel elements from anime content layout
        View animeContent = binding.animeContent.getRoot();
        ViewPager2 carouselViewPager = animeContent.findViewById(R.id.anime_carousel_viewpager);
        LinearLayout carouselIndicators = animeContent.findViewById(R.id.carousel_indicators);

        // Setup ViewPager2 with adapter
        carouselAdapter = new AnimeCarouselAdapter(getContext(), carouselAnimeList);
        carouselViewPager.setAdapter(carouselAdapter);

        // Setup indicators
        setupCarouselIndicators(carouselIndicators);

        // Setup page change callback for updating indicators
        carouselViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                updateIndicators(position, carouselIndicators);
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
                    currentPage = (currentPage + 1) % carouselAnimeList.size();
                    carouselViewPager.setCurrentItem(currentPage, true);
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
            "Action, Drama, Fantasy"
        ));
        recentNews.add(new News(
            "Demon Slayer",
            "After his family is slaughtered by demons and his sister is turned into one, Tanjiro Kamado becomes a demon slayer...",
            "Posted on June 3, 2025",
            "kimetsu_no_yaiba",
            "Tanjiro Kamado's peaceful life is shattered when demons kill his family and transform his sister Nezuko into a demon. Determined to find a cure for Nezuko and avenge his family, Tanjiro trains to become a demon slayer. Armed with his compassionate nature and newfound skills, he embarks on a dangerous journey filled with deadly demons, powerful breathing techniques, and the hope of restoring his sister's humanity while protecting others from the same tragic fate.",
            "Action, Supernatural, Historical"
        ));
        recentNews.add(new News(
            "Chainsaw Man",
            "In a world where humanity battles devil creatures, Denji becomes the Chainsaw Man after merging with his devil dog Pochita...",
            "Posted on June 15, 2025",
            "chainsaw_man",
            "Denji lives in poverty, hunting devils to pay off his father's debt to the yakuza. His only companion is Pochita, a devil dog with a chainsaw for a head. When betrayed by the yakuza and killed, Denji merges with Pochita to become Chainsaw Man, a hybrid with the power to transform parts of his body into chainsaws. Now working for the government's Public Safety Devil Hunters, Denji must navigate a world of dangerous devils while pursuing his simple dreams of a normal life.",
            "Action, Supernatural, Dark Fantasy"
        ));
        recentNews.add(new News(
            "Tokyo Ghoul",
            "Ken Kaneki's life changes forever when he encounters his date, Rize, who reveals herself to be a flesh-eating ghoul...",
            "Posted on June 20, 2025",
            "tokyo_ghoul",
            "Ken Kaneki, a bookish college student, barely survives a date with Rize, a beautiful woman who reveals herself to be a flesh-eating ghoul. After being transplanted with Rize's organs, Kaneki becomes a half-ghoul, struggling to maintain his humanity while dealing with his new craving for human flesh. Caught between the human and ghoul worlds, he must learn to navigate this dangerous new existence while uncovering the dark secrets of both species.",
            "Dark Fantasy, Supernatural, Thriller"
        ));
        recentNews.add(new News(
            "Sword Art Online",
            "Players become trapped in a virtual reality MMORPG where death in the game means death in real life...",
            "Posted on June 25, 2025",
            "sword_art_online",
            "In the year 2022, virtual reality gaming reaches new heights with Sword Art Online, a VRMMORPG that allows players to control their avatars with their minds. However, when the game launches, players discover they cannot log out and that dying in the game means dying in real life. Kirito, a beta tester, must survive the 100 floors of Aincrad and defeat the final boss to free all trapped players, forming bonds and facing challenges that blur the line between virtual and reality.",
            "Adventure, Romance, Sci-Fi"
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

        // Setup Manga Updates RecyclerView
        RecyclerView mangaUpdatesRecyclerView = mangaContent.findViewById(R.id.manga_updates_recyclerview);
        LinearLayoutManager mangaUpdateLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mangaUpdatesRecyclerView.setLayoutManager(mangaUpdateLayoutManager);

        List<MangaUpdate> mangaUpdates = new ArrayList<>();
        mangaUpdates.add(new MangaUpdate(
            "Tensei Shitara Slime Datta",
            "Dec 7, 2024",
            "shingeki_no_kyojin_manga",
            "Ch.159"
        ));
        mangaUpdates.add(new MangaUpdate(
            "Kusuriya no Hitorigoto",
            "Feb 14, 2025",
            "kimetsu_no_yaiba_manga",
            "Ch.78"
        ));
        mangaUpdates.add(new MangaUpdate(
            "Sono Bisque Doll wa Koi wo Suru",
            "May 11, 2025",
            "tokyo_ghoul_manga",
            "Ch.97"
        ));

        MangaUpdateAdapter mangaUpdateAdapter = new MangaUpdateAdapter(getContext(), mangaUpdates);
        mangaUpdatesRecyclerView.setAdapter(mangaUpdateAdapter);

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
        weeklyTopAnime.add(new Anime("Shingeki No Kyojin", "ACTION", "shingeki_no_kyojin", "shingeki_no_kyojin_wide"));
        weeklyTopAnime.add(new Anime("Kimetsu No Yaiba", "ACTION", "kimetsu_no_yaiba", "kimetsu_no_yaiba_wide"));
        weeklyTopAnime.add(new Anime("Tokyo Ghoul", "DARK FANTASY", "tokyo_ghoul", "tokyo_ghoul_wide"));

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
            TextView animeWatchingCount = cardView.findViewById(R.id.anime_watching_count);
            TextView animeEpisodesCount = cardView.findViewById(R.id.anime_episodes_count);

            // Load image
            int imageResId = getContext().getResources().getIdentifier(anime.getImageResource(), "drawable", getContext().getPackageName());
            if (imageResId != 0) {
                animeImage.setImageResource(imageResId);
            }

            // Set text data
            animeTitle.setText(anime.getTitle());
            animeGenre.setText(anime.getGenre());
            animeReviews.setText("(1200 reviews)");
            animeWatchingCount.setText("3200");
            animeEpisodesCount.setText("25");

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
            int previousItem;

            if (currentItem == 0) {
                // Wrap to last item
                previousItem = carouselAnimeList.size() - 1;
            } else {
                previousItem = currentItem - 1;
            }

            carouselViewPager.setCurrentItem(previousItem, true);

            // Reset auto-scroll timer when user manually navigates
            startAutoScroll();
        });

        carouselNavRight.setOnClickListener(v -> {
            int currentItem = carouselViewPager.getCurrentItem();
            int nextItem;

            if (currentItem == carouselAnimeList.size() - 1) {
                // Wrap to first item
                nextItem = 0;
            } else {
                nextItem = currentItem + 1;
            }

            carouselViewPager.setCurrentItem(nextItem, true);

            // Reset auto-scroll timer when user manually navigates
            startAutoScroll();
        });
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