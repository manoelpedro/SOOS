package funcionario;

import java.time.DateTimeException;
import java.time.LocalDate;

public class FactoryFuncionario {
	
	private boolean temDiretor;
	
	public FactoryFuncionario() {
		temDiretor = false;
	}
	
	public Funcionario criaFuncionario(String nome, String cargo, String dataNascimento) throws Exception {
		
		LocalDate data = converteData(dataNascimento);
		
		if(cargo.equalsIgnoreCase("Diretor Geral")){
			if(temDiretor == false){
				Funcionario novoFuncionario = new Diretor(nome, "Diretor Geral", data);
				temDiretor = true;
				return novoFuncionario;
			}else{
				throw new Exception("Nao pode existir mais de um Diretor Geral.");
			}
		}	
		else if(cargo.equalsIgnoreCase("Medico")){
			Funcionario novoFuncionario = new Medico(nome, "Medico", data);
			return novoFuncionario;
		}	
		else if(cargo.equalsIgnoreCase("Tecnico Administrativo")){
			Funcionario novoFuncionario = new TecAdministrativo(nome, "Tecnico Administrativo", data);
			return novoFuncionario;
		}	
		return null;
	}
	
	/**
	 * Converte a data de nascimento de String para LocalDate.
	 * 
	 * @param dataNascimento
	 * 			Data de nascimento(dd/MM/yyyy) que sera convertida.
	 * @return
	 *			Retorna a data de nascimento convertida para LocalDate no formato (yyyy-MM-dd)
	 */
	private LocalDate converteData(String dataNascimento){
		try{
			String[] array = dataNascimento.split("/");
			LocalDate data = LocalDate.of(Integer.parseInt(array[2]), Integer.parseInt(array[1]),Integer.parseInt(array[0]));
			return data;
			
		}catch(DateTimeException e){
			throw new DateTimeException("Erro ao liberar o sistema. Insira uma data valida.");
		}
	}
}
