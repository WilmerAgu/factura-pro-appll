package com.example.facturapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InformeFactura extends AppCompatActivity {

    private TextView numeroFacturaTextView, categoriaTextView, fechaTextView,
            montoTextView,vendedorTextView,ciudadTextView ;
    private Button btnVolverListaFacturas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informe_factura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar TextViews
        numeroFacturaTextView = findViewById(R.id.tvCargarFactura);
        categoriaTextView = findViewById(R.id.tvCargarCategoria);
        fechaTextView = findViewById(R.id.tvCargarFecha);
        montoTextView = findViewById(R.id.tvCargarMonto);
        vendedorTextView = findViewById(R.id.tvCargarVendedor);
        ciudadTextView = findViewById(R.id.tvCargarCiudad);
        btnVolverListaFacturas = findViewById(R.id.btnVolverListaFacturas);

        // Obtener los datos de la factura desde el Intent
        String numeroFactura = getIntent().getStringExtra("numeroFactura");
        String categoria = getIntent().getStringExtra("categoria");
        String fecha = getIntent().getStringExtra("fecha");
        String monto = getIntent().getStringExtra("monto");
        String vendedor = getIntent().getStringExtra("vendedor");
        String ciudad = getIntent().getStringExtra("ciudad");

        // Mostrar los datos en los TextViews
        numeroFacturaTextView.setText(numeroFactura);
        categoriaTextView.setText(categoria);
        fechaTextView.setText(fecha);
        montoTextView.setText(monto);
        vendedorTextView.setText(vendedor);
        ciudadTextView.setText(ciudad);

        btnVolverListaFacturas = findViewById(R.id.btnVolverListaFacturas);

        btnVolverListaFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformeFactura.this, BuscarFactura.class);
                startActivity(intent);
            }
        });
    }
}
