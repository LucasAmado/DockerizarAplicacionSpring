/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.proyectocontraoferta.model.Cupon;

/**
 * Este repositorio sirve para conectar el servicio de cupon con la base de datos
 * @author lamado
 *
 */

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Long> {

	/**
	 * Sirve para buscar los cupones que contegan el nombre pasado comon parámetro
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return Page <Cupon> que contienen ese nombre
	 */
	@Query("select c from Cupon c where lower(c.nombre) like lower(concat('%',?1,'%'))")
	public Page <Cupon> findByNombre(String nombre, Pageable pageable);
}
