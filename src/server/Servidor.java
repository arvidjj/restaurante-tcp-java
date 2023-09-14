package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.cheff.Cheff;
import server.thread.Hilo;
import service.cheff.CheffiServiceImpl;
import utils.Configurator;

public class Servidor {
    private Map<Integer, Cheff> chefs; // Almacena los Chefs disponibles

    public Servidor() throws IOException {
        ServerSocket serverSocket = null;
        Configurator configuracion = new Configurator("src/config.properties");
        
        chefs = new HashMap<>();
        CheffiServiceImpl cheffService = new CheffiServiceImpl();
        Cheff chef1 = new Cheff("Cheff ramon");
        Cheff chef2 = new Cheff("Cheff felipe");
        chefs.put(1, chef1); 
        chefs.put(2, chef2);
        
        try {
            // Crear un socket de servidor que escuche en el puerto especificado
            serverSocket = new ServerSocket(configuracion.getIntProperty("puerto"));

            int contadorHilo = 10;

            // HACER UN FOR DE MÁXIMOS CLIENTES QUE SE PUEDEN CONECTAR
            while (true) {
                System.out.println("Servidor TCP esperando conexiones en el puerto " + serverSocket.getLocalPort());
                Socket socketCliente = serverSocket.accept();
                System.out.println("Pedido recibido");
                
                Cheff chefDisponible = chefs.get(1);
                if (chefDisponible != null) {
                    // Pasa el pedido y el chef al hilo que atiende al cliente
                    //new Hilo(socketCliente, contadorHilo, chefDisponible);
                    contadorHilo++;
                } else {
                    System.out.println("Ese cheff no esta disponible");
                }

            }
        } catch (IOException e) {
            System.out.println("IOException en servidor!");
            if (null != serverSocket) {
                serverSocket.close();
            }
            System.out.println("Problemas con el socket TCP servidor");
        }
    }

    public static void main(String[] args) {
        try {
            new Servidor();
        } catch (IOException e) {
            System.out.println("Error en SERVIDOR MAIN");
        }
    }
}
