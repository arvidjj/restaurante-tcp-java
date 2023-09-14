package service.pedido;

import beans.cheff.Cheff;
import beans.pedido.Pedido;
import service.base.IBaseService;

public interface IPedidoService extends IBaseService<Pedido> { 
	public Pedido findByNum(int name);
}
