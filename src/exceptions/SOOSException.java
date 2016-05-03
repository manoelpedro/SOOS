package exceptions;

public class SOOSException extends Exception {
	
	public SOOSException(){
		super("Erro na execucao do sistema SOOS");
	}
	
	public SOOSException(String msg){
		super(msg);
	}
	
}
