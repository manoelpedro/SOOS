package programas;

public class Facade {
	
	Controller controle;
	
	/* 
	 * iniciaSistema e fechaSistema serao usados para carregar/armazenar
	 * os dados do sistema nos arquivos de dados. 
	 */
	public void iniciaSistema(){
		controle = new Controller();
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
	
}