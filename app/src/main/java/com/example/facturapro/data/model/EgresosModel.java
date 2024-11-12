package com.example.facturapro.data.model;

public class EgresosModel {

    private String id; // Para almacenar el ID del egreo
    private String numeroFacturaEgreso;
    private String montoEgreso;
    private String categoriaEgreso;
    private String modoPago;
    private String estadoPago;
    private String fechaEgreso;

    public EgresosModel() {
    }

    public EgresosModel(String id, String numeroFacturaEgreso, String montoEgreso, String categoriaEgreso, String modoPago, String estadoPago, String fechaEgreso) {
        this.id = id;
        this.numeroFacturaEgreso = numeroFacturaEgreso;
        this.montoEgreso = montoEgreso;
        this.categoriaEgreso = categoriaEgreso;
        this.modoPago = modoPago;
        this.estadoPago = estadoPago;
        this.fechaEgreso = fechaEgreso;
    }
// Getters y Setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroFacturaEgreso() {
        return numeroFacturaEgreso;
    }

    public void setNumeroFacturaEgreso(String numeroFacturaEgreso) {
        this.numeroFacturaEgreso = numeroFacturaEgreso;
    }

    public String getMontoEgreso() {
        return montoEgreso;
    }

    public void setMontoEgreso(String montoEgreso) {
        this.montoEgreso = montoEgreso;
    }

    public String getCategoriaEgreso() {
        return categoriaEgreso;
    }

    public void setCategoriaEgreso(String categoriaEgreso) {
        this.categoriaEgreso = categoriaEgreso;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getModoPago() {
        return modoPago;
    }

    public void setModoPago(String modoPago) {
        this.modoPago = modoPago;
    }

    public String getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(String fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }
}
