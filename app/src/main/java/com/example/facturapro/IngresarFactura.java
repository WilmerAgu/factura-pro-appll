package com.example.facturapro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facturapro.data.dao.FacturaDao;
import com.example.facturapro.data.model.FacturaModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class IngresarFactura extends AppCompatActivity {

    private Button btnGuardarFactura, btnCancelarfactura;
    private EditText etNumeroFactura, etMonto, etFecha;
    private Spinner spinnerCategoria, spinnerVendedor, spinnerCiudad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingresar_factura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView ivCerrarSesionIngresarFactura = findViewById(R.id.ivCerrarSesionIngresarFactura);

        btnCancelarfactura = findViewById(R.id.btnCancelarfactura);
        btnGuardarFactura = findViewById(R.id.btnGuardarFactura);

        etNumeroFactura = findViewById(R.id.etNumeroFactura);
        etMonto = findViewById(R.id.etMonto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria); // Spinner para la categoría
        spinnerVendedor = findViewById(R.id.spinnerVendedor); // Spinner para los vendedores
        spinnerCiudad = findViewById(R.id.spinnerCiudad); // Spinner para las ciudades
        etFecha = findViewById(R.id.etFecha);
        etFecha.setFocusable(false);
        etFecha.setClickable(true);

        // Configurar el Spinner con las categorías
        String[] categorias = {"Selecciona una categoría", "Audífonos", "DDS", "Celular", "Tablet", "Laptop"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        spinnerCategoria.setAdapter(adapter);

        // Configurar Spinner de Vendedores
        String[] vendedores = {"Selecciona un vendedor", "Carlos Morales", "Tatiana Salas", "Camila Guzman", "Sergio Torres", "Luisa Gomez", "Daniel Salinas", "Ana Florez", "Andres Escobar", "Juan Quiroz", "Marcela Ocampo"};
        ArrayAdapter<String> vendedorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vendedores);
        spinnerVendedor.setAdapter(vendedorAdapter);

        // Configurar Spinner de Ciudades
        String[] ciudades = {"Selecciona una ciudad", "Bogotá", "Medellín", "Cali", "Barranquilla", "Manizales"};
        ArrayAdapter<String> ciudadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ciudades);
        spinnerCiudad.setAdapter(ciudadAdapter);

        // Listener para manejar la selección del Spinner de categorías
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Listener para manejar la selección del Spinner de vendedores
        spinnerVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Listener para manejar la selección del Spinner de ciudades
        spinnerCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        etFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    IngresarFactura.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etFecha.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Acción del botón guardar factura
        btnGuardarFactura.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FacturaDao facturaDao = new FacturaDao(db);
            FacturaModel factura = new FacturaModel();
            factura.setNumeroFactura(etNumeroFactura.getText().toString());
            factura.setMonto(etMonto.getText().toString());

            // Obtener los valores de los Spinners
            String categoriaSeleccionada = spinnerCategoria.getSelectedItem().toString();
            String vendedorSeleccionado = spinnerVendedor.getSelectedItem().toString();
            String ciudadSeleccionada = spinnerCiudad.getSelectedItem().toString();

            // Guardar los valores seleccionados
            factura.setCategoria(categoriaSeleccionada);
            factura.setVendedor(vendedorSeleccionado);
            factura.setCiudad(ciudadSeleccionada);
            factura.setFecha(etFecha.getText().toString());

            // Insertar la factura en Firestore
            facturaDao.insert(factura, new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    // Acciones después de guardar
                }
            });

            // Limpiar los campos después de guardar
            etNumeroFactura.setText("");
            etMonto.setText("");
            etFecha.setText("");
            spinnerCategoria.setSelection(0); // Resetear el Spinner de categoría
            spinnerVendedor.setSelection(0); // Resetear el Spinner de vendedor
            spinnerCiudad.setSelection(0); // Resetear el Spinner de ciudad
        });

        // Acción del botón cancelar factura
        btnCancelarfactura.setOnClickListener(view -> {
            Intent intent = new Intent(IngresarFactura.this, Facturas.class);
            startActivity(intent);
        });

        // Acción para cerrar sesión
        ivCerrarSesionIngresarFactura.setOnClickListener(view -> {
            Intent intent = new Intent(IngresarFactura.this, Login.class);
            startActivity(intent);
        });
    }
}
