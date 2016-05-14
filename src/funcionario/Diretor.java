package funcionario;

import java.time.LocalDate;


public class Diretor extends Funcionario {

	public Diretor(String nome, String cargo, LocalDate dataNascimento) {
		super(nome, cargo, dataNascimento);
		this.setId("1");
	}

}