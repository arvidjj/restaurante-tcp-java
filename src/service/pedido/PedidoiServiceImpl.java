package service.pedido;

import java.util.Map;

import beans.cheff.Cheff;
import beans.pedido.Pedido;
import service.base.BaseService;

public class PedidoiServiceImpl extends BaseService<Pedido> implements IPedidoService {
	@Override
	public Pedido findByNum(int name) {
		final Map<Integer, Pedido> map = super.getAll();
		Pedido pedido = null;
		for (Pedido c : map.values()) {
			if (name == c.getNumPedido()) {
				pedido = c;
				break;
			}
		}
		return pedido;
	}
}
