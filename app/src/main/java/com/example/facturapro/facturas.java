package com.example.facturapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class facturas extends AppCompatActivity {

    private Button btnBucarFactura, btnIngresarFactura;
    private ImageView ivCerrarSesionFacturas;

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
        ImageView ivCerrarSesionFacturas = findViewById(R.id.ivCerrarSesionFacturas);
        Button btnIngresarFactura = findViewById(R.id.btnIngresarFactura);
        Button btnBucarFactura = findViewById(R.id.btnBucarFactura);



        ivCerrarSesionFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(facturas.this, login.class);
                startActivity(intent);
            }
        });

        btnBucarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(facturas.this, buscarFactura.class);
                startActivity(intent);
            }
        });

        btnIngresarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(facturas.this, ingresarFactura.class);
                startActivity(intent);
            }
        });





    }
}