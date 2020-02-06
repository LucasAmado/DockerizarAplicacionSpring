package com.salesianostriana.dam.proyectocontraoferta.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.ToString;

/**
 * Clase POJO que modela la entidad Compra
 * 
 * @author lamado
 *
 */
@Data
@Entity
public class Compra {

	/**
	 * Id de la compra
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Usuario que realiza la compra
	 */
	@ManyToOne
	private Usuario usuario;

	/**
	 * Nombre y apellidos del cliente que realiza la compra. Se cogen popr si se
	 * borra un cliente que haya hecho alguna compra
	 */
	private String nombreComprador, apellidosComprador;

	/**
	 * Fecha en la que se realiza la compra
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaCompra;

	/**
	 * Lista de líneas de pedido que hay en la compra
	 */
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL)
	private List<LineaPedido> listaLineas;

	/**
	 * Precio final de la compra
	 */
	private double totalCompra;

	public Compra() {
		this.fechaCompra = LocalDate.now();
	}

	public Compra(Usuario usuario, List<LineaPedido> listaLineas, double totalCompra, LocalDate fechaCompra,
			String nombreUser) {
		this.usuario = usuario;
		this.listaLineas = listaLineas;
		this.totalCompra = calcularTotalCarrito();
		this.fechaCompra = fechaCompra;
		this.nombreComprador = nombreUser;
	}

	public double calcularTotalCarrito() {
		for (LineaPedido linea : listaLineas) {
			totalCompra += linea.getTotalLinea();
		}
		return totalCompra;
	}

}
