package server;
import java.io.*;
import java.net.*;

import server.thread.Hilo;
import utils.Configurator;

public class Servidor {

    public Servidor() throws IOException {
        ServerSocket serverSocket = null;
        Configurator configuracion = new Configurator("src/config.properties");
        try {
            // Crear un socket de servidor que escuche en el puerto especificado
        	serverSocket = new ServerSocket(configuracion.getIntProperty("puerto"));
            
            int contadorHilo = 10;
            
            //HACER UN FOR DE MAXIMOS CLIENTES QUE SE PUEDEN CONECTAR
            while (true) {
            	System.out.println("Servidor TCP esperando conexiones en el puerto " + serverSocket.getLocalPort());
            	Socket socketCliente = serverSocket.accept();
            	System.out.println("Pedido recibido");
            	
            	new Hilo(socketCliente, contadorHilo);
            	contadorHilo++;
            	
            }
        } catch (IOException e) {
            System.out.println("IOException en servidor!");
            if(null!= serverSocket) {
            	serverSocket.close();
            }
            System.out.println("Problemas con el socket TCP servidor");
        }
    }
    
    public static void main(String[] args) {
    	try {
			new Servidor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error en SERVIDOR MAIN");
		}
    		
    	}
}
