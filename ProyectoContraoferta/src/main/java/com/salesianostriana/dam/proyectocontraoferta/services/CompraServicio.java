/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.proyectocontraoferta.model.Compra;
import com.salesianostriana.dam.proyectocontraoferta.model.LineaPedido;
import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;
import com.salesianostriana.dam.proyectocontraoferta.repo.CompraRepository;
import com.salesianostriana.dam.proyectocontraoferta.services.base.BaseService;

import lombok.Getter;

/**
 * Clase que contiene los métodos crud de las compras
 * 
 * @author lamado
 *
 */
@Service
public class CompraServicio extends BaseService<Compra,Long,CompraRepository>{
	
	@Autowired
	private CompraRepository compraRepository;

	@Getter
	private List<LineaPedido> lineas = new ArrayList<>();
		
	public CompraServicio(CompraRepository repo) {
		this.compraRepository = repo;
	}

	/**
	 * @param c Compra que se quiere guardar
	 * @return compra guardada en la base de datos.
	 */
	public Compra add(Compra c) { 
		return compraRepository.save(c);
	}
	
	/**
	 * Este método sirve para guardar una compra editada
	 */
	public Compra edit(Compra c) {
		return compraRepository.save(c);
	}
	
	/**
	 * Este método sirve para borrar una compra
	 */
	public void delete(Compra c) {
		compraRepository.delete(c);
	}
	
	/**
	 * Este método permite encontrar todas las compras
	 * 
	 * @param pageable
	 * @return compras pageables
	 */
	public Page<Compra> findAllPageable(Pageable pageable) {
        return repositorio.findAll(pageable);
    }
	
	/**
	 * Este método permite encontrar todas las compras realizadas a partir del cliente que esté logeado en ese momento
	 * 
	 * @param logeado cliente que realiza la compra
	 * @param pageable
	 * @return compras hechas por el usuario logeado
	 */
	public Page <Compra> findComprasUsuarioConcreto(Usuario logeado, Pageable pageable){
		return repositorio.findByUsuario(logeado, pageable);
	}
}