package exceptions;

public class FuncionarioException extends SOOSException {
	
	public FuncionarioException(){
		super("Funcionario invalido");
	}
	
	public FuncionarioException(String msg){
		super(msg);
	}
	
}
