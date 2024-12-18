package com.example.facturapro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facturapro.data.dao.FacturaDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FacturasActivity extends AppCompatActivity {

    private Button btnBucarFactura, btnIngresarFactura;
    private ImageView ivCerrarSesionFacturas;
    private FacturaDao facturaDao;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_facturas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivCerrarSesionFacturas = findViewById(R.id.ivCerrarSesionFacturas);
        btnIngresarFactura = findViewById(R.id.btnIngresarFactura);
        btnBucarFactura = findViewById(R.id.btnBucarFactura);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        facturaDao = new FacturaDao(db);

        btnBucarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacturasActivity.this, BuscarFacturaActivity.class);
                startActivity(intent);
            }
        });

        btnIngresarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacturasActivity.this, IngresarFacturaActivity.class);
                startActivity(intent);
            }
        });
        ivCerrarSesionFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacturasActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        auth = FirebaseAuth.getInstance();
        ivCerrarSesionFacturas = findViewById(R.id.ivCerrarSesionFacturas);

        ivCerrarSesionFacturas.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(FacturasActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(FacturasActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}