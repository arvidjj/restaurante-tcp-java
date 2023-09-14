package service.pedidoConjunto;

import java.util.Map;

import beans.pedidoConjunto.PedidoConjunto;
import service.base.BaseService;

	public class PedidoConjuntoiServiceImpl extends BaseService<PedidoConjunto> implements IPedidoConjuntoService {
		@Override
		public PedidoConjunto findByNum(int name) {
			final Map<Integer, PedidoConjunto> map = super.getAll();
			PedidoConjunto pedido = null;
			for (PedidoConjunto c : map.values()) {
				if (name == c.getNumMesa()) {
					pedido = c;
					break;
				}
			}
			return pedido;
		}
	}
