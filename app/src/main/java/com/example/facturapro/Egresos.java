package com.example.facturapro;

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

import com.example.facturapro.data.dao.EgresosDao;
import com.example.facturapro.data.dao.FacturaDao;
import com.google.firebase.firestore.FirebaseFirestore;

public class Egresos extends AppCompatActivity {

    private Button btnBucarEgreso, btnIngresarEgreso;
    private ImageView ivCerrarSesionEgreso;
    private EgresosDao egresosDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_egresos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivCerrarSesionEgreso = findViewById(R.id.ivCerrarSesionEgreso);
        btnIngresarEgreso = findViewById(R.id.btnIngresarEgreso);
        btnBucarEgreso = findViewById(R.id.btnBucarEgresos);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        egresosDao = new EgresosDao(db);

        btnBucarEgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Egresos.this, BuscarEgresos.class);
                startActivity(intent);
            }
        });

        btnIngresarEgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Egresos.this, IngresarEgreso.class);
                startActivity(intent);
            }
        });
        ivCerrarSesionEgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Egresos.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }
}