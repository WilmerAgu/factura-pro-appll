package com.example.facturapro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.Arrays;
import java.util.List;

public class InformeFactura extends AppCompatActivity {

    private static final String TAG = "InformeFactura";

    private EditText etNumeroFactura, etMonto, etFecha;
    private Button btnActualizarFactura, btnEliminarfactura, btnCancelarfactura;
    private FacturaDao facturaDao;
    private FacturaModel selectedFactura;
    private String selectedFacturaId;
    private Spinner spinnerCategoria, spinnerVendedor, spinnerCiudad;

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

        // Inicializar vistas
        etNumeroFactura = findViewById(R.id.etNumeroFactura);
        etMonto = findViewById(R.id.etMonto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerVendedor = findViewById(R.id.spinnerVendedor);
        spinnerCiudad = findViewById(R.id.spinnerCiudad);
        etFecha = findViewById(R.id.etFecha);
        btnActualizarFactura = findViewById(R.id.btnActualizarFactura);
        btnEliminarfactura = findViewById(R.id.btnEliminarfactura);
        btnCancelarfactura = findViewById(R.id.btnCancelarfactura);

        facturaDao = new FacturaDao(FirebaseFirestore.getInstance());

        // Cargar datos en los Spinners
        cargarDatosSpinners();

        // Obtener el ID de la factura seleccionada
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedFacturaId = extras.getString("id");

            if (selectedFacturaId == null || selectedFacturaId.isEmpty()) {
                Toast.makeText(this, "ID de factura no válido", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Establecer valores en los EditText
            etNumeroFactura.setText(extras.getString("numeroFactura", ""));
            etMonto.setText(extras.getString("monto", ""));
            etFecha.setText(extras.getString("fecha", ""));

            // Obtener valores para los Spinners
            String categoria = extras.getString("categoria", "");
            String vendedor = extras.getString("vendedor", "");
            String ciudad = extras.getString("ciudad", "");

            // Establecer selección en los Spinners
            setSpinnerSelection(spinnerCategoria, categoria);
            setSpinnerSelection(spinnerVendedor, vendedor);
            setSpinnerSelection(spinnerCiudad, ciudad);

            facturaDao.getById(selectedFacturaId, factura -> {
                if (factura != null) {
                    selectedFactura = factura;
                } else {
                    Toast.makeText(this, "No se pudo recuperar la factura", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Botón para actualizar factura
        btnActualizarFactura.setOnClickListener(view -> {
            if (selectedFactura == null) {
                Toast.makeText(InformeFactura.this, "Por favor, espera a que se cargue la factura.", Toast.LENGTH_SHORT).show();
                return;
            }

            String numeroFactura = etNumeroFactura.getText().toString().trim();
            String monto = etMonto.getText().toString().trim();
            String categoria = spinnerCategoria.getSelectedItem().toString().trim();
            String vendedor = spinnerVendedor.getSelectedItem().toString().trim();
            String ciudad = spinnerCiudad.getSelectedItem().toString().trim();
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

        // Botón para eliminar factura
        btnEliminarfactura.setOnClickListener(v -> {
            if (selectedFacturaId != null) {
                facturaDao.delete(selectedFacturaId, isSuccess -> {
                    if (isSuccess) {
                        Toast.makeText(InformeFactura.this, "Factura eliminada", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(InformeFactura.this, "Error al eliminar la factura", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(InformeFactura.this, "Seleccione una factura para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para cancelar
        btnCancelarfactura.setOnClickListener(view -> {
            Intent intent = new Intent(InformeFactura.this, BuscarFactura.class);
            startActivity(intent);
        });
    }

    private void cargarDatosSpinners() {
        List<String> categorias = Arrays.asList("Audífonos", "DDS", "Celular", "Tablet", "Laptop");

        List<String> vendedores = Arrays.asList("Selecciona un vendedor", "Carlos Morales", "Tatiana Salas",
                "Camila Guzman","Sergio Torres", "Luisa Gomez", "Daniel Salinas", "Ana Florez",
                "Andres Escobar", "Juan Quiroz", "Marcela Ocampo");

        List<String> ciudades = Arrays.asList("Selecciona una ciudad", "Bogotá", "Medellín", "Cali",
                "Barranquilla", "Manizales");

        ArrayAdapter<String> categoriaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        ArrayAdapter<String> vendedorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vendedores);
        ArrayAdapter<String> ciudadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ciudades);

        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendedorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ciudadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoria.setAdapter(categoriaAdapter);
        spinnerVendedor.setAdapter(vendedorAdapter);
        spinnerCiudad.setAdapter(ciudadAdapter);
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }
}
