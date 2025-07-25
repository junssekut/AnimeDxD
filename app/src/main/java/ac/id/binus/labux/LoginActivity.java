package ac.id.binus.labux;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private View btnGoogleLogin, btnAppleLogin, btnFacebookLogin;

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
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        btnGoogleLogin = findViewById(R.id.btn_google_login);
        btnAppleLogin = findViewById(R.id.btn_apple_login);
        btnFacebookLogin = findViewById(R.id.btn_facebook_login);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
        
        btnGoogleLogin.setOnClickListener(v -> handleGoogleLogin());
        
        btnAppleLogin.setOnClickListener(v -> handleAppleLogin());
        
        btnFacebookLogin.setOnClickListener(v -> handleFacebookLogin());
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // For now, simulate a successful login
        // In a real app, you would validate credentials with your backend
        if (username.equals("admin") && password.equals("password")) {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            
            // Navigate to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleForgotPassword() {
        Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show();
        // Implement forgot password functionality
    }

    private void handleGoogleLogin() {
        Toast.makeText(this, "Google Login clicked", Toast.LENGTH_SHORT).show();
        // Implement Google OAuth login
    }

    private void handleAppleLogin() {
        Toast.makeText(this, "Apple Login clicked", Toast.LENGTH_SHORT).show();
        // Implement Apple login
    }

    private void handleFacebookLogin() {
        Toast.makeText(this, "Facebook Login clicked", Toast.LENGTH_SHORT).show();
        // Implement Facebook login
    }
}
