package com.example.facturapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class login extends AppCompatActivity {

    private EditText etInputLoginUserName;
    private EditText etInputLoginUserPassword;
    private Button btnIniciarSesion;


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

        etInputLoginUserName = findViewById(R.id.etInputLoginUserName);
        etInputLoginUserPassword = findViewById(R.id.etInputLoginUserPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etInputLoginUserName.getText().toString();
                String password = etInputLoginUserPassword.getText().toString();
                // Datos quemados para validación
                String userCorrect = "a";
                String passwordCorrect = "1";

                if (user.equals(userCorrect) && password.equals(passwordCorrect)){
                    Intent intent = new Intent(login.this, menu.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(login.this, "Usuario o Contraseña Incorrecto", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}