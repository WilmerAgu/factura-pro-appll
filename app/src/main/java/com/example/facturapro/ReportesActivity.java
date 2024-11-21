
package com.example.facturapro;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturapro.data.adapter.EgresosAdapter;
import com.example.facturapro.data.adapter.FacturaAdapter;
import com.example.facturapro.data.dao.EgresosDao;
import com.example.facturapro.data.dao.FacturaDao;
import com.example.facturapro.data.model.EgresosModel;
import com.example.facturapro.data.model.FacturaModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReportesActivity extends AppCompatActivity {

    private FacturaDao facturaDao;
    private EgresosDao egresosDao;
    private RecyclerView rvBusqueda;
    private FacturaAdapter facturaAdapter;
    private EgresosAdapter egresosAdapter;
    private List<FacturaModel> listaFacturas = new ArrayList<>();
    private List<EgresosModel> listaFacturasEgresos = new ArrayList<>();

    private ImageView pdfIcon, excelIcon;

    private androidx.appcompat.widget.SearchView shFecha, shNumeroFactura, shFechaEgresos, shNumeroFacturaEgresos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reportes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Verificar permisos en tiempo de ejecución
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // Inicializar Firebase Firestore y FacturaDao
        facturaDao = new FacturaDao(FirebaseFirestore.getInstance());
        egresosDao = new EgresosDao(FirebaseFirestore.getInstance());

        // Inicializar RecyclerView
        rvBusqueda = findViewById(R.id.rvBuqueda);
        rvBusqueda.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar Adapter y configurarlo
        facturaAdapter = new FacturaAdapter(listaFacturas);
        rvBusqueda.setAdapter(facturaAdapter);

        egresosAdapter = new EgresosAdapter(listaFacturasEgresos);
        rvBusqueda.setAdapter(egresosAdapter);

        // Configurar búsqueda
        shFecha = findViewById(R.id.shFecha);
        shNumeroFactura = findViewById(R.id.shNumeroFactura);

        shFechaEgresos = findViewById(R.id.shFechaEgresos);
        shNumeroFacturaEgresos = findViewById(R.id.shNumeroFacturaEgresos);

        // Inicializar descargas
        pdfIcon = findViewById(R.id.pdfIcon);
        excelIcon = findViewById(R.id.excelIcon);

        pdfIcon.setOnClickListener(view -> downloadFile("pdf"));
        excelIcon.setOnClickListener(view -> downloadFile("excel"));

        // Listener para búsqueda por fecha
        shFecha.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarPorFecha(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Listener para búsqueda por fecha egresos
        shFechaEgresos.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarPorFechaEgreso(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Listener para búsqueda por número de factura
        shNumeroFacturaEgresos.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarPorNumeroEgreso(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Listener para búsqueda por número de factura egresos
        shNumeroFactura.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarPorNumeroFactura(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Listener para el click en los elementos del RecyclerView
        facturaAdapter.setOnItemClickListener(factura -> {
            Toast.makeText(this, "Factura seleccionada: " + factura.getNumeroFactura(), Toast.LENGTH_SHORT).show();
        });

        // Listener para el click en los elementos del RecyclerView
        egresosAdapter.setOnItemClickListener(factura -> {
            Toast.makeText(this, "Factura seleccionada: " + factura.getNumeroFacturaEgreso(), Toast.LENGTH_SHORT).show();
        });
    }

    //Método para buscar facturas por número de factura
    private void buscarPorNumeroFactura(String numeroFactura) {
        facturaDao.getAllFacturas(facturas -> {
            if (facturas != null) {
                Log.d("Reportes", "Facturas obtenidas: " + facturas.size());
                List<FacturaModel> resultado = new ArrayList<>();
                for (FacturaModel factura : facturas) {
                    Log.d("Reportes", "Factura: " + factura.getNumeroFactura());
                    if (factura.getNumeroFactura().equals(numeroFactura)) {
                        resultado.add(factura);
                    }
                }
                actualizarLista(resultado);
            } else {
                Log.e("Reportes", "No se encontraron facturas para el número: " + numeroFactura);
                Toast.makeText(this, "No se encontraron facturas para el número indicado", Toast.LENGTH_SHORT).show();
            }

            // Resetea el campo de búsqueda
            shFechaEgresos.setQuery("", false);
            shFechaEgresos.clearFocus();
        });
    }

    //Método para buscar facturas por número de factura egresos
    private void buscarPorNumeroEgreso(String numeroFacturaEgreso) {
        egresosDao.getAllEgresos(egresos -> {
            if (egresos != null) {
                List<EgresosModel> resultado = new ArrayList<>();
                for (EgresosModel egreso : egresos) {
                    if (egreso.getNumeroFacturaEgreso().equals(numeroFacturaEgreso)) {
                        resultado.add(egreso);
                    }
                }
                actualizarListaEgresos(resultado);
            } else {
                Log.e("Reportes", "No se encontraron facturas para el número: " + numeroFacturaEgreso);
                Toast.makeText(this, "No se encontraron facturas para el número indicado", Toast.LENGTH_SHORT).show();
            }

            // Resetea el campo de búsqueda
            shNumeroFacturaEgresos.setQuery("", false);
            shNumeroFacturaEgresos.clearFocus();
        });
    }

    //Método para buscar facturas por fecha
    private void buscarPorFecha(String fecha) {
        facturaDao.getAllFacturas(facturas -> {
            if (facturas != null) {
                List<FacturaModel> resultado = new ArrayList<>();
                for (FacturaModel factura : facturas) {
                    if (factura.getFecha().equals(fecha)) {
                        resultado.add(factura);
                    }
                }
                actualizarLista(resultado);
            } else {
                Log.e("Reportes", "No se encontraron facturas para la fecha: " + fecha);
                Toast.makeText(this, "No se encontraron facturas para la fecha indicada", Toast.LENGTH_SHORT).show();
            }

            // Resetea el campo de búsqueda
            shFecha.setQuery("", false);
            shFecha.clearFocus();
        });
    }

    //Método para buscar facturas por fecha egresos
    private void buscarPorFechaEgreso(String fecha) {
        egresosDao.getAllEgresos(egresos -> {
            if (egresos != null) {
                List<EgresosModel> resultado = new ArrayList<>();
                for (EgresosModel egreso : egresos) {
                    if (egreso.getFechaEgreso().equals(fecha)) {
                        resultado.add(egreso);
                    }
                }
                actualizarListaEgresos(resultado);
            } else {
                Log.e("Reportes", "No se encontraron facturas de egresos para la fecha: " + fecha);
                Toast.makeText(this, "No se encontraron facturas egresos para la fecha indicada", Toast.LENGTH_SHORT).show();
            }

            // Resetea el campo de búsqueda
            shNumeroFactura.setQuery("", false);
            shNumeroFactura.clearFocus();
        });
    }

    // Método para descargar el archivo
    private void downloadFile(String fileType) {
        // Simular URL del archivo a descargar
        String url = "";
        if ("pdf".equals(fileType)) {
            url = "https://example.com/factura_seleccionada.pdf";
        } else if ("excel".equals(fileType)) {
            url = "https://example.com/factura_seleccionada.xlsx";
        }

        // Usar DownloadManager para descargar el archivo
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            // Configurar título y descripción de la notificación
            request.setTitle("Descargando archivo");
            request.setDescription("Descargando " + fileType.toUpperCase());
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Configurar el destino del archivo descargado
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "factura_seleccionada." + fileType);

            // Iniciar la descarga
            downloadManager.enqueue(request);
        } else {
            Toast.makeText(this, "Error al iniciar la descarga", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para actualizar la lista en el RecyclerView
    private void actualizarLista(List<FacturaModel> nuevaLista) {
        rvBusqueda.setAdapter(facturaAdapter);
        listaFacturas.clear();
        listaFacturas.addAll(nuevaLista);
        facturaAdapter.notifyDataSetChanged();
    }

    private void actualizarListaEgresos(List<EgresosModel> nuevaLista) {
        rvBusqueda.setAdapter(egresosAdapter);
        listaFacturasEgresos.clear();
        listaFacturasEgresos.addAll(nuevaLista);
        egresosAdapter.notifyDataSetChanged();
    }
}
