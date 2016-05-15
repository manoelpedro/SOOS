package paciente;

import java.time.DateTimeException;
import java.time.LocalDate;


public class FactoryPaciente {
	
	public Paciente criaPaciente(String nome, String dataNascimento, double peso, String sexo, String genero, String tipoSanguineo) throws Exception{
		
		ValidaCriacaoDePaciente(nome, peso, tipoSanguineo);
		LocalDate data = converteData(dataNascimento);
		
		Paciente paciente = new Paciente(nome, data, peso, sexo, genero, tipoSanguineo);
		
		return paciente;
	}
	
	/**
	 * Valida os parametros recebidos para criacao de um novo paciente.
	 * 
	 * @param nome
	 * @param peso
	 * @param tipoSanguineo
	 * @throws Exception
	 */
	private void ValidaCriacaoDePaciente(String nome, double peso, String tipoSanguineo) throws Exception{
		if(nome == null || nome.trim().equals("")){
			throw new Exception("Nao foi possivel cadastrar o paciente. Nome do paciente nao pode ser vazio.");
		}
		if(peso < 0){
			throw new Exception("Nao foi possivel cadastrar o paciente. Peso do paciente nao pode ser negativo.");
		}
		if(tipoSanguineo == null || tipoSanguineo.trim().equals("") || !(tipoSanguineo.matches("(?i)[abo]{1,2}+[+|-]"))){
			throw new Exception("Nao foi possivel cadastrar o paciente. Tipo sanguineo invalido.");
		}
	}
	
	/**
	 * Converte a data de nascimento recebida em string para LocalDate.
	 * 
	 * @param dataNascimento
	 * 			Data de nascimento do paciente (dd/MM/yyyy).
	 * @return
	 * 			Retorna a data de nascimento convertida para LocalDate no formato (yyyy-MM-dd)
	 */
	private LocalDate converteData(String dataNascimento){
		try{
			String[] array = dataNascimento.split("/");
			LocalDate data = LocalDate.of(Integer.parseInt(array[2]), Integer.parseInt(array[1]),Integer.parseInt(array[0]));
			return data;
			
		}catch(DateTimeException e){
			throw new DateTimeException("Nao foi possivel cadastrar o paciente. Data invalida.");
		}
	}
}
