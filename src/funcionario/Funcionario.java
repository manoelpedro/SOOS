package funcionario;

import hospital.Pessoa;

import java.time.LocalDate;

public class Funcionario extends Pessoa{
	
	private String cargo;
	private String matricula;
	private String senha;
	private String id;
	
	public Funcionario(String nome, String cargo, LocalDate dataNascimento) {
		super(nome, dataNascimento);
		
		this.cargo = cargo;
		this.matricula = "";
		this.senha = "";
		this.id = "";
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
