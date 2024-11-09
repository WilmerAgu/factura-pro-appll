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
import com.google.firebase.firestore.FirebaseFirestore;

public class Facturas extends AppCompatActivity {

    private Button btnBucarFactura, btnIngresarFactura;
    private ImageView ivCerrarSesionFacturas;
    private FacturaDao facturaDao;


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
                Intent intent = new Intent(Facturas.this, BuscarFactura.class);
                startActivity(intent);
            }
        });

        btnIngresarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Facturas.this, IngresarFactura.class);
                startActivity(intent);
            }
        });
        ivCerrarSesionFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Facturas.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}