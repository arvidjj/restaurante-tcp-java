package exceptions;

import java.net.SocketException;

public class SocketUNIException extends SocketException {
	private static final long serialVersionUID = 1L;
	
	public SocketUNIException(String message) {
		super(message);
	}
}
