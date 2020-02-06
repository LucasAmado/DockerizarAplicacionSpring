/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.proyectocontraoferta.model.LineaPedido;

/**
 * Este repositorio sirve para conectar el servicio de linea de pedido con la base de datos
 * @author lamado
 *
 */
@Repository
public interface LineaPedidoRepository extends JpaRepository <LineaPedido, Long> {

}
