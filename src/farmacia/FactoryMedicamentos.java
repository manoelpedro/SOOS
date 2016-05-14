package farmacia;

import java.util.LinkedHashSet;

public class FactoryMedicamentos {

	public Medicamento criaMedicamentos(String nome, String tipo, double preco, int quantidade, String categorias){
		
		String[] recebidas = categorias.split(",");
		Categorias[] noEnum =  Categorias.values();
		
		LinkedHashSet <Categorias> categoriasDoMedicamento = new LinkedHashSet <>(); //LinkedHashSet ordena na ordem que s�o inseridos
		
		for (Categorias c : noEnum) {
			for (String categoria : recebidas) {
				if(categoria.trim().equalsIgnoreCase(c.name())){
					categoriasDoMedicamento.add(c);
				}
			}
		}
		
		if(tipo.trim().equalsIgnoreCase("generico")){
			Medicamento novoMedicamento = new MedicamentoGenerico(nome, "Generico", preco, quantidade, categoriasDoMedicamento);
			return novoMedicamento;
		}
		else if (tipo.trim().equalsIgnoreCase("referencia")){
			Medicamento novoMedicamento = new MedicamentoDeReferencia(nome, "Referencia", preco, quantidade, categoriasDoMedicamento);
			return novoMedicamento;
		}
		return null;
	}
	
	//usar strategy
}
