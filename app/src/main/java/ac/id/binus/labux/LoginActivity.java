package ac.id.binus.labux;

import android.content.Intent;
import android.os.Bundle;
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

        // If validation fails, return early
        if (hasError) {
            return;
        }

        // If validation passes, store username and navigate to MainActivity
        UserSession.getInstance().setUsername(username);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
}
