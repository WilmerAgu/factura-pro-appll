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

public class Menu extends AppCompatActivity {
    private Button btnFacturas, btnGastos, btnReportes;
    private ImageView ivCerrarSesionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnFacturas = findViewById(R.id.btnFacturas);
        Button btnGastos = findViewById(R.id.btnGastos);
        Button btnReportes = findViewById(R.id.btnReportes);
        ImageView ivCerrarSesionMenu = findViewById(R.id.ivCerrarSesionMenu);


        btnFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Facturas.class);
                startActivity(intent);
            }
        });

        btnGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Menu.this, Gastos.class);
                startActivity(intent
                );
            }
        });

        btnReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Menu.this, Reportes.class);
                startActivity(intent
                );
            }
        });

        ivCerrarSesionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Login.class);
                startActivity(intent);
            }
        });
    }
}