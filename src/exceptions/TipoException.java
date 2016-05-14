package exceptions;

public class TipoException extends EntradaException{

	public TipoException() {
		
		super("Tipo invalido");
	}
	
	public TipoException(String msg){
		super(msg);
	}
	
}
