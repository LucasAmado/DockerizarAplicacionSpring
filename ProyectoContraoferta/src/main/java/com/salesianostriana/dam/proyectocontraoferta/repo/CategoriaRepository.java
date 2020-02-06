/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.proyectocontraoferta.model.Categoria;

/**
 * Este repositorio sirve para conectar el servicio de categoría con la base de datos
 * @author lamado
 *
 */

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	/**
	 * Permite encontrar una categoría a partir de su nombre
	 * 
	 * @param nombre nombre de la categoría
	 * @return categoría con ese nombre
	 */
	public Categoria findByNombre(String nombre);
	
	/**
	 * Sirve para buscar las catergorías que contengan el parámetro nombre
	 * 
	 * @param nombre nombre de la categoría
	 * @param pageable
	 * @return page de categorías
	 */
	@Query("select c from Categoria c where lower(c.nombre) like lower(concat('%',?1,'%'))")
	public Page <Categoria> findByNombrePageable(String nombre, Pageable pageable);
}
