package com.example.facturapro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facturapro.data.dao.FacturaDao;
import com.example.facturapro.data.model.Factura;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class IngresarFactura extends AppCompatActivity {

    private Button btnGuardarFactura, btnCancelarfactura;
    private EditText etNumeroFactura, etMonto, etCategoria, etVendedor, etCiudad, etFecha;


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

        Button btnCancelarFactura = findViewById(R.id.btnCancelarfactura);
        Button btnGuardarFactura = findViewById(R.id.btnGuardarFactura);

        etNumeroFactura = findViewById(R.id.etNumeroFactura);
        etMonto = findViewById(R.id.etMonto);
        etCategoria = findViewById(R.id.etCategoria);
        etVendedor = findViewById(R.id.etVendedor);
        etCiudad = findViewById(R.id.etCiudad);
        etFecha = findViewById(R.id.etFecha);
        etFecha.setFocusable(false);
        etFecha.setClickable(true);

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        IngresarFactura.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Formatear la fecha seleccionada y mostrarla en el EditText
                                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                etFecha.setText(selectedDate);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
        btnGuardarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                FacturaDao facturaDao = new FacturaDao(db);
                Factura factura = new Factura();
                factura.setNumeroFactura(etNumeroFactura.getText().toString());
                factura.setMonto(etMonto.getText().toString());
                factura.setCategoria(etCategoria.getText().toString());
                factura.setVendedor(etVendedor.getText().toString());
                factura.setCiudad(etCiudad.getText().toString());
                factura.setFecha(etFecha.getText().toString());

                facturaDao.insert(factura, new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }
                });
                etNumeroFactura.setText("");
                etMonto.setText("");
                etCategoria.setText("");
                etVendedor.setText("");
                etCiudad.setText("");
                etFecha.setText("");

            }
        });



        btnCancelarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresarFactura.this, Facturas.class);
                startActivity(intent);
            }
        });

        ivCerrarSesionIngresarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresarFactura.this, Login.class);
                startActivity(intent);
            }
        });

    }
}