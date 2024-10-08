package com.example.facturapro;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturapro.data.adapter.FacturaAdapter;
import com.example.facturapro.data.model.Factura;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class buscarFactura extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FacturaAdapter adapter;
    private List<Factura> facturaList;
    private FirebaseFirestore db;

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

        // Inicializa RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFacturas);  // Asegúrate de que este ID coincida con el de tu XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        facturaList = new ArrayList<>();
        adapter = new FacturaAdapter(facturaList);
        recyclerView.setAdapter(adapter);

        // Inicializa Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Cargar datos de Firebase
        cargarFacturas();
    }
    private void cargarFacturas() {
        db.collection("facturas")  // Cambia 'facturas' por el nombre de tu colección en Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        facturaList.clear();  // Limpia la lista antes de agregar nuevas facturas
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Factura factura = document.toObject(Factura.class);
                            facturaList.add(factura);
                        }
                        adapter.notifyDataSetChanged();  // Notifica al adapter que los datos han cambiado
                    } else {
                        // Manejo de errores
                    }
                });

    }
}