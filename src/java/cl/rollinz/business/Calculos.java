/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rollinz.business;

import cl.rollinz.entities.Producto;
import cl.rollinz.entities.Pedido;
import cl.rollinz.entities.Mesa;
import java.util.ArrayList;
/**
 *
 * @author rolan
 */
public class Calculos {
    public int calcularTotal(ArrayList<Producto> productos, int cantidad){
        int calculo = 0;
        int total = 0;
        ArrayList<Pedido> listado = new ArrayList<>();
        Pedido pedido = new Pedido();
        
        for (Producto producto : productos) {
            calculo = producto.getPrecio() * cantidad;
            pedido.setTotalpedido(total);
            listado.add(pedido);
        }
        
        //Transformar en 2 metodos
        for (Pedido pedido1 : listado) {
            total = pedido1.getTotalpedido() + total;
        }
        total = calculo * productos.size();
        return total;
    }
    
//    public Producto cargarDatosDelProducto(String nombreProducto, int precio) {
//        Producto producto = new Producto(nombreProducto, precio);
//        if (producto) {
//            
//        }
//        return producto;    
//    }
    
//    public Pedido cargarDatosDelPedido(Producto producto, Mesa mesa, int cantidad) {
//        Pedido pedido = new Pedido();
//        int total = calcularTotal(producto, cantidad);
//        if (total>0) {
//            pedido.setIdprodcutoFk(producto);
//            pedido.setIdmesaFk(mesa);
//            pedido.setTotalpedido(total);
//        }
//        return pedido;
//    }
}
