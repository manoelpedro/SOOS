package hospital;

import paciente.Paciente;
import farmacia.Farmacia;

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
	
	public int cadastraPaciente(String nome, String dataNascimento, double peso, String sexo, String genero, String tipoSanguineo) throws Exception{
		return controle.cadastraPaciente(nome, dataNascimento, peso, sexo, genero, tipoSanguineo);
	}
	
	public String getInfoPaciente(int id, String atributo) throws Exception{
		return controle.getInfoPaciente(id, atributo);
	}
	
	public int getProntuario(int posicao) throws Exception{
		return controle.getProntuario(posicao);
	}
	
	//          vvvvvvvvv farmacia vvvvvvvvvv
	
	public String cadastraMedicamento(String nome, String tipo, double preco,int quantidade, String categorias) throws Exception{
		return controle.cadastraMedicamento(nome, tipo, preco, quantidade, categorias);
	}
	
	public String getInfoMedicamento(String atributo, String nome) throws Exception{
		return controle.getInfoMedicamento(atributo, nome);
	}
	
	public String atualizaMedicamento(String nome, String preco, String novoValor) throws Exception{
		return controle.atualizaMedicamento(nome, preco, novoValor);
	}
	
	public String consultaMedCategoria(String categoria) throws Exception {
		return controle.consultaMedCategoria(categoria);
	}
	
	public Object consultaMedNome(String nome) throws Exception{
		return controle.consultaMedNome(nome);
		
	}
	
	public String getEstoqueFarmacia(String tipo_ordenacao) throws Exception{
		return controle.getEstoqueFarmacia(tipo_ordenacao);
	}
	
	//      vvvvvvvvvvvvvv passo 5 vvvvvvvvvvvvvvvvv
	
	public void cadastraOrgao(String nome, String tipoSanguineo) throws Exception{
		controle.cadastraOrgao(nome, tipoSanguineo);
	}
	
	public String buscaOrgPorSangue(String tipoSanguineo) throws Exception{
		return controle.buscaOrgPorSangue(tipoSanguineo);
	}
	
	public String buscaOrgPorNome(String tipoSanguineo) throws Exception{
		return controle.buscaOrgPorNome(tipoSanguineo);
	}
	
	public boolean buscaOrgao(String nome, String tipoSanguineo) throws Exception{
		return controle.buscaOrgao(nome, tipoSanguineo);
	}
	
	public void retiraOrgao(String nome, String tipoSanguineo) throws Exception{
		controle.retiraOrgao(nome, tipoSanguineo);
	}
	
	public int qtdOrgaos(String nome) throws Exception{
		return controle.qtdOrgaos(nome);
	}
	
	public int totalOrgaosDisponiveis(){
		return controle.totalOrgaosDisponiveis();
	}
	/////////////////passo 6//////////////////
	
	public int getPacienteID(String nome) throws Exception{
		return controle.getPacienteID(nome);
	}
	
	public void realizaProcedimento(String procedimento, String IDNomePaciente, String nomesMedicamentos) throws Exception{
		controle.realizaProcedimento(procedimento, IDNomePaciente, nomesMedicamentos);
	}
	
	public void realizaProcedimento(String procedimento, String IDNomePaciente, String orgao, String nomesMedicamentos) throws Exception{
		controle.realizaProcedimento(procedimento, IDNomePaciente, orgao, nomesMedicamentos);
	}
	
	public int getTotalProcedimento(String id){
		return controle.getTotalProcedimento(id);
	}
}