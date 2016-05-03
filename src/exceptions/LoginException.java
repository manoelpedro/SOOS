package exceptions;

public class LoginException extends SOOSException {
	
	public LoginException(){
		super("Erro no login");
	}
	
	public LoginException(String msg){
		super(msg);
	}

}
