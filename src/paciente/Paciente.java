package paciente;

import hospital.Pessoa;

import java.time.LocalDate;

import cartao.CartaoFidelidade;

public class Paciente extends Pessoa{
	
	private double peso;
	private String tipoSanguineo;
	private String sexoBiologico;
	private String genero;
	private int idade;
	private int id;
	private double totalGasto;
	private CartaoFidelidade cartaoFidelidade;
	
	public Paciente(String nome, LocalDate dataNascimento, double peso, String sexo, String genero, String tipoSanguineo) {
		super(nome, dataNascimento);
	
		this.peso = peso;
		this.tipoSanguineo = tipoSanguineo;
		this.sexoBiologico = sexo;
		this.genero = genero;
		this.cartaoFidelidade = new CartaoFidelidade();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Paciente){
			Paciente Outropaciente = (Paciente) obj;
			if(this.getNome().equals(Outropaciente.getNome())){
				return true;
			}
		}			
		return false;
	}
	

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getTipoSanguineo() {
		return tipoSanguineo;
	}

	public String getSexoBiologico() {
		return sexoBiologico;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public CartaoFidelidade getCartaoFidelidade() {
		return cartaoFidelidade;
	}

	public double getTotalGasto() {
		return totalGasto;
	}

	public void setTotalGasto(double totalGasto) {
		this.totalGasto = totalGasto;
	}
	
	
	
}
