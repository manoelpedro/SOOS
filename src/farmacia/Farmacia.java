package farmacia;

import hospital.Controller;
import funcionario.TecAdministrativo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import com.sun.xml.internal.ws.util.StringUtils;

import exceptions.CategoriaException;
import exceptions.EntradaException;
import exceptions.MedicamentoException;
import exceptions.NomeException;
import exceptions.SOOSException;

public class Farmacia {
	
	private ArrayList<Medicamento> medicamentos; //lembrar
	private FactoryMedicamentos factoryMedicamento;
	//private Controller controle;
	
	public Farmacia(){
		
		medicamentos = new ArrayList<Medicamento>();
		factoryMedicamento = new FactoryMedicamentos();
		
		
	}
	//FALTA RESOLVER SE O FUNCIONARIO LOGADO EH TECNICO ADM
	/**
	 * Cadastra um medicamento na farmacia.
	 * So realiza a operacao se o funcionario for um tecnico administrativo.
	 * 
	 * @param nome
	 *			Nome do medicamento.
	 * @param tipo
	 * 			Tipo do medicamento: De referencia ou generico.
	 * @param preco
	 * 			Preco do medicamento.
	 * @param quantidade
	 * 			Quantidade do medicamento.
	 * @param categorias
	 * 			Categoria que o medicamento se enquadra. Pode ser mais de uma.
	 * @return
	 * 			Retorna o nome do medicamento.
	 * @throws Exception
	 */
	public String cadastraMedicamento(String nome, String tipo, double preco, int quantidade, String categorias) throws Exception {
		
		
		verificaSeNometemErro(nome, "Erro no cadastro de medicamento. ");
		verificaSePrecotemErro(preco, "Erro no cadastro de medicamento. ");
		verificaSeQuantidadetemErro(quantidade, "Erro no cadastro de medicamento. ");
		
		Medicamento Novomedicamento = factoryMedicamento.criaMedicamentos(nome, tipo, preco, quantidade, categorias);
		
		//se for generico altera o preco com desconto
		if(Novomedicamento instanceof MedicamentoGenerico){
			Novomedicamento.setPreco(calculaPrecoGenerico(Novomedicamento.getPreco()));
		}
		medicamentos.add(Novomedicamento);
		return nome;
	}
	
	/**
	 * Metodo para obter informacoes especificas de um medicamento.
	 * Retorna o valor de um determinado atributo.
	 * 
	 * @param atributo
	 *			Atributo que se deseja retornar o valor.
	 * @param nome
	 * 			Nome do medicamento que se deseja retorna um atributo
	 * @return
	 * 			Retorna o valor do atributo especificado.
	 * @throws SOOSException
	 */
	public String getInfoMedicamento(String atributo, String nome) throws SOOSException{
		
		Medicamento medicamento = pesquisaMedicamento(nome);
			
		if (medicamento == null){
			throw new MedicamentoException("Erro na consulta de medicamento. Medicamento nao cadastrado.");
		}	
		
		switch (atributo.trim().toLowerCase()) {
		case "nome":
			return medicamento.getNome();		
		case "tipo":
			if(medicamento.getTipo().equalsIgnoreCase("referencia")){
				return "de Referencia";
			}
			return medicamento.getTipo();
		case "categorias":
			String catg = "";
			for (Categorias c : medicamento.getCategorias()){ //percorre lista de categorias do medicamento
				catg = catg + c.name().toLowerCase() + ",";
			}	
			catg = catg.substring(0, catg.length()-1); //catg = posicao 0 da string ate posicao final - 1
			return catg;
		case "preco":
			return String.valueOf(medicamento.getPreco());
		case "quantidade":
			return String.valueOf(medicamento.getQuantidade());
		default:
			throw new EntradaException("Este atributo nao existe");
		}	
	}
	
	/**
	 * Atualiza informacoes de um medicamento.
	 * 
	 * @param nome
	 * 			Nome do medicamento que tera um atributo atualizado.
	 * @param atributo
	 * 			Atributo que sera atualizado/modificado.
	 * @param novoValor
	 * 			Novo valor que sera atribuido ao atributo informado.
	 * @throws SOOSException
	 */
	public String atualizaMedicamento(String nome, String atributo, String novoValor) throws SOOSException{
		
		if(atributo.equalsIgnoreCase("nome"))
			throw new EntradaException("Erro ao atualizar medicamento. Nome do medicamento nao pode ser alterado.");	
		else if (atributo.equalsIgnoreCase("tipo"))
			throw new EntradaException("Erro ao atualizar medicamento. Tipo do medicamento nao pode ser alterado.");
		
		Medicamento medicamento = pesquisaMedicamento(nome);
		if(medicamento == null)
			throw new MedicamentoException("Erro ao atualizar medicamento. Medicamento nao cadastrado.");
		
		switch (atributo.toLowerCase()){
		case "quantidade":
			medicamento.setQuantidade(Integer.parseInt(novoValor));
			break;
		case "preco":
			if(medicamento instanceof MedicamentoGenerico){ //se for generico o preco tem desconto.
				medicamento.setPreco(calculaPrecoGenerico(Double.parseDouble(novoValor)));
				break;
			}
			medicamento.setPreco(Double.parseDouble(novoValor));//se nao for o preco sera extamente o novoValor informado
			break;
		default:
			throw new EntradaException("Erro ao atualizar medicamento. Atributo invalido.");
		}
		return null;
	}
	
	/**
	 * Retorna nomes dos medicamentos buscando-os atraves de sua categoria.
	 * 
	 * @param categoria
	 * 			Categoria que se deseja pesquisar.
	 * @return
	 * 			Nome(s) do(s) medicamento(s) pertencente(s) a categoria especificada.
	 * @throws Exception 
	 */
	public String consultaMedCategoria(String categoria) throws Exception{
		
		categoria = categoria.toUpperCase();
		
		Categorias[] noEnum =  Categorias.values(); //array com as categorias possiveis definidas no enum
		
		ArrayList<Medicamento> medicamentosOrdenados = new ArrayList<Medicamento>();
		
		ArrayList<String> categoriasDisponiveis = new ArrayList<String>();
		
		for (Categorias categ : noEnum) {
			categoriasDisponiveis.add(categ.name());
		}
		
		if(categoriasDisponiveis.contains(categoria)){

			for (Medicamento medicamento : medicamentos) { //para cada medicamento cadastrado
				for (Categorias c : medicamento.getCategorias()) {
					if(c.name().equals(categoria)){
						medicamentosOrdenados.add(medicamento);
					}
				}
			}
					
			if(medicamentosOrdenados == null || medicamentosOrdenados.isEmpty()){ //se nao encontrar nenhum medicamento com a categoria especificada
				throw new Exception("Erro na consulta de medicamentos. Nao ha remedios cadastrados nessa categoria.");
					
			}		
			Collections.sort(medicamentosOrdenados);
			
			String retorno = "";
			for (Medicamento m : medicamentosOrdenados) {
				retorno += m.getNome() + ",";
			}
			
			return retorno.substring(0, retorno.length()-1);
			
		}

		throw new Exception("Erro na consulta de medicamentos. Categoria invalida.");
	}
	
	/**
	 * Pesquisa as informcoes relaciadas a um determinado medicamento.
	 * 
	 * @param nome
	 * 			Nome do medicamento que obtido as informacoes.
	 * @return
	 * 			Informacoes de um medicamento: nome, preco, quantidade...
	 * @throws SOOSException
	 */
	public String consultaMedNome(String nome)throws SOOSException{
		
		verificaSeNometemErro(nome, "Erro na consulta de medicamentos");
		
		for (Medicamento medicamento : medicamentos) {
			if(nome.equalsIgnoreCase(medicamento.getNome())){
				return medicamento.toString();
			}
		}
		throw new NomeException("Erro na consulta de medicamentos. Medicamento nao cadastrado.");
	}
	
	/**
	 * Retorna nome dos medicamentos cadastrados da farmacia.
	 * 
	 * @param ordenacao
	 * 			Tipo de ordenacao dos medicamentos que sera retornada:
	 * 			Ordem alfabetica ou por menor preco.
	 * @return
	 * @throws SOOSException
	 */
	public String getEstoqueFarmacia(String ordenacao) throws SOOSException{
		
		switch(ordenacao.trim().toLowerCase()){
		
		case "preco":
			
			Collections.sort(medicamentos);
			
			String retorno = "";
			for (Medicamento m : medicamentos) {
				retorno += m.getNome() + ",";
			}
			
			return retorno.substring(0, retorno.length()-1);
		case "alfabetica":
			ArrayList<String> nomes = new ArrayList<>();

			for (int i = 0; i < medicamentos.size(); i++) {
				nomes.add(medicamentos.get(i).getNome());
			}
			Collections.sort(nomes);
			
			String saida = "";
			for (String string : nomes) {
				saida = saida + string + ",";
			}
			return saida.substring(0,saida.length()-1);
		default:
			throw new EntradaException("Erro na consulta de medicamentos. Tipo de ordenacao invalida.");
		}
	}

	/**
	 * Pesquisa medicacamento por nome e retorna sua instancia.
	 * 
	 * @param nome
	 * @return
	 * @throws SOOSException
	 */
	private Medicamento pesquisaMedicamento(String nome)throws SOOSException{
		for (Medicamento medicamento : medicamentos) {
			if(nome.equalsIgnoreCase(medicamento.getNome())){
				return medicamento;
			}
		}		
		return null;
	}
	
	/**
	 * Calcula o desconto de 40% no preco de um medicamento generico.
	 * 
	 * @param valor
	 * @return
	 */
	private double calculaPrecoGenerico(double valor){
		double valorComDesconto = valor - (0.4 * valor);
		return valorComDesconto;
	}

	/////////////////////
	private void verificaSeNometemErro(String nome, String localDoErro) throws SOOSException {
		
		if (nome == null|| nome.trim().equalsIgnoreCase("")) {
			throw new EntradaException(localDoErro + "Nome do medicamento nao pode ser vazio.");
		}
	}
	//////////////////////
	private void verificaSePrecotemErro(double preco, String localDoErro) throws SOOSException{
		if (preco < 0){
			throw new EntradaException(localDoErro + "Preco do medicamento nao pode ser negativo.");
		}
	}
	///////////////////
	private void verificaSeQuantidadetemErro(int quantidade, String localDoErro)throws SOOSException {
		if (quantidade < 0){
			throw new EntradaException(localDoErro + "Quantidade do medicamento nao pode ser negativo.");
		}
	}
}