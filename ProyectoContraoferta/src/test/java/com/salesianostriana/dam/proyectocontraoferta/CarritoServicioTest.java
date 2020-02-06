package com.salesianostriana.dam.proyectocontraoferta;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salesianostriana.dam.proyectocontraoferta.model.LineaPedido;
import com.salesianostriana.dam.proyectocontraoferta.model.Producto;
import com.salesianostriana.dam.proyectocontraoferta.services.CarritoServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.ProductoServicio;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarritoServicioTest {
	
	@Autowired
	CarritoServicio carritoServicio;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	ProductoServicio productoServicio;
	
	/**
	 *Añade una lineaPedido al carrito
	 */
	@Test
	public void test_Add() {
		
		Producto p = productoServicio.findById((long) 70);
		
		LineaPedido lp = new LineaPedido(p, 1);
		carritoServicio.addProducto(p);

		
		assertEquals(true, carritoServicio.getProductosCarrito().contains(lp));
	}
}