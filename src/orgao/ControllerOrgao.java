package orgao;

import java.util.HashMap;

import exceptions.EntradaException;
import exceptions.MatriculaException;
import exceptions.SOOSException;


//classe provisoria enquanto nao sai os testes
public class ControllerOrgao {
	
	private HashMap<Orgaos, String> orgaos;
	
	public ControllerOrgao(){
		orgaos = new HashMap<Orgaos, String>();
	}

	public void verificaSeNomeTemErro(String nome, String localDoErro) throws SOOSException{
		if (nome == null || nome.equals("")) {
			throw new EntradaException(localDoErro + "Nome invalido.");
		}
	}
	
	public void excluiOrgao(String nome, String tipoSanguineo) throws SOOSException {

		verificaSeNomeTemErro(nome, "Erro ao excluir orgao. ");

		if (!(orgaos.containsValue(nome))) { //se nao existe o nome(valor) informado
			throw new MatriculaException("Erro ao excluir funcionario. Funcionario nao cadastrado.");
		}
		
		orgaos.remove(nome);
	}
	
	//pesquisa orgao pelo nome
	public String pesquisaOrgaoNome(String nome) throws SOOSException{
		
		if (orgaos.containsValue(nome)){
			
			return nome;
			
		}else {
			throw new EntradaException("Orgao nao encontrado");
		}
		
		
	}
	//pesquisa orgao pelo tipoSanguineo
	public String pesquisaOrgaoTipoSanguineo(String tipoSanguineo) throws SOOSException{
		
		if (orgaos.containsValue(tipoSanguineo)){
			return tipoSanguineo;
		}else{
			throw new EntradaException("Orgao nao encontrado");
		}
		
	}
	//saber a quantidade de orgaos a partir do nome dele
	public void quantidadeOrgaoNome(String nome){
		 /*
         * O método "keySet()" retorna um Set com todas as chaves do
         * nosso HashMap, e tendo o Set com todas as Chaves, 
          * podemos facilmente pegar
         * os valores que desejamos
         * */
        
        for (Orgaos nome1 : orgaos.keySet()) {
               
               //Capturamos o valor a partir da chave
               String value = orgaos.get(nome1);
               System.out.println(nome1 + " = " + value);
        }

	}
	//obter a quantidade de orgao total no banco de cadastro
	public void quantidadeOrgao(){
		//coloca um acumulador se for todos os orgaos incluindo os que já foram transplantados
	}
	
}
