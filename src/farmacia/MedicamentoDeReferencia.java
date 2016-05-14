package farmacia;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;

public class MedicamentoDeReferencia extends Medicamento{

	public MedicamentoDeReferencia(String nome, String tipo, double preco, int quantidade, LinkedHashSet <Categorias> categorias) {
		super(nome, tipo, preco, quantidade, categorias);
	}

	@Override
	public String toString() {
		String categorias = "";
		for (Categorias ctg : getCategorias()) {
			categorias = categorias + ctg.name().toLowerCase() + ",";
		}
		categorias = categorias.substring(0, categorias.length()-1);
		
		DecimalFormat df = new DecimalFormat("#.00");
		String precoFormatado = df.format(getPreco());
		
		return "Medicamento de Referencia: "+ getNome() +" - Preco: R$ "+precoFormatado+" - Disponivel: "
				+ getQuantidade()+" - Categorias: "+categorias;
	}
	
	
	
}
