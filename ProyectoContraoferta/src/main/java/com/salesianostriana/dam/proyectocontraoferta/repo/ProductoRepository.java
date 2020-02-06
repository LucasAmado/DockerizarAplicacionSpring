package com.salesianostriana.dam.proyectocontraoferta.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.proyectocontraoferta.model.Producto;

/**
 * Este repositorio sirve para conectar el servicio de producto con la base de datos
 * @author lamado
 *
 */

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

	/**
	 * Permite encontrar los productos que pertenecen a una categoría en concreto (encontrada por el parámetro nombte), que además no están caducados ni borrados
	 * 
	 * @param nombre nombre de la categoría buscada
	 * @param pageable
	 * @return productos de esa categoría sin caducar ni borrados
	 */
	@Query("select p from Producto p where p.fechaCaducidad >= CURRENT_DATE and lower(p.categoria.nombre) like lower(?1) and p.estado != 0")
	public Page<Producto> findByCategoriaNombrePageable(String nombre, Pageable pageable);

	/**
	 * Permite encontrar los productos que no estén caducados ni borrados
	 * 
	 * @param pageable
	 * @return productos sin caducar ni borrados
	 */
	@Query("select p from Producto p where p.fechaCaducidad >= CURRENT_DATE and p.estado != 0")
	public Page<Producto> findSinCaducar(Pageable pageable);

	/**
	 * Permite encontrar los productos que no están caducados ni borrados que contengan el parámetro nombre
	 * 
	 * @param nombre nombre del producto buscado
	 * @param pageable
	 * @return productos que contienen el nombre buscado que no estén caducados ni borrados
	 */
	@Query("select p from Producto p where p.fechaCaducidad >= CURRENT_DATE and lower(p.nombre) like lower(concat('%',?1,'%')) and p.estado != 0")
	public Page<Producto> findSinCaducarByNombre(String nombre, Pageable pageable);

	/**
	 * Permite encontrar los productos que estén caducados pero no borrados
	 * 
	 * @param pageable
	 * @return productos caducados pero no borrados
	 */
	@Query("select p from Producto p where p.fechaCaducidad < CURRENT_DATE and p.estado != 0")
	public Page<Producto> findCaducados(Pageable pageable);

	/**
	 * Permite encontrar los productos que estén caducados, no estén borrados y contengan el nombre buscado 
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return productos caducados, no borrados y que cntengan ese nombre
	 */
	@Query("select p from Producto p where p.fechaCaducidad < CURRENT_DATE and lower(p.nombre) like lower(concat('%',?1,'%')) and p.estado != 0")
	public Page<Producto> findCaducadosByNombre(String nombre, Pageable pageable);

	/**
	 * Permite encontrar todos los productos borrados
	 * 
	 * @param pageable
	 * @return productos borrados
	 */
	@Query("select p from Producto p where p.estado = 0")
	public Page<Producto> findAllBorrados(Pageable pageable);

	/**
	 * Permite encontrar todos los productos que estén borrados y contengan el nombre buscado
	 * 
	 * @param nombre nombre del producto buscado
	 * @param pageable
	 * @return productos borrados que contengan ese nombre
	 */
	@Query("select p from Producto p where p.estado = 0 and lower(p.nombre) like lower(concat('%',?1,'%'))")
	public Page<Producto> findAllBorradosByNombre(String nombre, Pageable pageable);

	/**
	 * Permite encontrar todos los productos que no estén borrados
	 * 
	 * @param pageable
	 * @return productos no borrados
	 */
	@Query("select p from Producto p where p.estado != 0")
	public Page<Producto> findNoBorrados(Pageable pageable);

	/**
	 * Permite encotrar los productos que no estén borrados y contengan el nombre buscado
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return productos sin borrar que contenagn ese nombre
	 */
	@Query("select p from Producto p where lower(p.nombre) like lower(concat('%',?1,'%')) and p.estado != 0")
	public Page<Producto> findNoBorradosByNombre(String nombre, Pageable pageable);

}