package farmacia;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;

public class MedicamentoGenerico extends Medicamento{

	public MedicamentoGenerico(String nome, String tipo, double preco, int quantidade, LinkedHashSet <Categorias> categorias) {
		super(nome, tipo, preco, quantidade, categorias);
	}
	
	@Override
	public String toString() {
		String categorias = "";
		for (Categorias ctg : getCategorias()) { //precorre a lista com as categorias do medicamento
			categorias = categorias + ctg.name().toLowerCase() + ",";
		}
		categorias = categorias.substring(0, categorias.length()-1); //string com as categorias
		
		DecimalFormat df = new DecimalFormat("#.00"); //formato de impressao do preco do medicamento 
		String precoFormatado = df.format(getPreco());
		
		return "Medicamento Generico: "+ getNome() +" - Preco: R$ "+precoFormatado+" - Disponivel: "
				+ getQuantidade()+" - Categorias: "+categorias;
	}
	
}
