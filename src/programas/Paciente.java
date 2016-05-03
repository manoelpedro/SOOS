package programas;

import java.time.LocalDate;

public class Paciente extends Pessoa{
	
	private double peso;
	private String tipoSanguineo;
	private String sexoBiologico;
	private String genero;
	private String id;
	
	public Paciente(String nome, LocalDate dataNascimento, double peso, String tipoSanguineo, String sexo, String genero) {
		super(nome, dataNascimento);
	
		this.peso = peso;
		this.tipoSanguineo = tipoSanguineo;
		this.sexoBiologico = sexo;
		this.genero = genero;
		this.id = "";
		
	}

}
