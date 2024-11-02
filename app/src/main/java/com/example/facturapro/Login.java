package com.example.facturapro;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;

import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQ_ONE_TAP = 2;  // Puedes usar cualquier entero único

    private FirebaseAuth auth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private EditText etInputLoginUserEmail, etInputLoginUserPassword;
    private Button btnIniciarSesion,btnRegistro,btnSesionGoogle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Inicializar el cliente One Tap
        oneTapClient = Identity.getSignInClient(Login.this);

        // Construir el request de inicio de sesión
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // ID de cliente del servidor, NO el de la aplicación Android
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        // Inicializar vistas
        etInputLoginUserEmail = findViewById(R.id.etInputLoginUserEmail);
        etInputLoginUserPassword = findViewById(R.id.etInputLoginUserPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnSesionGoogle = findViewById(R.id.btnSesionGoogle);

        // Configurar listeners de botones
        btnIniciarSesion.setOnClickListener(v -> loginUser());
        btnRegistro.setOnClickListener(v -> registerUser());
        btnSesionGoogle.setOnClickListener(v -> signIn());

    }

    private void loginUser() {
        String email = etInputLoginUserEmail.getText().toString().trim();
        String password = etInputLoginUserPassword.getText().toString().trim();

        // Validaciones básicas
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar progreso
        btnIniciarSesion.setEnabled(false);

        // Intentar login
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    btnIniciarSesion.setEnabled(true);
                    if (task.isSuccessful()) {
                        // Login exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(Login.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                        // Aquí puedes redirigir al usuario a la siguiente actividad
                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Login fallido
                        Toast.makeText(Login.this, "Error en el login: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser() {
        String email = etInputLoginUserEmail.getText().toString().trim();
        String password = etInputLoginUserPassword.getText().toString().trim();

        // Validaciones básicas
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar longitud de contraseña
        if (password.length() < 6) {
            Toast.makeText(Login.this, "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar progreso
        btnRegistro.setEnabled(false);

        // Intentar registro
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, task -> {
                    btnRegistro.setEnabled(true);
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(Login.this, "Registro exitoso",
                                Toast.LENGTH_SHORT).show();
                        // Aquí puedes redirigir al usuario a la siguiente actividad
                        // Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        // startActivity(intent);
                        // finish();
                    } else {
                        // Registro fallido
                        Toast.makeText(Login.this, "Error en el registro: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Iniciar el flujo de inicio de sesión con Google One Tap
    private void signIn() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(Login.this, result -> {

                    try {
                        startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                null, 0, 0, 0);

                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        // Si One Tap falla, iniciar el flujo normal de Google Sign-In
                    }
                })
                .addOnFailureListener(this, e -> {
                    // No se pudo mostrar la UI de One Tap, usar el flujo normal
                    Log.d(TAG, e.getLocalizedMessage());
                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        // Autenticar con Firebase
                        firebaseAuthWithGoogle(idToken);
                    }

                } catch (ApiException e) {
                    // ... Manejar el error ...
                }
                break;

            // ... Manejar otros códigos de solicitud, como el del flujo normal ...
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // Error de inicio de sesión
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // El usuario ha iniciado sesión
            // Redirigir a la actividad de inicio de sesión
            Intent intent = new Intent(Login.this, Menu.class);
            startActivity(intent);
            finish();
        } else {
            // El usuario no ha iniciado sesión
            // Mostrar la interfaz de usuario de inicio de sesión
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verificar si el usuario ya está logueado
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Usuario ya logueado, redirigir a la actividad principal
            Intent intent = new Intent(Login.this, Menu.class);
            startActivity(intent);
            finish();
        }
    }
}