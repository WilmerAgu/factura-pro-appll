package com.example.facturapro;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturapro.data.adapter.FacturaAdapter;
import com.example.facturapro.data.dao.FacturaDao;
import com.google.firebase.firestore.FirebaseFirestore;

public class BuscarFactura extends AppCompatActivity {

    private RecyclerView recyclerViewFacturas;
    private FacturaAdapter facturaAdapter;
    private FacturaDao facturaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_factura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Inicializar RecyclerView
        recyclerViewFacturas = findViewById(R.id.recyclerViewFacturas);
        recyclerViewFacturas.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar FacturaDao para obtener las facturas
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        facturaDao = new FacturaDao(db);

        // Cargar las facturas y configurar el RecyclerView
        cargarFacturas();
    }

    private void cargarFacturas() {
        facturaDao.getAllFacturas(facturas -> {
            if (facturas != null && !facturas.isEmpty()) {
                // Inicializar el adapter con la lista de facturas
                facturaAdapter = new FacturaAdapter(facturas);
                recyclerViewFacturas.setAdapter(facturaAdapter);

                // Configurar el listener de clic para cada item
                facturaAdapter.setOnItemClickListener(factura -> {
                    // Navegar a InformeFactura y pasar los datos
                    Intent intent = new Intent(BuscarFactura.this, InformeFactura.class);
                    intent.putExtra("numeroFactura", factura.getNumeroFactura());
                    intent.putExtra("categoria", factura.getCategoria());
                    /*intent.putExtra("fecha", factura.getFecha());*/
                    intent.putExtra("monto", factura.getMonto());
                    intent.putExtra("vendedor", factura.getVendedor());
                    intent.putExtra("ciudad", factura.getCiudad());

                    startActivity(intent);
                });
            }
        });
    }

}