package programas;

import java.time.LocalDate;

public class Medico extends Funcionario{

	public Medico(String nome, String cargo, LocalDate dataNascimento) {
		super(nome, cargo, dataNascimento);
		this.setId("2");
	}

}