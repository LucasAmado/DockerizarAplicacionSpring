/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Clase POJO que modela la entidad Producto (Ticket)
 * 
 * @author lamado
 *
 */
@Data
@AllArgsConstructor
@Entity
public class Producto {
	/**
	 * Id del producto
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * Nombre, descripción e imagen del producto
	 */
	private String nombre, descripcion, fileUrl;
	
	/**
	 * precioU: precio unitario
	 * precioOriginal: precio en otras plataformas
	 */
	private double precioU, precioOriginal;

	/**
	 * Cupón de descuento
	 */
	@ManyToOne
	private Cupon cupon;

	/**
	 * Fecha de caducidad del producto
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaCaducidad;

	/**
	 * Booleano que indica si el producto está caducado o no
	 */
	private boolean caducado;

	/**
	 * Líneas de pedido a las que pertenece un producto
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "producto")
	private List<LineaPedido> listaLineas;

	/**
	 * Estado del producto, entendiendo que 0 significa que se ha borrado
	 */
	private int estado;
	
	/**
	 * Categoría a la que pertenece un producto
	 */
	@ManyToOne
	private Categoria categoria;

	
	
	
	public Producto() {
		this.estado = 1;
	}




	public Producto(String nombre, String descripcion, String fileUrl, double precioU, double precioOriginal,
			Cupon cupon, LocalDate fechaCaducidad, boolean caducado, int estado, Categoria categoria) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fileUrl = fileUrl;
		this.precioU = precioU;
		this.precioOriginal = precioOriginal;
		this.cupon = cupon;
		this.fechaCaducidad = fechaCaducidad;
		this.caducado = caducado;
		this.estado = estado;
		this.categoria = categoria;
	}

}