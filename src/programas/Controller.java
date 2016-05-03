package programas;

import java.util.HashMap;

import exceptions.EntradaException;
import exceptions.SOOSException;
import exceptions.SenhaException;
import exceptions.FuncionarioException;
import exceptions.LoginException;
import exceptions.MatriculaException;

import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * 
 * Classe que gerencia a execucao do sistema.
 * 
 */
public class Controller {

	private HashMap<String, Funcionario> funcionarios;
	private final String CHAVE;
	private FactoryFuncionario factoryFuncionario;
	private LocalDate dataDeHoje;
	private int qtd_cadastros_realizados;
	private Funcionario funcionarioLogado;
	private boolean sistemaLiberado;
	
	/**
	 * Metodo Construtor da classe Controller
	 */
	public Controller(){
			funcionarios = new HashMap<String, Funcionario>();
			CHAVE = "c041ebf8";
			factoryFuncionario = new FactoryFuncionario();
			sistemaLiberado = false;
	}
	
	/**
	 * Libera o sistema recebendo uma chave de desbloqueio e criando o primeiro usuario do sistema.
	 * 
	 * @param chave
	 *			Chave de desbloqueio do sistema.
	 *
	 * @param nome
	 *			Nome do primeiro ususario do sistema.
	 * 
	 * @param dataNascimento
	 *			Data de nascimento do primeiro ususario do sistema.
	 * 
	 * @return
	 *			Retorna a matricula do usuario(funcionario) que desbloqueou o sistema.
	 * 
	 * @throws Exception
	 */
	public String liberaSistema(String chave, String nome, String dataNascimento) throws SOOSException {
		
		if(sistemaLiberado == true){
			throw new SOOSException("Erro ao liberar o sistema. Sistema liberado anteriormente.");
		}
		
		verificaSeNomeTemErro(nome, "Erro ao liberar o sistema. ");
		verificaSeDataTemErro(dataNascimento, "Erro ao liberar o sistema. ");
		verificaChave(chave, "Erro ao liberar o sistema. ");
			
		Funcionario novoFuncionario = factoryFuncionario.criaFuncionario(nome, "Diretor Geral", dataNascimento);

		String matricula = geraMatricula(novoFuncionario);
		novoFuncionario.setMatricula(matricula);

		String senha = geraSenha(novoFuncionario);
		novoFuncionario.setSenha(senha);

		sistemaLiberado = true;
		funcionarios.put(matricula, novoFuncionario);
		
		/*
		sysos apenas para fins de testes no controllerTest
		System.out.println("SISTEMA LIBERADO");
		System.out.println(novoFuncionario.getNome());
		System.out.println("Data de Nasc: "+novoFuncionario.getDataNascimento());
		System.out.println(novoFuncionario.getCargo());
		System.out.println("Matricula: "+novoFuncionario.getMatricula());
		System.out.println("Senha: "+novoFuncionario.getSenha());
		*/
	
		return matricula;
	}
	
	/**
	 * Realiza o login do funcionario no sistema.
	 * 
	 * @param matricula
	 * 			Matricula do funcionario.
	 * 
	 * @param senha
	 * 			Senha do funcionario.
	 * 
	 * @throws Exception
	 */
	public void login(String matricula, String senha) throws SOOSException{
		
		if(funcionarioLogado != null){ //eh pq tem alguem logado
			throw new LoginException("Nao foi possivel realizar o login. Um funcionario ainda esta logado: " + funcionarioLogado.getNome() + ".");
		}
		
		verificaSeMatriculaTemErro(matricula, "Erro no login de funcionario. ");
		verificaSeSenhaTemErro(senha, "Erro no login de funcionario");
		
		if(!(funcionarios.containsKey(matricula))){ //se nao contem a matricula(chave) informada
			throw new FuncionarioException("Nao foi possivel realizar o login. Funcionario nao cadastrado.");
		}
		if(!(funcionarios.get(matricula).getSenha().equals(senha))){ //se a senha for diferente da senha do usuario tentando logar.
			throw new SenhaException("Nao foi possivel realizar o login. Senha incorreta.");
		}
		
		Funcionario funcionarioAcessando = pesquisaFuncionario(matricula);
		
		funcionarioLogado = funcionarioAcessando;
		
		/*
		syso para fins de testes
		System.out.println("LOGADO NO SISTEMA");
		System.out.println(funcionarioLogado.getNome());
		System.out.println("Data de Nasc: "+funcionarioLogado.getDataNascimento());
		System.out.println(funcionarioLogado.getCargo());
		System.out.println("Matricula: "+funcionarioLogado.getMatricula());
		System.out.println("Senha: "+funcionarioLogado.getSenha());
		*/
	}
	
	/**
	 * Desloga o funcionario do sistema.
	 * 
	 * @throws Exception
	 */
	public void logout() throws SOOSException{
		
		if(funcionarioLogado == null){
			throw new FuncionarioException("Nao foi possivel realizar o logout. Nao ha um funcionario logado.");
		}else{
			funcionarioLogado = null;
		}
	}
	
	/**
	 * Pesquisa um funcionario e retorna sua instancia caso encontre. Retorna null se nao encontrar.
	 * 
	 * @param matricula
	 *			Matricula do funcionario.
	 *
	 * @return
	 * @throws Exception 
	 */
	public Funcionario pesquisaFuncionario(String matricula) throws SOOSException {
		
		verificaSeMatriculaTemErro(matricula, "Erro na consulta de funcionario. ");
		
		/*if(matricula == null || matricula.equals("")){
			throw new Exception("Erro na consulta de funcionario. Matricula Invalida");
		}
		
		//se conter letras
		for(int i = 0; i < matricula.length(); i++){
			char caractere = matricula.charAt(i);
			if(Character.isLetter(caractere)){
				throw new Exception("Erro na consulta de funcionario. A matricula nao segue o padrao.");
			}
		}*/
		
		if(!(funcionarios.containsKey(matricula))){ //se nao contem a matricula(chave) informada
			throw new MatriculaException("Erro na consulta de funcionario. Funcionario nao cadastrado.");
		}
		
		Funcionario funcionario = funcionarios.get(matricula);
		
		return funcionario;
	}

	/**
	 * Cadastra um novo funcionario.
	 * 
	 * @param nome
	 * 			Nome do funcionario que sera cadastrado.
	 * @param dataNascimento
	 * 			Data de nascimento do funcionario que sera cadastrado.
	 * @param cargo
	 * 			Cargo do funcionario que sera cadastrado.
	 * 
	 * @throws Exception
	 */
	public String cadastraFuncionario(String nome, String cargo, String dataNascimento) throws SOOSException {
		
		if(!(funcionarioLogado instanceof Diretor)){ //se nao for diretor geral
			throw new FuncionarioException("Erro no cadastro de funcionario. O funcionario "+funcionarioLogado.getNome()+" nao tem permissao para cadastrar funcionarios.");
		}

		verificaSeNomeTemErro(nome, "Erro no cadastro de funcionario. ");
		verificaCargo(cargo, "Erro no cadastro de funcionario. ");
		verificaSeDataTemErro(dataNascimento, "Erro no cadastro de funcionario. ");
		
		Funcionario novoFuncionario = factoryFuncionario.criaFuncionario(nome, cargo, dataNascimento);
		
		String matricula = geraMatricula(novoFuncionario);
		novoFuncionario.setMatricula(matricula);
		
		String senha = geraSenha(novoFuncionario);
		novoFuncionario.setSenha(senha);
		
		/* nao verifico se um funcionario ja existe pois nao tem um criterio
		 * de igualdade.
		 */
		funcionarios.put(matricula, novoFuncionario);
		
		/*
		esses sysos sao apenas para fins de teste la no controllerTest
		System.out.println("FUNCIONARIO CADASTRADO");
		System.out.println(novoFuncionario.getNome());
		System.out.println("Data de Nasc: "+novoFuncionario.getDataNascimento());
		System.out.println(novoFuncionario.getCargo());
		System.out.println("Matricula: "+novoFuncionario.getMatricula());
		System.out.println("Senha: "+novoFuncionario.getSenha());
		*/
	
		return matricula;
	}
	
	/**
	 * Verifica se a senha informada eh valida.
	 * 
	 * @param senha
	 * @param localDoErro
	 * @throws Exception
	 */
	private void verificaSeSenhaTemErro(String senha, String localDoErro) throws SOOSException{
		
		if (senha == null || senha.equals("")) {
			throw new EntradaException( localDoErro + "Digite uma senha");
		}
	}
	
	/**
	 * Verifica se a matricula informada eh um valor valido.
	 * 
	 * @param matricula
	 * @param localDoErro
	 * @throws Exception
	 */
	private void verificaSeMatriculaTemErro(String matricula, String localDoErro) throws SOOSException{
		
		if(matricula == null || matricula.equals("")){
			throw new EntradaException( localDoErro + "Matricula Invalida");
		}
		// se contem letras
		for (int i = 0; i < matricula.length(); i++) {
			char caractere = matricula.charAt(i);
			if (Character.isLetter(caractere)) {
				throw new MatriculaException( localDoErro + "A matricula nao segue o padrao.");
			}
		}
	}
	
	public void excluiFuncionario(String matricula, String senha) throws SOOSException{
		
		if(!(funcionarioLogado instanceof Diretor)){ //se usuario nao for o Diretor Geral
			throw new FuncionarioException("Erro ao excluir funcionario. O funcionario " + funcionarioLogado.getNome() + " nao tem permissao para excluir funcionarios.");
		}
		
		verificaSeMatriculaTemErro(matricula, "Erro ao excluir funcionario. ");
		verificaSeSenhaTemErro(senha, "Erro ao excluir funcionario. ");
		
		if(!(funcionarios.containsKey(matricula))){ //se nao existe a matricula(chave) informada
			throw new MatriculaException("Erro ao excluir funcionario. Funcionario nao cadastrado.");
		}
		if(!(funcionarioLogado.getSenha().equals(senha))){ //se a senha do usuario logado for dirente da informada
			throw new SenhaException("Erro ao excluir funcionario. Senha invalida.");
		}
		
		funcionarios.remove(matricula);
	}
	
	public void atualizaInfoFuncionario(String matricula, String atributo, String novoValor){
		
	}
	
	/**
	 * Gera uma senha para um novo funcionario cadastrado no sistema.
	 * Os 4 primeiros digitios sao o ano de nascimento do funcionario.
	 * Os 4 ultimos digitos sao os 4 primeiros digitos da matricula.
	 * 
	 * @param funcionario
	 * 			Recebe um objeto do tipo Funcionario que recebera uma senha.
	 * @return
	 */
	private String geraSenha(Funcionario funcionario) {
		
		LocalDate dataNascimento = funcionario.getDataNascimento();
		String ano = dataNascimento.toString().substring(0, 4);
		
		String matricula = funcionario.getMatricula();
		String quatroPrimeirosDigitosMatricula = matricula.substring(0, 4);
		
		String senha = ano + quatroPrimeirosDigitosMatricula;
		
		return senha;
	}
	
	/**
	 * Verifica se a chave que libera o sistema eh valida.
	 * 
	 * @param chave
	 * @param localDoErro
	 * @throws Exception
	 */
	private void verificaChave(String chave, String localDoErro) throws SOOSException{
		
		if(chave == null || chave.equals("")){
			throw new EntradaException( localDoErro + "Insira uma chave.");
		}
		if (!(chave.equals(CHAVE))) {
			throw new EntradaException( localDoErro + "Chave invalida.");
		}
	}
	
	/**
	 * Retorna o valor de um atributo do funcionario especificado como parametro.
	 * 
	 * @param matricula
	 * 			Matricula de um funcionario.
	 * 
	 * @param atributo
	 * 			Atributo que sera retornado o seu valor.
	 * 
	 * @return
	 * 			Retorna o valor do atributo.
	 * 
	 * @throws Exception
	 */
	public String getInfoFuncionario(String matricula, String atributo) throws SOOSException{
		
		Funcionario funcionario = pesquisaFuncionario(matricula);
		
		if (funcionario == null){
			throw new FuncionarioException("Funcionario nao cadastrado");
		}	
		
		if(atributo == null || atributo.equals("")){
			throw new EntradaException("Atributo invalido");
		}
		
		switch (atributo) {
		case "Nome":
			return funcionario.getNome();
		case "Data":
			String data = funcionario.getDataNascimento().toString();
			return data;
		case "Cargo":
			return funcionario.getCargo();
		case "Senha":
			throw new EntradaException("Erro na consulta de funcionario. A senha do funcionario eh protegida.");
		default:
			throw new EntradaException("Este atributo nao existe");
		}		
	}

	
	
	
	
	/**
	 * Verifica se o nome informado eh valido.
	 * 
	 * @param nome
	 * @param localDoErro
	 * @throws Exception
	 */
	private void verificaSeNomeTemErro(String nome, String localDoErro) throws SOOSException{
		
		if(nome == null || nome.equals("")){
			throw new EntradaException( localDoErro + "Nome do funcionario nao pode ser vazio.");
		}
	}
	
	/**
	 * Verifica se a data de nascimento informada eh valida.
	 * 
	 * @param localDoErro
	 * @param dataNascimento
	 * @throws Exception
	 */
	private void verificaSeDataTemErro(String dataNascimento, String localDoErro) throws SOOSException{
		
		if(dataNascimento == null || dataNascimento.equals("")){ //falta para entradas tipo 34/23/2009
			throw new EntradaException( localDoErro + "Data invalida.");
		}
	}
	
	/**
	 * Verifica se o cargo informado eh valido para o cadastro de um novo funcionario.
	 * 
	 * @param cargo
	 * @param localDoErro
	 * @throws Exception
	 */
	private void verificaCargo(String cargo, String localDoErro) throws SOOSException {
		
		if(cargo == null || cargo.equals("")){
			throw new EntradaException( localDoErro + "Nome do cargo nao pode ser vazio.");
		}
		if(!(cargo.equals("Diretor Geral")) && !(cargo.equals("Medico")) && !(cargo.equals("Tecnico Administrativo"))){
			throw new EntradaException( localDoErro + "Cargo invalido.");
		}
		if(cargo.equals("Diretor Geral")){
			throw new EntradaException( localDoErro + "Nao eh possivel criar mais de um Diretor Geral.");
		}
	}
	
	/**
	 * Gera uma matricula utilizando o ID do funcionario (1 caractere), 
	 * o ano atual (4 caracteres), 
	 * e a quantidade de cadastros realizados(formatado em 3 caracteres);
	 * 
	 * @param funcionario
	 * 			Recebe um funcionario que ira receber a matricula.
	 * 
	 * @return String 
	 * 			Retorna uma matricula com 8 caracteres numericos.
	 */
	private String geraMatricula(Funcionario funcionario) {
		
		String id = funcionario.getId();
		
		dataDeHoje = LocalDate.now(); // DIA, MES E ANO ATUAL
		int anoAtual = dataDeHoje.getYear(); // APENAS O ANO
		
		qtd_cadastros_realizados += 1;
		
		DecimalFormat formatar = new DecimalFormat("000");
		String cadastros_realizados_3digitos = formatar.format(qtd_cadastros_realizados);
		
		String matricula = id + anoAtual + cadastros_realizados_3digitos;
		
		return matricula;
	}
	
	/**
	 * Retorna um Objeto Funcionario, se algum estiver logado, ou null se nao estiver nenhum logado.
	 * 
	 * @return
	 *			Retorna uma instanca para um funcionario, ou null.
	 */
	public Funcionario getFuncionarioLogado() {
		return funcionarioLogado;
	}

}