package programas;

public class Facade {
	
	Controller controle;
	
	public Facade(){
		controle = new Controller();
	}
	
	/* 
	 * iniciaSistema e fechaSistema serao usados para carregar/armazenar os dados do sistema nos arquivos de dados. 
	 */
	public void iniciaSistema(){
		
	}
	
	public void fechaSistema() throws Exception{
		if(controle.getFuncionarioLogado() != null){
			throw new Exception("Nao foi possivel fechar o sistema. Um funcionario ainda esta logado: " + controle.getFuncionarioLogado().getNome() + ".");
		}
	}
	
	public String liberaSistema(String chave, String nome, String dataNascimento) throws Exception{
			return controle.liberaSistema(chave, nome, dataNascimento);
	}
		
	public void login(String matricula, String senha) throws Exception{
		controle.login(matricula, senha);
	}
	
	public void logout() throws Exception{
		controle.logout();
	}
	
	public String cadastraFuncionario(String nome, String cargo, String dataNascimento) throws Exception{
		return controle.cadastraFuncionario(nome, cargo, dataNascimento);
	}
	
	public String getInfoFuncionario(String matricula, String atributo) throws Exception{
		return controle.getInfoFuncionario(matricula, atributo);
	}
	
	public void excluiFuncionario(String matricula, String senha) throws Exception{
		controle.excluiFuncionario(matricula, senha);
	}
	
	public void atualizaInfoFuncionario(String matricula, String atributo, String novoValor) throws Exception{
		controle.atualizaInfoFuncionario(matricula, atributo, novoValor);
	}
	
	public void atualizaInfoFuncionario(String atributo, String novoValor) throws Exception{
		controle.atualizaInfoFuncionario(atributo, novoValor);
	}
	
	public void atualizaSenha(String antigaSenha, String novaSenha) throws Exception{
		controle.atualizaSenha(antigaSenha, novaSenha);
	}
	
}