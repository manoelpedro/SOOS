package exceptions;

public class MatriculaException extends SOOSException{
	
	public MatriculaException(){
		super("Matricula invalida");
	}
	
	public MatriculaException(String msg){
		super(msg);
	}
	
}
