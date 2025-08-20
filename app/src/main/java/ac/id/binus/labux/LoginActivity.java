package ac.id.binus.labux;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ac.id.binus.labux.utils.UserSession;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvUsernameError, tvPasswordError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide action bar for full screen experience
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvUsernameError = findViewById(R.id.tv_username_error);
        tvPasswordError = findViewById(R.id.tv_password_error);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());

        // Reset button color when user starts typing (clears error state)
        etUsername.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetButtonColor();
                btnLogin.setEnabled(true);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        etPassword.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetButtonColor();
                btnLogin.setEnabled(true);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void handleLogin() {
        // Clear previous error messages
        hideErrorMessages();

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean hasError = false;

        // Validate username
        if (username.isEmpty()) {
            showUsernameError("Username must be filled in");
            hasError = true;
        } else if (username.length() < 5 || username.length() > 10) {
            showUsernameError("Username must be 5-10 characters long");
            hasError = true;
        }

        // Validate password
        if (password.isEmpty()) {
            showPasswordError("Password must not be empty");
            hasError = true;
        }

        // If validation fails, set button to red color and return early
        if (hasError) {
            setButtonColor("#D3727E"); // Red color for error
            return;
        }

        // Clear any previous error color first
        resetButtonColor();

        // If validation passes, set button to green color
        setButtonColor("#70B603"); // Green color for success

        // Disable button to prevent multiple clicks
        btnLogin.setEnabled(false);

        // Store username and navigate to MainActivity after a delay
        UserSession.getInstance().setUsername(username);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500); // Increased to 1.5 seconds delay
    }

    private void showUsernameError(String message) {
        tvUsernameError.setText(message);
        tvUsernameError.setVisibility(View.VISIBLE);
    }

    private void showPasswordError(String message) {
        tvPasswordError.setText(message);
        tvPasswordError.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessages() {
        tvUsernameError.setVisibility(View.GONE);
        tvPasswordError.setVisibility(View.GONE);
    }

    private void setButtonColor(String color) {
        // Use background tint to color the existing drawable
        android.content.res.ColorStateList colorStateList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(color));
        btnLogin.setBackgroundTintList(colorStateList);

        // Also add a debug log to make sure this method is being called
        android.util.Log.d("LoginActivity", "Setting button color to: " + color);
    }

    private void resetButtonColor() {
        // Clear the background tint to restore original appearance
        btnLogin.setBackgroundTintList(null);
        android.util.Log.d("LoginActivity", "Resetting button color");
    }
}
