package client;

import java.io.*;
import java.net.*;

import beans.pedido.Pedido;
import beans.pedidoMenu.PedidoMenu;
import beans.usuario.Usuario;
import utils.Configurator;

public class Cliente {
	
	   public Cliente() throws IOException {
	        Socket clienteSocket = null;
	        ObjectInputStream dataInputStream = null;
	        ObjectOutputStream dataOutputStream = null;
	        Configurator configuracion = new Configurator("src/config.properties");
	        try {
	            // Crear un socket para conectarse al servidor
	            clienteSocket = new Socket(configuracion.getProperty("direccion"), configuracion.getIntProperty("puerto"));

	            // Crear flujos de entrada y salida para comunicarse con el servidor
	            
	            dataOutputStream = new ObjectOutputStream(clienteSocket.getOutputStream());
	            dataOutputStream.writeObject(getPedido());
	            
	            dataInputStream = new ObjectInputStream(clienteSocket.getInputStream());
	            
	            //final Usuario cliente = (Usuario) dataInputStream.readObject();
	            
	            final Pedido pedido = (Pedido) dataInputStream.readObject();
	            // Leer la respuesta del servidor
	            System.out.println("Respuesta del servidor: " + pedido.isServido());

	            dataInputStream.close();
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace(); //ESTO NO ES BUENO HACER! SE EXPONE TODO EL STACK TRACE DEL NEGOCIO
	            
	            System.out.println("IOException en el cliente TCP");
	        } finally {
	        	try {
					clienteSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	   
	   private Pedido getPedido() {
		   System.out.println("Realiza tu pedido: ");
		   PedidoMenu pedMenu = new PedidoMenu();
		   
		   pedMenu.setEntrada("Rollos");
		   pedMenu.setPlatoFondo("Fideo");
		   pedMenu.setBebidas("Jugo");
		   pedMenu.setPostre("Helado");
		   
		   Pedido ped = new Pedido(1, 3, 2);
		   ped.setPedMenu(pedMenu);
		   return ped;
	}

	public static void main(String[] args) {
	    	try {
				new Cliente();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error en SERVIDOR MAIN");
			}
	    		
	    	}
	}

