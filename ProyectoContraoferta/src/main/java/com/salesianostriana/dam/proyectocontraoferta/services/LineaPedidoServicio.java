/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.proyectocontraoferta.model.LineaPedido;
import com.salesianostriana.dam.proyectocontraoferta.repo.LineaPedidoRepository;
import com.salesianostriana.dam.proyectocontraoferta.services.base.BaseService;

/**
 * Clase que contiene los métodos crud de las líneas de pedido
 * 
 * @author lamado
 *
 */
@Service
public class LineaPedidoServicio extends BaseService<LineaPedido, Long, LineaPedidoRepository> {

	@Autowired
	private LineaPedidoRepository lineaPedidoRepository;

	public LineaPedidoServicio(LineaPedidoRepository repo) {
		this.lineaPedidoRepository = repo;
	}

	/**
	 * Este método permite añadir una nueva línea de pedido
	 * 
	 * @param lp linea de pedido
	 * @return linea de pedido guardada en la base de datos
	 */
	public LineaPedido add(LineaPedido lp) {
		return lineaPedidoRepository.save(lp);
	}

	/**
	 * Este método permite editar una línea de pedido
	 */
	public LineaPedido edit(LineaPedido lp) {
		return lineaPedidoRepository.save(lp);
	}

	/**
	 * Este método permite eliminar una línea de pedido
	 */
	public void delete(LineaPedido lp) {
		lineaPedidoRepository.delete(lp);
	}
}
