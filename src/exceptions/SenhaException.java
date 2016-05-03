package exceptions;

public class SenhaException extends SOOSException {
	
	public SenhaException(){
		super("Senha invalida");
	}
	
	public SenhaException(String msg){
		super(msg);
	}
	
}
