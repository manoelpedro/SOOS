package orgao;

public class Orgao {

	private String nome;
	private String tipoSanguineo;
	
	public Orgao(String nome, String tipoSanguineo){
		
		this.nome = nome;
		this.tipoSanguineo = tipoSanguineo;
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoSanguineo() {
		return tipoSanguineo;
	}

	public void setTipo_sanguineo(String tipoSanguineo) {
		this.tipoSanguineo = tipoSanguineo;
	}
	
}
