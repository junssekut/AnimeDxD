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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

        // Setup ViewPager2 with adapter
        carouselAdapter = new AnimeCarouselAdapter(getContext(), carouselAnimeList);
        binding.animeCarouselViewpager.setAdapter(carouselAdapter);

        // Setup indicators
        setupCarouselIndicators();

        // Setup page change callback for updating indicators
        binding.animeCarouselViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                updateIndicators(position);
            }
        });

        // Setup navigation button click listeners
        setupCarouselNavigation();

        // Setup auto-scroll
        setupAutoScroll();
    }

    private void setupCarouselIndicators() {
        binding.carouselIndicators.removeAllViews();

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
            binding.carouselIndicators.addView(indicators[i]);
        }
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < binding.carouselIndicators.getChildCount(); i++) {
            ImageView indicator = (ImageView) binding.carouselIndicators.getChildAt(i);
            if (i == position) {
                indicator.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.indicator_active));
            } else {
                indicator.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.indicator_inactive));
            }
        }
    }

    private void setupAutoScroll() {
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (carouselAnimeList != null && carouselAnimeList.size() > 0) {
                    currentPage = (currentPage + 1) % carouselAnimeList.size();
                    binding.animeCarouselViewpager.setCurrentItem(currentPage, true);
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
        // Setup Weekly Top 3 RecyclerView
        LinearLayoutManager weeklyLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.weeklyTopRecyclerview.setLayoutManager(weeklyLayoutManager);

        // Setup Section 1 RecyclerView (News/Manga Updates)
        LinearLayoutManager section1LayoutManager = new LinearLayoutManager(getContext());
        binding.section1Recyclerview.setLayoutManager(section1LayoutManager);

        // Setup Section 2 RecyclerView (Reviews/Manga Releases)
        LinearLayoutManager section2LayoutManager = new LinearLayoutManager(getContext());
        binding.section2Recyclerview.setLayoutManager(section2LayoutManager);

        // Create sample data and set adapters
        setupWeeklyTopData();
        setupRecentNewsData();
        setupLatestReviewData();
    }

    private void setupWeeklyTopData() {
        List<Anime> weeklyTopAnime = new ArrayList<>();
        weeklyTopAnime.add(new Anime("Shingeki No Kyojin", "ACTION", "shingeki_no_kyojin", "shingeki_no_kyojin_wide"));
        weeklyTopAnime.add(new Anime("Kimetsu No Yaiba", "ACTION", "kimetsu_no_yaiba", "kimetsu_no_yaiba_wide"));
        weeklyTopAnime.add(new Anime("Tokyo Ghoul", "DARK FANTASY", "tokyo_ghoul", "tokyo_ghoul_wide"));

        WeeklyTopAdapter weeklyAdapter = new WeeklyTopAdapter(getContext(), weeklyTopAnime);
        binding.weeklyTopRecyclerview.setAdapter(weeklyAdapter);
    }

    private void setupRecentNewsData() {
        List<News> recentNews = new ArrayList<>();
        recentNews.add(new News(
            "Attack On Titan",
            "In a world where humanity is on the brink of extinction due to giant humanoid creatures known as Titans...",
            "Posted on May 30, 2025",
            "shingeki_no_kyojin"
        ));
        recentNews.add(new News(
            "Demon Slayer",
            "After his family is slaughtered by demons and his sister is turned into one, Tanjiro Kamado becomes a demon slayer...",
            "Posted on June 3, 2025",
            "kimetsu_no_yaiba"
        ));
        recentNews.add(new News(
            "Chainsaw Man",
            "In a world where humanity battles devil creatures, Denji becomes the Chainsaw Man after merging with his devil dog Pochita...",
            "Posted on June 15, 2025",
            "chainsaw_man"
        ));
        recentNews.add(new News(
            "Tokyo Ghoul",
            "Ken Kaneki's life changes forever when he encounters his date, Rize, who reveals herself to be a flesh-eating ghoul...",
            "Posted on June 20, 2025",
            "tokyo_ghoul"
        ));
        recentNews.add(new News(
            "Sword Art Online",
            "Players become trapped in a virtual reality MMORPG where death in the game means death in real life...",
            "Posted on June 25, 2025",
            "sword_art_online"
        ));

        RecentNewsAdapter newsAdapter = new RecentNewsAdapter(getContext(), recentNews);
        binding.section1Recyclerview.setAdapter(newsAdapter);
    }

    private void setupLatestReviewData() {
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
        binding.section2Recyclerview.setAdapter(reviewAdapter);
    }

    private void setupTabSwitching() {
        binding.newsTab.setOnClickListener(v -> {
            // Switch to news tab
            binding.newsTab.setBackgroundResource(R.drawable.tab_selected_yellow);
            binding.newsTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.background_dark));
            binding.mangaTab.setBackground(null);
            binding.mangaTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));

            isMangaTabSelected = false;
            loadAnimeContent();
        });

        binding.mangaTab.setOnClickListener(v -> {
            // Switch to manga tab
            binding.mangaTab.setBackgroundResource(R.drawable.tab_selected_yellow);
            binding.mangaTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.background_dark));
            binding.newsTab.setBackground(null);
            binding.newsTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));

            isMangaTabSelected = true;
            loadMangaContent();
        });
    }

    private void loadAnimeContent() {
        // Show anime sections
        binding.featuredSection.setVisibility(View.VISIBLE);
        binding.mangaCarousel.getRoot().setVisibility(View.GONE);
        binding.weeklyTopTitle.setVisibility(View.VISIBLE);
        binding.weeklyTopRecyclerview.setVisibility(View.VISIBLE);

        // Update section titles
        binding.section1Title.setText(R.string.recent_news);
        binding.section2Title.setText(R.string.latest_review);

        // Setup anime data
        setupRecentNewsData();
        setupLatestReviewData();

        // Start auto-scroll for carousel
        startAutoScroll();
    }

    private void loadMangaContent() {
        // Hide anime sections, show manga sections
        binding.featuredSection.setVisibility(View.GONE);
        binding.mangaCarousel.getRoot().setVisibility(View.VISIBLE);
        binding.weeklyTopTitle.setVisibility(View.GONE);
        binding.weeklyTopRecyclerview.setVisibility(View.GONE);

        // Update section titles for manga
        binding.section1Title.setText(R.string.popular_manga_updates);
        binding.section2Title.setText(R.string.latest_manga_releases);

        // Setup manga data
        setupPopularMangaUpdatesData();
        setupLatestMangaReleasesData();

        // Stop auto-scroll when in manga mode
        stopAutoScroll();
    }

    private void setupPopularMangaUpdatesData() {
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

        // Setup RecyclerView with horizontal layout for manga updates
        LinearLayoutManager mangaUpdateLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.section1Recyclerview.setLayoutManager(mangaUpdateLayoutManager);
        binding.section1Recyclerview.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        MangaUpdateAdapter mangaUpdateAdapter = new MangaUpdateAdapter(getContext(), mangaUpdates);
        binding.section1Recyclerview.setAdapter(mangaUpdateAdapter);
    }

    private void setupLatestMangaReleasesData() {
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
        binding.section2Recyclerview.setAdapter(mangaReleaseAdapter);
    }

    private void setupCarouselNavigation() {
        binding.carouselNavLeft.setOnClickListener(v -> {
            int currentItem = binding.animeCarouselViewpager.getCurrentItem();
            int previousItem;

            if (currentItem == 0) {
                // Wrap to last item
                previousItem = carouselAnimeList.size() - 1;
            } else {
                previousItem = currentItem - 1;
            }

            binding.animeCarouselViewpager.setCurrentItem(previousItem, true);

            // Reset auto-scroll timer when user manually navigates
            startAutoScroll();
        });

        binding.carouselNavRight.setOnClickListener(v -> {
            int currentItem = binding.animeCarouselViewpager.getCurrentItem();
            int nextItem;

            if (currentItem == carouselAnimeList.size() - 1) {
                // Wrap to first item
                nextItem = 0;
            } else {
                nextItem = currentItem + 1;
            }

            binding.animeCarouselViewpager.setCurrentItem(nextItem, true);

            // Reset auto-scroll timer when user manually navigates
            startAutoScroll();
        });
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