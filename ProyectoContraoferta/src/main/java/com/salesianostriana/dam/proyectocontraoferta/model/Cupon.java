package com.salesianostriana.dam.proyectocontraoferta.model;

/**
 * Clase POJO que modela la entidad Cupon
 * 
 * @author lamado
 *
 */

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
public class Cupon {
	
	/**
	 * Id del cupón
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Nombre del cupón
	 */
	@Column(unique = true)
	private String nombre;

	/**
	 * Lista de productos asociadas a un cupón de descuento
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "cupon")
	private List<Producto> listaProductos;

	/**
	 * Unidades mínimas requeridas para que se aplique el cupón
	 */
	private int unidadesMin;

	/**
	 * Porcentaje de descuento que se aplica
	 */
	private double descuento;

	public Cupon(String nombre, int unidadesMin, double descuento) {
		this.nombre = nombre;
		this.unidadesMin = unidadesMin;
		this.descuento = descuento;
	}

	public Cupon(String nombre) {
		this.nombre = nombre;
		listaProductos = new ArrayList<>();
	}

	public void add(List<Producto> lista) {
		for (Producto p : lista) {
			lista.add(p);
		}

	}

}
