package com.pruebas;

public class ServicioPrecioImpl implements ServicioPrecio {

  private static final double TASA_IGV = 0.18;
  private static final double UMBRAL_DESCUENTO = 200.0;
  private static final double TASAS_DESCUENTO = 0.10;

  @Override
  public double calcularDescuento(double subtotal) {
    if (subtotal < 0) {
      throw new IllegalArgumentException("El subtotal no puede ser negativo");
    }
    return subtotal > UMBRAL_DESCUENTO ? subtotal * TASAS_DESCUENTO : 0.0;
  }

  @Override
  public double calcularImpuesto(double subtotal) {
    if (subtotal < 0) {
      throw new IllegalArgumentException("El subtotal no puede ser negativo");
    }
    return subtotal * TASA_IGV;
  }
}
