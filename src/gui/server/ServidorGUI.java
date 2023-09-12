package gui.server;

import javax.swing.*;

import beans.cheff.Cheff;
import server.thread.Hilo;
import service.cheff.CheffiServiceImpl;
import utils.Configurator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServidorGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea logTextArea;
    private JButton asignarPedidoButton;
    Configurator configuracion = new Configurator("src/config.properties");
    private JComboBox<String> boxCheffs;
    
    private Map<Integer, Cheff> chefs;
    
    private int contadorHilo;
    CheffiServiceImpl cheffService;
    
    public ServidorGUI() {
        super("Servidor TCP");

        logTextArea = new JTextArea(20, 40);
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        asignarPedidoButton = new JButton("Asignar Pedido");
        
        asignarPedidoButton.addActionListener(new ActionListener() {
            @Override
           public void actionPerformed(ActionEvent e) {
                try {
					asignarPedido(null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });


        chefs = new HashMap<>();
        
        cheffService = new CheffiServiceImpl();
        Cheff chef1 = new Cheff("Cheff ramon");
        Cheff chef2 = new Cheff("Cheff felipe");
        chefs.put(1, chef1); 
        chefs.put(2, chef2);
        cheffService.save(chef1);
        cheffService.save(chef2);
        
        boxCheffs = new JComboBox<>();
        String[] chefNames = cheffService.getAll().values()
             .stream()
             .map(chef -> chef.getNombre())
             .toArray(String[]::new);
     	boxCheffs.setModel(new DefaultComboBoxModel<>(chefNames));
     
        boxCheffs.setSelectedIndex(0); 
        
        JPanel panel = new JPanel();
        panel.add(asignarPedidoButton);
        panel.add(new JLabel("Seleccionar Chef: "));
        panel.add(boxCheffs);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(logScrollPane, BorderLayout.CENTER);
        container.add(panel, BorderLayout.SOUTH);

        
        contadorHilo = 1;

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        iniciarServidor();
    }

    private void iniciarServidor() {
        try {
            ServerSocket serverSocket = new ServerSocket(configuracion.getIntProperty("puerto"));

            while (true) {
                log("Servidor TCP esperando conexiones en el puerto " + serverSocket.getLocalPort());
                Socket socketCliente = serverSocket.accept();
                log("Pedido recibido");

                asignarPedido(socketCliente);
            }
        } catch (IOException e) {
            log("Error en el servidor: " + e.getMessage());
        }
    }

    private void asignarPedido(Socket socketcliente) throws IOException {
        // Get the selected chef's name from the JComboBox
        String selectedChefName = (String) boxCheffs.getSelectedItem();
        Cheff chefAsignado = cheffService.findByName(selectedChefName);

        if (chefAsignado != null) {
            // Create a new thread (Hilo) with the socket client, thread number, and the assigned chef
            new Hilo(socketcliente, contadorHilo, chefAsignado); // Replace null with your Socket instance
            log("Pedido asignado al Chef " + chefAsignado.getNombre());
            contadorHilo++;
        } else {
            log("No hay chefs disponibles en este momento.");
        }
    }

    private Cheff obtenerChefDisponible() {
        for (Cheff chef : chefs.values()) {
            if (chef.isDisponible()) {
                chef.setDisponible(false); // Marcar al chef como no disponible
                return chef;
            }
        }
        return null; // No hay chefs disponibles
    }

    
    private void log(String message) {
        logTextArea.append(message + "\n");
    }

    public static void main(String[] args) {

            new ServidorGUI();
    }
}

