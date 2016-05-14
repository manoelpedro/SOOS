package exceptions;

public class MedicamentoException extends SOOSException{

	public MedicamentoException(){
		super("Medicamento invalido");
	}
	
	public MedicamentoException(String msg){
		super(msg);
	}
	
}
