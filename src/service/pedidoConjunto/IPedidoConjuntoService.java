package service.pedidoConjunto;

import beans.pedidoConjunto.PedidoConjunto;
import service.base.IBaseService;

	public interface IPedidoConjuntoService extends IBaseService<PedidoConjunto> { 
		public PedidoConjunto findByNum(int name);
	}
