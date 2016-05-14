package paciente;

import java.util.ArrayList;


public class Prontuario implements Comparable<Prontuario>{
	
	private Paciente paciente;
	private ArrayList<String> procedimentos;
	
	public Prontuario(){
		procedimentos = new ArrayList<String>();
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public ArrayList<String> getProcedimentos() {
		return procedimentos;
	}

	public void setProcedimentos(ArrayList<String> procedimentos) {
		this.procedimentos = procedimentos;
	}

	public int compareTo(Prontuario p) {
		return paciente.getNome().compareTo(p.getPaciente().getNome());
	}
	
	
	
}
