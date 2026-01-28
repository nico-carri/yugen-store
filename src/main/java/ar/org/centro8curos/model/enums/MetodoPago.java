package ar.org.centro8curos.model.enums;

public enum MetodoPago {
    TARJETA_CREDITO("Tarjeta de Credito"),
    TARJETA_DEBITO("Tarjeta de Debito"),
    TRANSFERENCIA("Transferencia"),
    EFECTIVO("Efectivo");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}