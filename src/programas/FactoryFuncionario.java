package programas;

import java.time.DateTimeException;
import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;

public class FactoryFuncionario {
	
	public Funcionario criaFuncionario(String nome, String cargo, String dataNascimento) {
		
		LocalDate data = converteData(dataNascimento);
		
		if(cargo.equals("Diretor Geral")){
			Funcionario novoFuncionario = new Diretor(nome, cargo, data);
			return novoFuncionario;
		}	
		else if(cargo.equals("Medico")){
			Funcionario novoFuncionario = new Medico(nome, cargo, data);
			return novoFuncionario;
		}	
		else if(cargo.equals("Tecnico Administrativo")){
			Funcionario novoFuncionario = new TecAdministrativo(nome, cargo, data);
			return novoFuncionario;
		}	
		return null;
	}
	
	/**
	 * Converte a data de nascimento de String para LocalDate
	 * 
	 * @param dataNascimento
	 * @return
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
