package com.example.facturapro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facturapro.data.dao.FacturaDao;
import com.example.facturapro.data.model.FacturaModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class InformeFactura extends AppCompatActivity {

    private static final String TAG = "InformeFactura";

    private EditText etNumeroFactura, etMonto, etCategoria, etVendedor, etCiudad, etFecha;
    private Button btnActualizarFactura, btnEliminarfactura, btnCancelarfactura;
    private FacturaDao facturaDao;
    private FacturaModel selectedFactura;
    private String selectedFacturaId;

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

        etNumeroFactura = findViewById(R.id.etNumeroFactura);
        etMonto = findViewById(R.id.etMonto);
        etCategoria = findViewById(R.id.etCategoria);
        etVendedor = findViewById(R.id.etVendedor);
        etCiudad = findViewById(R.id.etCiudad);
        etFecha = findViewById(R.id.etFecha);
        btnActualizarFactura = findViewById(R.id.btnActualizarFactura);
        btnEliminarfactura = findViewById(R.id.btnEliminarfactura);
        btnCancelarfactura = findViewById(R.id.btnCancelarfactura);

        facturaDao = new FacturaDao(FirebaseFirestore.getInstance());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedFacturaId = extras.getString("id");

            if (selectedFacturaId == null || selectedFacturaId.isEmpty()) {
                Toast.makeText(this, "ID de factura no válido", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            etNumeroFactura.setText(extras.getString("numeroFactura", ""));
            etMonto.setText(extras.getString("monto", ""));
            etCategoria.setText(extras.getString("categoria", ""));
            etVendedor.setText(extras.getString("vendedor", ""));
            etCiudad.setText(extras.getString("ciudad", ""));
            etFecha.setText(extras.getString("fecha", ""));

            facturaDao.getById(selectedFacturaId, factura -> {
                if (factura != null) {
                    selectedFactura = factura;
                } else {
                    Toast.makeText(this, "No se pudo recuperar la factura", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnActualizarFactura.setOnClickListener(view -> {
            if (selectedFactura == null) {
                Toast.makeText(InformeFactura.this, "Por favor, espera a que se cargue la factura.", Toast.LENGTH_SHORT).show();
                return;
            }

            String numeroFactura = etNumeroFactura.getText().toString().trim();
            String monto = etMonto.getText().toString().trim();
            String categoria = etCategoria.getText().toString().trim();
            String vendedor = etVendedor.getText().toString().trim();
            String ciudad = etCiudad.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();

            if (numeroFactura.isEmpty() || monto.isEmpty() || categoria.isEmpty() || vendedor.isEmpty() || ciudad.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(InformeFactura.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedFactura.setNumeroFactura(numeroFactura);
            selectedFactura.setMonto(monto);
            selectedFactura.setCategoria(categoria);
            selectedFactura.setVendedor(vendedor);
            selectedFactura.setCiudad(ciudad);
            selectedFactura.setFecha(fecha);

            facturaDao.update(selectedFacturaId, selectedFactura, isSuccess -> {
                if (isSuccess) {
                    Toast.makeText(InformeFactura.this, "Factura Actualizada", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(InformeFactura.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnEliminarfactura.setOnClickListener(v -> {
            if (selectedFacturaId != null) {
                facturaDao.delete(selectedFacturaId, new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean isSuccess) {
                        if (isSuccess) {
                            Toast.makeText(InformeFactura.this, "Factura eliminada", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(InformeFactura.this, "Error al eliminar la factura", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(InformeFactura.this, "Seleccione una factura para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelarfactura.setOnClickListener(view -> {
            Intent intent = new Intent(InformeFactura.this, BuscarFactura.class);
            startActivity(intent);
        });
    }
}
