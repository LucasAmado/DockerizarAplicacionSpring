package com.salesianostriana.dam.proyectocontraoferta.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Clase POJO que modela la entidad Usuario
 * 
 * @author lamado
 */

@Data
@Entity
@NoArgsConstructor
public class Usuario {
	/**
	 * Id del usuario
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * Nombre y apellidos del usuario
	 */
	private String nombre, apellidos;
	
	/**
	 * Correo electrónico del usuario
	 */
	@Column(unique = true)
	private String email;
	
	/**
	 * Contraseña
	 */
	private String password;
	
	/**
	 * Boolean que indica ai el usuario es admin o no
	 */
	private boolean admin;
	
	/**
	 * Compras realizadas por un usuario (cliente)
	 */
	@ToString.Exclude
	@OneToMany
	private List<Compra> listaCompras;

	public Usuario(String nombre, String apellidos, String email, String password, boolean admin) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password = password;
		this.admin = admin;
	}

}
