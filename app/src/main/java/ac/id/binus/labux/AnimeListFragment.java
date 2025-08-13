package ac.id.binus.labux;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ac.id.binus.labux.LoginActivity;
import ac.id.binus.labux.model.Anime;
import ac.id.binus.labux.utils.UserSession;

public class AnimeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimeAdapter animeAdapter;
    private List<Anime> animeList;
    private TextView tvAnimeCount;

    public AnimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anime_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_anime);
        tvAnimeCount = view.findViewById(R.id.tv_anime_count);
        ImageView menuIcon = view.findViewById(R.id.menu_icon);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize anime data
        initializeAnimeData();
        
        // Setup adapter
        animeAdapter = new AnimeAdapter(getContext(), animeList);
        recyclerView.setAdapter(animeAdapter);
        
        // Update anime count
        tvAnimeCount.setText(animeList.size() + " Animes");

        // Setup menu icon click with dropdown
        setupMenuDropdown(menuIcon);
    }

    private void setupMenuDropdown(ImageView menuIcon) {
        menuIcon.setOnClickListener(v -> showDropdownMenu(v));
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
        // Clear user session
        UserSession.getInstance().clearSession();

        // Create intent to go back to LoginActivity
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

        // Finish the current activity
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void initializeAnimeData() {
        animeList = new ArrayList<>();
        
        // Add anime data based on the image shown
        animeList.add(new Anime(
            "Attack On Titan",
            "Hajime Isayama",
            "Action, Dark Fantasy",
            "In a world where humanity is on the brink of extinction due to giant humanoid creatures known as Titans...",
            "cover1", // use existing drawable
            "cover1",
            9.0f,
            15420,
            32000,
            87
        ));

        animeList.add(new Anime(
            "Demon Slayer",
            "Koyoharu Gotouge",
            "Action, Supernatural",
            "After his family is slaughtered by demons and his sister is turned into one, Tanjiro Kamado becomes...",
            "cover2",
            "cover2",
            8.6f,
            12340,
            28000,
            44
        ));

        animeList.add(new Anime(
            "Jujutsu Kaisen",
            "Gege Akutami",
            "Action, Supernatural",
            "Yuji Itadori, a high schooler with extraordinary strength, swallows a cursed object to save his...",
            "cover3",
            "cover3",
            8.6f,
            11200,
            25000,
            24
        ));

        animeList.add(new Anime(
            "One Piece",
            "Eiichiro Oda",
            "Action, Adventure",
            "Monkey D. Luffy sets off on his adventure to find the legendary treasure known as 'One Piece'...",
            "onepieceanime",
            "onepieceanime",
            8.7f,
            18500,
            45000,
            1000
        ));

        animeList.add(new Anime(
            "Naruto",
            "Masashi Kishimoto",
            "Action, Adventure",
            "Naruto Uzumaki, a young ninja who seeks recognition from his peers and dreams of becoming the Hokage...",
            "cover4",
            "cover4",
            8.4f,
            16800,
            38000,
            720
        ));
    }
}
