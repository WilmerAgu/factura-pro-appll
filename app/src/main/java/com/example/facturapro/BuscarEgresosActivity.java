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

import com.example.facturapro.data.adapter.EgresosAdapter;
import com.example.facturapro.data.dao.EgresosDao;
import com.google.firebase.firestore.FirebaseFirestore;

public class BuscarEgresosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEgresos;
    private EgresosAdapter egresosAdapter;
    private EgresosDao egresosDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_egresos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewEgresos = findViewById(R.id.recyclerViewEgresos);
        recyclerViewEgresos.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        egresosDao = new EgresosDao(db);

        cargarEgresos();
    }


    @Override
    protected void onResume() {
        super.onResume();
        cargarEgresos(); // Refrescar la lista de facturas al volver
    }

    private void cargarEgresos() {
        egresosDao.getAllEgresos(egresos -> {
            if (egresos != null && !egresos.isEmpty()) {
                egresosAdapter = new EgresosAdapter(egresos);
                recyclerViewEgresos.setAdapter(egresosAdapter);

                egresosAdapter.setOnItemClickListener(egreso -> {
                    Intent intent = new Intent(BuscarEgresosActivity.this, InformeEgresosActivity.class);
                    intent.putExtra("id", egreso.getId());
                    intent.putExtra("numeroFacturaEgreso", egreso.getNumeroFacturaEgreso());
                    intent.putExtra("categoriaEgreso", egreso.getCategoriaEgreso());
                    intent.putExtra("fechaEgreso", egreso.getFechaEgreso());
                    intent.putExtra("montoEgreso", egreso.getMontoEgreso());
                    intent.putExtra("estadoPago", egreso.getEstadoPago());
                    intent.putExtra("modoPago", egreso.getModoPago());
                    startActivity(intent);
                });
            }
        });
    }
}