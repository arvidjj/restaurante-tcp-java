package beans.base;

import java.io.Serializable;

public abstract class AbstractBean implements Serializable {
	
	//CADA BEAN UN SERVICIO
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer _id;
	
	public Integer getId() {
		return _id;
	}
	
	public void setId(Integer id) {
		_id = id;
	}
}
