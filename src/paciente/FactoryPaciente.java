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

	private LocalDate converteData(String dataNascimento){
		try{
			String[] array = dataNascimento.split("/");
			LocalDate data = LocalDate.of(Integer.parseInt(array[2]), Integer.parseInt(array[1]),Integer.parseInt(array[0]));
			return data;
			
		}catch(DateTimeException e){
			throw new DateTimeException("Nao foi possivel cadastrar o paciente. Data invalida.");
		}
	}
	
	
	private void ValidaCriacaoDePaciente(String nome, double peso, String tipoSanguineo) throws Exception{
		
		if(nome == null || nome.trim().equals("")){
			throw new Exception("Nao foi possivel cadastrar o paciente. Nome do paciente nao pode ser vazio.");
		}
		
		if(peso < 0){
			throw new Exception("Nao foi possivel cadastrar o paciente. Peso do paciente nao pode ser negativo.");
		}
		
		if(tipoSanguineo == null || tipoSanguineo.equals("") || (!(tipoSanguineo.equals("A+"))
				&& !(tipoSanguineo.equals("A-"))
				&& !(tipoSanguineo.equals("B+"))
				&& !(tipoSanguineo.equals("B-"))
				&& !(tipoSanguineo.equals("AB+"))
				&& !(tipoSanguineo.equals("AB-"))
				&& !(tipoSanguineo.equals("O+"))
				&& !(tipoSanguineo.equals("O-")))){
			
			throw new Exception("Nao foi possivel cadastrar o paciente. Tipo sanguineo invalido.");
		}
		
	}
	
}
