package ac.id.binus.labux.ui.about;

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

import ac.id.binus.labux.LoginActivity;
import ac.id.binus.labux.R;
import ac.id.binus.labux.utils.UserSession;

public class AboutFragment extends Fragment {

    private ImageView menuIcon;
    private TextView userTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_about, container, false);

        // Initialize views
        menuIcon = root.findViewById(R.id.menuIcon);
        userTextView = root.findViewById(R.id.user);

        setupMenuDropdown();
        setupWelcomeMessage();

        return root;
    }

    private void setupWelcomeMessage() {
        String username = UserSession.getInstance().getUsername();
        if (username != null && !username.isEmpty()) {
            // Capitalize first letter of username
            String capitalizedUsername = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
            userTextView.setText(capitalizedUsername);
        } else {
            // Fallback if no username is stored
            userTextView.setText("User");
        }
    }

    private void setupMenuDropdown() {
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
}
