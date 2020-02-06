package com.salesianostriana.dam.proyectocontraoferta.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.proyectocontraoferta.model.Compra;
import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;

/**
 * Este repositorio sirve para conectar el servicio de compra con la base de datos
 * @author lamado
 *
 */
@Repository
public interface CompraRepository extends JpaRepository <Compra, Long>{
	
	/**
	 * Permite buscar las compras realizadas por el usuario que está logeado
	 * 
	 * @param logeado usuario que ha hecho las compras
	 * @param pageable
	 * @return page de compras del usuario logeado
	 */
	public Page <Compra> findByUsuario(Usuario logeado, Pageable pageable);
	
}
