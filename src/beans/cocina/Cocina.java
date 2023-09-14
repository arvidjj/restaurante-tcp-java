package beans.cocina;

import beans.base.AbstractBean;
import beans.cheff.Cheff;
import server.thread.Hilo;
import service.cheff.CheffiServiceImpl;
import service.pedido.PedidoiServiceImpl;
import service.pedidoConjunto.PedidoConjuntoiServiceImpl;
import utils.Loggeador;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;


public class Cocina extends AbstractBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CheffiServiceImpl cheffService;
    private PedidoiServiceImpl pedidoService;
    private final PedidoConjuntoiServiceImpl pedidoConjuntoService;
    private int contadorHilo;
    private final Loggeador log; //loggeador
    
    public Cocina(CheffiServiceImpl cheffService, PedidoConjuntoiServiceImpl pedidoConjServ , PedidoiServiceImpl pedidoService) {
        this.cheffService = cheffService;
        this.pedidoService = pedidoService;
        this.pedidoConjuntoService = pedidoConjServ;
        this.contadorHilo = 1;
        log = new Loggeador();
    }

    public void asignarPedido(Socket socketCliente, String selectedChefName) throws IOException {
        Cheff chefAsignado = cheffService.findByName(selectedChefName);

        if (chefAsignado != null) {
            new Hilo(socketCliente, contadorHilo, chefAsignado, cheffService, pedidoConjuntoService, pedidoService);
            log.loggear("Pedido asignado al Chef " + chefAsignado.getNombre());
            contadorHilo++;
        } else {
        	log.loggear("Ese chef no está disponible o ya tiene asignado un pedido.");
        }
    }
}
