package ac.id.binus.labux.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import ac.id.binus.labux.R;
import ac.id.binus.labux.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerViews();
        setupTabSwitching();

        return root;
    }

    private void setupRecyclerViews() {
        // Setup Weekly Top 3 RecyclerView
        LinearLayoutManager weeklyLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.weeklyTopRecyclerview.setLayoutManager(weeklyLayoutManager);

        // Setup Recent News RecyclerView
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(getContext());
        binding.recentNewsRecyclerview.setLayoutManager(newsLayoutManager);

        // Setup Latest Review RecyclerView
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext());
        binding.latestReviewRecyclerview.setLayoutManager(reviewLayoutManager);

        // TODO: Add adapters with sample data
    }

    private void setupTabSwitching() {
        binding.newsTab.setOnClickListener(v -> {
            // Switch to news tab
            binding.newsTab.setBackgroundResource(R.drawable.tab_selected);
            binding.newsTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.background_dark));
            binding.mangaTab.setBackground(null);
            binding.mangaTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        });

        binding.mangaTab.setOnClickListener(v -> {
            // Switch to manga tab
            binding.mangaTab.setBackgroundResource(R.drawable.tab_selected);
            binding.mangaTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.background_dark));
            binding.newsTab.setBackground(null);
            binding.newsTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}