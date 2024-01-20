package com.ista.zhotel.model;

import java.util.Date;

public class DetalleFactura {
    private Long idDetalleFac;
    private Long idReserva;
    private Double subTotal;
    private Long idEncabezado;

    public Long getIdDetalleFac() {
        return idDetalleFac;
    }

    public void setIdDetalleFac(Long idDetalleFac) {
        this.idDetalleFac = idDetalleFac;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Long getIdEncabezado() {
        return idEncabezado;
    }

    public void setIdEncabezado(Long idEncabezado) {
        this.idEncabezado = idEncabezado;
    }
}
