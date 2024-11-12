package com.example.facturapro.data.dao;

import android.util.Log;

import com.example.facturapro.data.model.EgresosModel;
import com.example.facturapro.data.model.FacturaModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EgresosDao {
    private static final String TAG = "EgresosDao";
    private static final String COLLECTION_NAME = "egresos-factura";

    private final FirebaseFirestore db;

    public EgresosDao(FirebaseFirestore db) {
        this.db = db;
    }

    // Método para insertar una nueva factura
    public void insert(EgresosModel egreso, OnSuccessListener<String> listener) {
        Map<String, Object> egresoData = mapEgresoToData(egreso);

        db.collection(COLLECTION_NAME)
                .add(egresoData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Egreso insertado: " + documentReference.getId());
                    listener.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al insertar egreso: ", e);
                    listener.onSuccess(null);
                });
    }

    // Método para actualizar una factura existente
    public void update(String id, EgresosModel egreso, OnSuccessListener<Boolean> listener) {
        if (id == null || id.isEmpty()) {
            Log.e(TAG, "ID de egreso es nulo o vacío. No se puede actualizar.");
            if (listener != null) listener.onSuccess(false);
            return;
        }
        Map<String, Object> egresoData = mapEgresoToData(egreso);

        db.collection(COLLECTION_NAME)
                .document(id)
                .update(egresoData)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Egreso actualizado correctamente con ID: " + id);
                    if (listener != null) listener.onSuccess(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al actualizar egreso con ID: " + id, e);
                    if (listener != null) listener.onSuccess(false);
                });
    }


    // Método para obtener una factura por su ID
    public void getById(String id, OnSuccessListener<EgresosModel> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            EgresosModel egreso = document.toObject(EgresosModel.class);
                            listener.onSuccess(egreso);
                        } else {
                            listener.onSuccess(null);
                        }
                    } else {
                        Log.e(TAG, "Error al obtener el egreso: ", task.getException());
                        listener.onSuccess(null);
                    }
                });
    }

    // Método para obtener todas las facturas
    public void getAllEgresos(OnSuccessListener<List<EgresosModel>> listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EgresosModel> egresoList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        EgresosModel egreso = documentSnapshot.toObject(EgresosModel.class);
                        if (egreso != null) {
                            egreso.setId(documentSnapshot.getId()); // Guardar el ID
                            egresoList.add(egreso);
                        }
                    }
                    Log.d(TAG, "Egresos cargados: " + egresoList.size());
                    listener.onSuccess(egresoList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar egresos", e);
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
                    Log.e(TAG, "Error al eliminar egreso: ", e);
                    listener.onSuccess(false);
                });
    }

    // Método privado para convertir FacturaModel a un Map<String, Object>
    private Map<String, Object> mapEgresoToData(EgresosModel egreso) {
        Map<String, Object> egresoData = new HashMap<>();
        egresoData.put("numeroFacturaEgreso", egreso.getNumeroFacturaEgreso());
        egresoData.put("montoEgreso", egreso.getMontoEgreso());
        egresoData.put("categoriaEgreso", egreso.getCategoriaEgreso());
        egresoData.put("modoPago", egreso.getModoPago());
        egresoData.put("estadoPago", egreso.getEstadoPago());
        egresoData.put("fechaEgreso", egreso.getFechaEgreso());
        return egresoData;
    }
}
