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

        recyclerViewFacturas = findViewById(R.id.recyclerViewFacturas);
        recyclerViewFacturas.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        facturaDao = new FacturaDao(db);

        cargarFacturas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarFacturas(); // Refrescar la lista de facturas al volver
    }

    private void cargarFacturas() {
        facturaDao.getAllFacturas(facturas -> {
            if (facturas != null && !facturas.isEmpty()) {
                facturaAdapter = new FacturaAdapter(facturas);
                recyclerViewFacturas.setAdapter(facturaAdapter);

                facturaAdapter.setOnItemClickListener(factura -> {
                    Intent intent = new Intent(BuscarFactura.this, InformeFactura.class);
                    intent.putExtra("id", factura.getId());
                    intent.putExtra("numeroFactura", factura.getNumeroFactura());
                    intent.putExtra("categoria", factura.getCategoria());
                    intent.putExtra("fecha", factura.getFecha());
                    intent.putExtra("monto", factura.getMonto());
                    intent.putExtra("vendedor", factura.getVendedor());
                    intent.putExtra("ciudad", factura.getCiudad());
                    startActivity(intent);
                });
            }
        });
    }
}
