package ac.id.binus.labux;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import ac.id.binus.labux.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hide the action bar to remove "HOME" title
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        setupCustomBottomNavigation();
    }

    private void setupCustomBottomNavigation() {
        // Set home as selected by default
        updateNavigationSelection(R.id.nav_home_btn);

        // Home button click
        binding.navHomeBtn.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_home);
            updateNavigationSelection(R.id.nav_home_btn);
        });

        // List button click (navigate to home for now)
        binding.navListBtn.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_home);
            updateNavigationSelection(R.id.nav_list_btn);
        });

        // About button click (navigate to home for now)
        binding.navAboutBtn.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_about);
            updateNavigationSelection(R.id.nav_about_btn);
        });
    }

    private void updateNavigationSelection(int selectedId) {
        // Reset all icons to secondary color by finding ImageView children
        LinearLayout homeBtn = binding.navHomeBtn;
        LinearLayout listBtn = binding.navListBtn;
        LinearLayout aboutBtn = binding.navAboutBtn;

        ImageView homeIcon = (ImageView) homeBtn.getChildAt(0);
        ImageView listIcon = (ImageView) listBtn.getChildAt(0);
        ImageView aboutIcon = (ImageView) aboutBtn.getChildAt(0);

        int secondaryColor = ContextCompat.getColor(this, R.color.text_secondary);
        int selectedColor = ContextCompat.getColor(this, R.color.golden_yellow);

        homeIcon.setColorFilter(secondaryColor);
        listIcon.setColorFilter(secondaryColor);
        aboutIcon.setColorFilter(secondaryColor);

        // Set selected icon to golden yellow
        if (selectedId == R.id.nav_home_btn) {
            homeIcon.setColorFilter(selectedColor);
        } else if (selectedId == R.id.nav_list_btn) {
            listIcon.setColorFilter(selectedColor);
        } else if (selectedId == R.id.nav_about_btn) {
            aboutIcon.setColorFilter(selectedColor);
        }
    }

}