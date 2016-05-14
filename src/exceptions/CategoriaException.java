package exceptions;

public class CategoriaException extends SOOSException{

		
	public CategoriaException(){
			super("Remedio invalido");
		}
		
	public CategoriaException(String msg){
			super(msg);
	}
		
}
