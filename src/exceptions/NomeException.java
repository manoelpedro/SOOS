package exceptions;

public class NomeException extends SOOSException {

	public NomeException() {
		
		super("Nome invalido");
	}
	
	public NomeException(String msg){
		super(msg);
	}
	
}
