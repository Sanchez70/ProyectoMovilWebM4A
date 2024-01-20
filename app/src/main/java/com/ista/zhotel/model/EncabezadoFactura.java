package com.ista.zhotel.model;

import java.util.Date;

public class EncabezadoFactura {
    private Long idEncabezado;
    private String cedula_persona;

    private Date fechaFactura;
    private Double total;

    public Long getIdEncabezado() {
        return idEncabezado;
    }

    public void setIdEncabezado(Long idEncabezado) {
        this.idEncabezado = idEncabezado;
    }

    public String getCedula_persona() {
        return cedula_persona;
    }

    public void setCedula_persona(String cedula_persona) {
        this.cedula_persona = cedula_persona;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
