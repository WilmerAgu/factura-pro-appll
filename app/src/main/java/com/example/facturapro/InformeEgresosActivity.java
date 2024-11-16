package com.example.facturapro;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.facturapro.data.dao.EgresosDao;
import com.example.facturapro.data.model.EgresosModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class InformeEgresosActivity extends AppCompatActivity {

    private static final String TAG = "InformeEgresos";

    private EditText etNumeroFacturaEgreso, etMontoEgreso, etFechaEgresos;
    private Button btnActualizarEgreso, btnEliminarEgreso, btnCancelarEgreso;
    private EgresosDao egresosDao;
    private EgresosModel selectedEgreso;
    private String selectedEgresoId;
    private Spinner spCategoriaEgreso, spEstadoPago, spModoPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informe_egresos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Inicializar vistas
        etNumeroFacturaEgreso = findViewById(R.id.etNumeroFacturaEgreso);
        etMontoEgreso = findViewById(R.id.etMontoEgreso);
        spCategoriaEgreso = findViewById(R.id.spCategoriaEgreso);
        spEstadoPago = findViewById(R.id.spEstadoPago);
        spModoPago = findViewById(R.id.spModoPago);
        etFechaEgresos = findViewById(R.id.etFechaEgresos);
        btnActualizarEgreso = findViewById(R.id.btnActualizarEgreso);
        btnEliminarEgreso = findViewById(R.id.btnEliminarEgreso);
        btnCancelarEgreso = findViewById(R.id.btnCancelarEgreso);

        egresosDao = new EgresosDao(FirebaseFirestore.getInstance());

        // Cargar datos en los Spinners
        datosSpinners();

        // Obtener el ID de la factura seleccionada
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedEgresoId = extras.getString("id");

            if (selectedEgresoId == null || selectedEgresoId.isEmpty()) {
                Toast.makeText(this, "ID de egreso no válido", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            // Establecer valores en los EditText
            etNumeroFacturaEgreso.setText(extras.getString("numeroFacturaEgreso", ""));
            etMontoEgreso.setText(extras.getString("montoEgreso", ""));
            etFechaEgresos.setText(extras.getString("fechaEgreso", ""));

            // Obtener valores para los Spinners
            String categoriaEgreso = extras.getString("categoriaEgreso", "");
            String EstadoPago = extras.getString("estadoPago", "");
            String ModoPago = extras.getString("modoPago", "");

            // Establecer selección en los Spinners
            setSpinnerSelection(spCategoriaEgreso, categoriaEgreso);
            setSpinnerSelection(spEstadoPago, EstadoPago);
            setSpinnerSelection(spModoPago, ModoPago);

            egresosDao.getById(selectedEgresoId, egreso -> {
                if (egreso != null) {
                    selectedEgreso = egreso;
                } else {
                    Toast.makeText(this, "No se pudo recuperar la factura", Toast.LENGTH_SHORT).show();
                }
            });
        }
        // Botón para actualizar factura
        btnActualizarEgreso.setOnClickListener(view -> {
            if (selectedEgreso == null) {
                Toast.makeText(InformeEgresosActivity.this, "Por favor, espera a que se cargue la factura.", Toast.LENGTH_SHORT).show();
                return;
            }

            String numeroFacturaEgreso = etNumeroFacturaEgreso.getText().toString().trim();
            String montoEgreso = etMontoEgreso.getText().toString().trim();
            String categoriaEgreso = spCategoriaEgreso.getSelectedItem().toString().trim();
            String modoPago = spModoPago.getSelectedItem().toString().trim();
            String estadoPago = spEstadoPago.getSelectedItem().toString().trim();
            String fechaEgreso = etFechaEgresos.getText().toString().trim();

            if (numeroFacturaEgreso.isEmpty() || montoEgreso.isEmpty() || categoriaEgreso.isEmpty()
                    || modoPago.isEmpty() || estadoPago.isEmpty() || fechaEgreso.isEmpty()) {
                Toast.makeText(InformeEgresosActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedEgreso.setNumeroFacturaEgreso(numeroFacturaEgreso);
            selectedEgreso.setMontoEgreso(montoEgreso);
            selectedEgreso.setCategoriaEgreso(categoriaEgreso);
            selectedEgreso.setModoPago(modoPago);
            selectedEgreso.setEstadoPago(estadoPago);
            selectedEgreso.setFechaEgreso(fechaEgreso);

            egresosDao.update(selectedEgresoId, selectedEgreso, isSuccess -> {
                if (isSuccess) {
                    Toast.makeText(InformeEgresosActivity.this, "Egreso Actualizado", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(InformeEgresosActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            });
        });
        // Botón para eliminar factura
        btnEliminarEgreso.setOnClickListener(v -> {
            if (selectedEgresoId != null) {
                egresosDao.delete(selectedEgresoId, isSuccess -> {
                    if (isSuccess) {
                        Toast.makeText(InformeEgresosActivity.this, "Factura eliminada", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(InformeEgresosActivity.this, "Error al eliminar la factura", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(InformeEgresosActivity.this, "Seleccione una factura para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para cancelar
        btnCancelarEgreso.setOnClickListener(view -> {
            Intent intent = new Intent(InformeEgresosActivity.this, BuscarEgresosActivity.class);
            startActivity(intent);
        });

    }
    private void datosSpinners() {
        List<String> categoriaEgreso = Arrays.asList("Nomina", "Impuestos", "Insumos", "Marketing");
        List<String> modoPago = Arrays.asList("Efectivo", "Tarjeta de Crédito");
        List<String> estadoPago = Arrays.asList("Realizado", "Pendiente");

        // Crear adaptadores para los Spinners
        ArrayAdapter<String> categoriaEgresoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriaEgreso);
        ArrayAdapter<String> modoPagoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modoPago);
        ArrayAdapter<String> estadoPagoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estadoPago);

        // Establecer el diseño del dropdown
        categoriaEgresoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asignar adaptadores a los Spinners
        spCategoriaEgreso.setAdapter(categoriaEgresoAdapter);
        spModoPago.setAdapter(modoPagoAdapter);
        spEstadoPago.setAdapter(estadoPagoAdapter);
    }


    private void setSpinnerSelection(Spinner spinner, String value){
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }
}

