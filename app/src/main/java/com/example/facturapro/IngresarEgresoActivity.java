package com.example.facturapro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facturapro.data.dao.EgresosDao;
import com.example.facturapro.data.model.EgresosModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class IngresarEgresoActivity extends AppCompatActivity {

    private Button btnGuardarEgreso, btnCancelarEgreso;
    private EditText etNumeroFacturaEgreso, etMontoEgreso, etFechaEgreso;
    private Spinner spCategoriaEgreso, spEstadoPago, spModoPago;
    private ImageView ivCerrarSesionIngresarEgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingresar_egreso);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView ivCerrarSesionIngresarEgreso = findViewById(R.id.ivCerrarSesionIngresarEgreso);

        btnCancelarEgreso = findViewById(R.id.btnCancelarEgreso);
        btnGuardarEgreso = findViewById(R.id.btnGuardarEgreso);

        etNumeroFacturaEgreso = findViewById(R.id.etNumeroFacturaEgreso);
        etMontoEgreso = findViewById(R.id.etMontoEgreso);
        spCategoriaEgreso = findViewById(R.id.spCategoriaEgreso);
        spEstadoPago = findViewById(R.id.spEstadoPago);
        spModoPago = findViewById(R.id.spModoPago);
        etFechaEgreso = findViewById(R.id.etFechaEgreso);
        etFechaEgreso.setFocusable(false);
        etFechaEgreso.setClickable(true);

        // Configurar el Spinner con las categorías
        String[] categoriaEgresos = {"Tipo de Gasto", "Nomina", "Impuestos", "Insumos", "Marketing"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriaEgresos);
        spCategoriaEgreso.setAdapter(adapter);

        // Configurar Spinner de modo de pago
        String[] modoPago = {"Selecciona metodo de pago", "Efectivo", "Tarjeta de Crédito"};
        ArrayAdapter<String> vendedorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modoPago);
        spEstadoPago.setAdapter(vendedorAdapter);


        // Configurar Spinner de estado de pago
        String[] estadoPago= {"Selecciona el estado de pago","Realizado", "Pendiente"};
        ArrayAdapter<String> ciudadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, estadoPago);
        spModoPago.setAdapter(ciudadAdapter);

        // Listener para manejar la selección del Spinner de categorías
        spCategoriaEgreso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();

                // Si el usuario no seleccionó una categoría válida
                if (position == 0) {
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    ((TextView) view).setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
        // Listener para manejar la selección del Spinner de estado de pago
        spEstadoPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String vendedorSeleccionado = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    ((TextView) view).setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // Listener para manejar la selección del Spinner de estado de pago
        spModoPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ciudadSeleccionada = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    ((TextView) view).setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
        // Configurar el campo de fecha
        etFechaEgreso.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    IngresarEgresoActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etFechaEgreso.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });
        // Acción del botón guardar factura
        btnGuardarEgreso.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            EgresosDao egresosDao = new EgresosDao(db);
            EgresosModel egreso = new EgresosModel();
            egreso.setNumeroFacturaEgreso(etNumeroFacturaEgreso.getText().toString());
            egreso.setMontoEgreso(etMontoEgreso.getText().toString());

            // Obtener los valores de los Spinners
            String categoriaEgresoSeleccionada = spCategoriaEgreso.getSelectedItem().toString();
            String modoPagoSeleccionado = spModoPago.getSelectedItem().toString();
            String estadoPagoSeleccionado = spEstadoPago.getSelectedItem().toString();
            egreso.setFechaEgreso(etFechaEgreso.getText().toString());

            // Guardar los valores seleccionados
            egreso.setCategoriaEgreso(categoriaEgresoSeleccionada);
            egreso.setModoPago(modoPagoSeleccionado);
            egreso.setEstadoPago(estadoPagoSeleccionado);


            // Insertar la factura en Firestore
            egresosDao.insert(egreso, new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    // Acciones después de guardar
                }
            });

            // Limpiar los campos después de guardar
            etNumeroFacturaEgreso.setText("");
            etMontoEgreso.setText("");
            etFechaEgreso.setText("");
            spCategoriaEgreso.setSelection(0);
            spModoPago.setSelection(0);
            spEstadoPago.setSelection(0);
        });

        // Acción del botón cancelar factura
        btnCancelarEgreso.setOnClickListener(view -> {
            Intent intent = new Intent(IngresarEgresoActivity.this, EgresosActivity.class);
            startActivity(intent);
        });

        // Acción para cerrar sesión
        ivCerrarSesionIngresarEgreso.setOnClickListener(view -> {
            Intent intent = new Intent(IngresarEgresoActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }
}