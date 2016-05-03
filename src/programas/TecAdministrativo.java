package programas;

import java.time.LocalDate;

public class TecAdministrativo extends Funcionario {

	public TecAdministrativo(String nome, String cargo, LocalDate dataNascimento) {
		super(nome, cargo, dataNascimento);
		this.setId("3");
	}

}
