package com.example.facturapro.data.model;

public class Factura {

    private String id;
    private String numeroFactura;
    private String monto;
    private String categoria;
    private String vendedor;
    private String ciudad;
    private String fecha;

    public Factura() {
    }

    public Factura(String numeroFactura, String monto, String categoria, String vendedor, String ciudad, String fecha) {
        this.numeroFactura = numeroFactura;
        this.monto = monto;
        this.categoria = categoria;
        this.vendedor = vendedor;
        this.ciudad = ciudad;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
