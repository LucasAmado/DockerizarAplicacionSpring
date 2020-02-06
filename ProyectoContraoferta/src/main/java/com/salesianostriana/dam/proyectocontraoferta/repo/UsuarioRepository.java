package com.salesianostriana.dam.proyectocontraoferta.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;

/**
 * Este repositorio sirve para conectar el servicio de usuario con la base de datos
 * @author lamado
 *
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	/**
	 * Permite encontrar un usaurio concreto a partir de su email
	 * 
	 * @param email email del usuario
	 * @return usuario asociado a ese email
	 */
	public Usuario findFirstByEmail(String email);
	
	/**
	 * Permite encotrar los uaurios cuyo nombre o apellidos contengan el nombre buscado
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return usuarios con ese nombre buscado
	 */
	@Query("select u from Usuario u where lower(u.nombre) like lower(concat('%',?1,'%')) or lower(u.apellidos) like lower(concat('%',?1,'%'))")
	public Page <Usuario> findByNombre (String nombre, Pageable pageable);
	
}
