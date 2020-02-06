package com.salesianostriana.dam.proyectocontraoferta.model;

/**
 * Clase POJO que modela la entidad Linea Pedido
 * 
 * @author lamado
 *
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LineaPedido {
	
	/**
	 * Id de la línea de pedido
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * Cantidad de productos que hay en una línea
	 */
	private int cantidad;
	
	/**
	 *Producto que hay en la línea
	 */
	@ManyToOne
	private Producto producto;
	
	/**
	 * TotalLinea es el precio total de una línea de pedido.
	 * DescuentoLinea es el total que se descuenta en la línea en caso de que se aplique un cupón
	 */
	private double totalLinea, descuentoLinea;
	
	/**
	 * Compra a la que pertenecen las líneas
	 */
	@ManyToOne
	private Compra compra;

	public LineaPedido(Producto producto, int cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
		this.totalLinea = calcularPrecioLinea();
	}

	/**
	 * Este método sirve para calcular el total de la línea de pedido y el descuentoLinea
	 * 
	 * @return precio final de la línea de pedido
	 */
	public double calcularPrecioLinea() {
		double total = 0;
		descuentoLinea = 0;
		total += producto.getPrecioU() * cantidad;

		if (producto.getCupon().getId() != 1 && cantidad >= producto.getCupon().getUnidadesMin()) {
			descuentoLinea = (total * producto.getCupon().getDescuento() / 100);
			total -= descuentoLinea;
		}

		return total;

	}
}
