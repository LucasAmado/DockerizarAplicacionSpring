/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Clase POJO que modela la entidad Compra
 * 
 * @author lamado
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
	/**
	 * Id de la categoría
	 */
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * Nombre de la categoría que debe ser único
	 */
	@Column(unique = true)
	private String nombre;

	/**
	 * Lista de productos asociados a una categoría
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "categoria")
	private List<Producto> listaProductos;
	
	
	
	public Categoria(String nombre) {
		this.nombre = nombre;
		listaProductos = new ArrayList<>();
	}

	public void add(List<Producto> lista) {
		for (Producto p : lista) {
			lista.add(p);
		}

	}

}
