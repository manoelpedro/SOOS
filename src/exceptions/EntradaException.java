package exceptions;

/**
 * Exception lancada em caso de entradas de valores invalidos.
 * 
 * @author manoelp
 *
 */
public class EntradaException extends SOOSException {

	public EntradaException() {
		super("Entrada invalida");
	}
	
	public EntradaException(String msg){
		super(msg);
	}
	
}
