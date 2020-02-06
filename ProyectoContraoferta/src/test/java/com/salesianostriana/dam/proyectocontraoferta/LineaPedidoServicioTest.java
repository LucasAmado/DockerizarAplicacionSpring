/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salesianostriana.dam.proyectocontraoferta.model.LineaPedido;
import com.salesianostriana.dam.proyectocontraoferta.model.Producto;
import com.salesianostriana.dam.proyectocontraoferta.services.CategoriaServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.CuponServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.LineaPedidoServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.ProductoServicio;

/**
 * Esta clase contiene las pruebas que comprueban que el servicio de la LineaPedido funciona correctamente
 * 
 * @author lamado
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class LineaPedidoServicioTest {

	@Autowired
	EntityManager entityManager;

	@Autowired
	LineaPedidoServicio lineaPedidoServicio;
	
	@Autowired
	CategoriaServicio categoriaServicio;

	@Autowired
	ProductoServicio productoServicio;
	
	@Autowired
	CuponServicio cuponServicio;

	/**
	 * Añade un producto de prueba a la línea de pedido
	 */
	@Test
	public void test_addLinea() {
		Producto p = new Producto("prueba", "producto prueba", "imgPrueba.jpg", 19.99, 12.50, cuponServicio.findById((long) 1), LocalDate.of(2020, 10, 24), false, 0, categoriaServicio.findById((long) 0));
		
		productoServicio.save(p);
		
		LineaPedido lp = new LineaPedido(p,1);
		
		lineaPedidoServicio.add(lp);
		
		assertEquals(true, lineaPedidoServicio.findAll().contains(lp));
	}

	/**
	 * Elimina una línea de pedido
	 */
	@Test
	public void test_deleteLinea() {
		Producto borrar = new Producto("pruebaBorrado", "producto prueba", "imgPrueba.jpg", 19.99, 12.50, cuponServicio.findById((long) 59), LocalDate.of(2019, 12, 5), false, 0, categoriaServicio.findById((long) 55));

		productoServicio.save(borrar);

		LineaPedido lp = new LineaPedido(borrar, 1);

		lineaPedidoServicio.add(lp);

		lineaPedidoServicio.delete(lp);

		assertEquals(false, lineaPedidoServicio.findAll().contains(lp));
	}

	/**
	 * Se encarga de buscar todas las líneas de pedido que hay en la base de datos
	 */
	@Test
	public void test_FindAllLineas() {

		TypedQuery<LineaPedido> query = entityManager.createQuery("select lP from LineaPedido lP", LineaPedido.class);

		List<LineaPedido> lineas = query.getResultList();

		assertEquals(lineas, lineaPedidoServicio.findAll());
	}

}
