package gui.cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import beans.pedido.Pedido;
import beans.pedidoMenu.PedidoMenu;
import controller.MenuC;
import utils.Configurator;

public class ClienteGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> entradaComboBox;
    private JComboBox<String> platoFondoComboBox;
    private JComboBox<String> bebidasComboBox;
    private JComboBox<String> postreComboBox;
    private JComboBox<Integer> mesaComboBox;
    private JComboBox<Integer> sillaComboBox;
    private JButton enviarPedidoButton;
    private Socket clienteSocket;
    private ObjectOutputStream dataOutputStream;
    private ObjectInputStream dataInputStream;
    private Configurator configuracion = new Configurator("src/config.properties");
    
    private MenuC menuRecibido;
    
    public ClienteGUI() throws ClassNotFoundException {
        // Configurar la interfaz gráfica
        setTitle("Realizar un pedido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(7, 2));
        
        JLabel entradaLabel = new JLabel("Entrada:");
        entradaComboBox = new JComboBox<>();
        JLabel platoFondoLabel = new JLabel("Plato Fondo:");
        platoFondoComboBox = new JComboBox<>();
        JLabel bebidasLabel = new JLabel("Bebidas:");
        bebidasComboBox = new JComboBox<>();
        JLabel postreLabel = new JLabel("Postre:");
        postreComboBox = new JComboBox<>();

        // Componentes para seleccionar la mesa y la silla
        JLabel mesaLabel = new JLabel("Mesa:");
        mesaComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        JLabel sillaLabel = new JLabel("Silla:");
        sillaComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        // Botón para enviar el pedido
        enviarPedidoButton = new JButton("Enviar Pedido");

        // Agregar componentes a la ventana
        add(entradaLabel);
        add(entradaComboBox);
        add(platoFondoLabel);
        add(platoFondoComboBox);
        add(bebidasLabel);
        add(bebidasComboBox);
        add(postreLabel);
        add(postreComboBox);
        add(mesaLabel);
        add(mesaComboBox);
        add(sillaLabel);
        add(sillaComboBox);
        add(new JLabel()); // Espacio en blanco
        add(enviarPedidoButton);

        // Manejador de eventos para el botón "Enviar Pedido"
        enviarPedidoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					enviarPedido();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        // Inicializar la conexión con el servidor
        try {
            clienteSocket = new Socket(configuracion.getProperty("direccion"), configuracion.getIntProperty("puerto"));
            dataOutputStream = new ObjectOutputStream(clienteSocket.getOutputStream());
            dataInputStream = new ObjectInputStream(clienteSocket.getInputStream());
            
            //OBTENER MENU EL STREAM
            menuRecibido = new MenuC();
            System.out.println("Obteniendo menu");
            menuRecibido = (MenuC) dataInputStream.readObject();
            System.out.println(menuRecibido);
            
            popularComboBoxes(menuRecibido);
            
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar al servidor.");
        }

        // Mostrar la ventana
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void enviarPedido() throws ClassNotFoundException {
        try {
            // Crear un objeto Pedido con la información seleccionada por el cliente
            PedidoMenu pedidoMenu = new PedidoMenu();
            pedidoMenu.setEntrada(entradaComboBox.getSelectedItem().toString());
            pedidoMenu.setPlatoFondo(platoFondoComboBox.getSelectedItem().toString());
            pedidoMenu.setBebidas(bebidasComboBox.getSelectedItem().toString());
            pedidoMenu.setPostre(postreComboBox.getSelectedItem().toString());

            int mesa = (int) mesaComboBox.getSelectedItem();
            int silla = (int) sillaComboBox.getSelectedItem();

            Pedido pedido = new Pedido(mesa, silla);
            pedido.setPedMenu(pedidoMenu);

            // Enviar el pedido al servidor
            dataOutputStream.writeObject(pedido);

            // Mostrar un mensaje de confirmación
            JOptionPane.showMessageDialog(this, "Pedido enviado con éxito.");

            // Limpiar las selecciones después de enviar el pedido
            entradaComboBox.setSelectedIndex(0);
            platoFondoComboBox.setSelectedIndex(0);
            bebidasComboBox.setSelectedIndex(0);
            postreComboBox.setSelectedIndex(0);
            
            final Pedido pedidoRecibido = (Pedido) dataInputStream.readObject();
            // Leer la respuesta del servidor
            System.out.println("Respuesta del servidor: " + pedidoRecibido.isServido());

            dataInputStream.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al enviar el pedido.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
				new ClienteGUI();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
}
