package com.example.facturapro.data.dao;

import android.util.Log;

import com.example.facturapro.data.model.FacturaModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturaDao {
    private static final String TAG = "FacturaDao";
    private static final String COLLECTION_NAME = "facturapro";

    private final FirebaseFirestore db;

    public FacturaDao(FirebaseFirestore db) {
        this.db = db;
    }

    // Método para insertar una nueva factura
    public void insert(FacturaModel factura, OnSuccessListener<String> listener) {
        Map<String, Object> facturaData = mapFacturaToData(factura);

        db.collection(COLLECTION_NAME)
                .add(facturaData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Factura insertada: " + documentReference.getId());
                    listener.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al insertar factura: ", e);
                    listener.onSuccess(null);
                });
    }

    // Método para actualizar una factura existente
    public void update(String id, FacturaModel factura, OnSuccessListener<Boolean> listener) {
        if (id == null || id.isEmpty()) {
            Log.e(TAG, "ID de factura es nulo o vacío. No se puede actualizar.");
            if (listener != null) listener.onSuccess(false);
            return;
        }
        Map<String, Object> facturaData = mapFacturaToData(factura);

        db.collection(COLLECTION_NAME)
                .document(id)
                .update(facturaData)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Factura actualizada correctamente con ID: " + id);
                    if (listener != null) listener.onSuccess(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al actualizar factura con ID: " + id, e);
                    if (listener != null) listener.onSuccess(false);
                });
    }


    // Método para obtener una factura por su ID
    public void getById(String id, OnSuccessListener<FacturaModel> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            FacturaModel factura = document.toObject(FacturaModel.class);
                            listener.onSuccess(factura);
                        } else {
                            listener.onSuccess(null);
                        }
                    } else {
                        Log.e(TAG, "Error al obtener la factura: ", task.getException());
                        listener.onSuccess(null);
                    }
                });
    }

    // Método para obtener todas las facturas
    public void getAllFacturas(OnSuccessListener<List<FacturaModel>> listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FacturaModel> facturaList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        FacturaModel factura = documentSnapshot.toObject(FacturaModel.class);
                        if (factura != null) {
                            factura.setId(documentSnapshot.getId()); // Guardar el ID
                            facturaList.add(factura);
                        }
                    }
                    Log.d(TAG, "Facturas cargadas: " + facturaList.size());
                    listener.onSuccess(facturaList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar facturas", e);
                    listener.onSuccess(null);
                });
    }

    // Método para eliminar una factura por su ID
    public void delete(String id, OnSuccessListener<Boolean> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .delete()
                .addOnSuccessListener(unused -> listener.onSuccess(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar factura: ", e);
                    listener.onSuccess(false);
                });
    }

    // Método privado para convertir FacturaModel a un Map<String, Object>
    private Map<String, Object> mapFacturaToData(FacturaModel factura) {
        Map<String, Object> facturaData = new HashMap<>();
        facturaData.put("numeroFactura", factura.getNumeroFactura());
        facturaData.put("monto", factura.getMonto());
        facturaData.put("categoria", factura.getCategoria());
        facturaData.put("vendedor", factura.getVendedor());
        facturaData.put("ciudad", factura.getCiudad());
        facturaData.put("fecha", factura.getFecha());
        return facturaData;
    }
    // Método para buscar facturas por fecha
    public void getFacturasPorFecha(String fecha, OnSuccessListener<List<FacturaModel>> listener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("fecha", fecha)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FacturaModel> facturaList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        FacturaModel factura = documentSnapshot.toObject(FacturaModel.class);
                        if (factura != null) {
                            factura.setId(documentSnapshot.getId());
                            facturaList.add(factura);
                        }
                    }
                    listener.onSuccess(facturaList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al buscar facturas por fecha", e);
                    listener.onSuccess(null);
                });
    }

    // Método para buscar factura por número de factura
    public void getFacturaPorNumero(String numeroFactura, OnSuccessListener<FacturaModel> listener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("numeroFactura", numeroFactura)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        FacturaModel factura = documentSnapshot.toObject(FacturaModel.class);
                        if (factura != null) {
                            factura.setId(documentSnapshot.getId());
                            listener.onSuccess(factura);
                        } else {
                            listener.onSuccess(null);
                        }
                    } else {
                        listener.onSuccess(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al buscar factura por número", e);
                    listener.onSuccess(null);
                });
    }

}
