package programas;

import java.util.HashMap;
import java.text.DecimalFormat;
//import java.text.NumberFormat;
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
	//private DecimalFormat formatar;
	private Funcionario funcionarioLogado;
	private boolean sistemaLiberado;
	//private boolean sistemaLiberado;
	
	
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
	public String liberaSistema(String chave, String nome, String dataNascimento) throws Exception {
		
		if(sistemaLiberado == true){
			throw new Exception("Erro ao liberar o sistema. Sistema liberado anteriormente.");
		}
		validaParametros(chave, nome, dataNascimento);
			
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
	public void login(String matricula, String senha) throws Exception{
		
		if(funcionarioLogado != null){ //eh pq tem alguem logado
			throw new Exception("Nao foi possivel realizar o login. Um funcionario ainda esta logado: " + funcionarioLogado.getNome() + ".");
		}
	
		validaLogin(matricula, senha);
		
		//se chegou aqui entao a matricula e senha estao corretas
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
	public void logout() throws Exception{
		
		if(funcionarioLogado == null){
			throw new Exception("Nao foi possivel realizar o logout. Nao ha um funcionario logado.");
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
	 */
	public Funcionario pesquisaFuncionario(String matricula) {
	
		Funcionario funcionario;	
		
		if(funcionarios.containsKey(matricula)){
			funcionario = funcionarios.get(matricula);
			return funcionario;
		}
		System.out.println("nao achou");
		return null;
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
	public String cadastraFuncionario(String nome, String cargo, String dataNascimento) throws Exception {
		
		if(!(funcionarioLogado.getCargo().equals("Diretor Geral"))){
			throw new Exception("Erro no cadastro de funcionario. O funcionario "+funcionarioLogado.getNome()+" nao tem permissao para cadastrar funcionarios.");
		}
		validaFuncionario(nome, cargo, dataNascimento);
		
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
	 * Verifica se sao validos os parametros recebidos no metodo libera sistema.
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
	 * @throws Exception
	 */
	private void validaParametros(String chave, String nome, String dataNascimento) throws Exception{
		
		if(chave == null || chave.equals("")){
			throw new Exception("Erro ao liberar o sistema. Insira uma chave.");
		}
		if(nome == null || nome.equals("")){
			throw new Exception("Erro ao liberar o sistema. Insira um nome.");
		}
		if(dataNascimento == null || dataNascimento.equals("")){ //falta para entradas tipo 34/23/2009
			throw new Exception("Erro ao liberar o sistema. Insira uma data valida.");
		}
		if (!(chave.equals(CHAVE))) {
			throw new Exception("Erro ao liberar o sistema. Chave invalida.");
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
	public String getInfoFuncionario(String matricula, String atributo) throws Exception{
		
		Funcionario funcionario = pesquisaFuncionario(matricula);
		
		if (funcionario == null){
			throw new Exception("Funcionario nao cadastrado");
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
			throw new Exception("Erro na consulta de funcionario. A senha do funcionario eh protegida.");
		default:
			throw new Exception("Este atributo nao existe");
		}		
	}
	
	/**
	 * Verifica se os parametros para logar sao validos, se a matricula existe e se a senha esta associada a ela.
	 * 
	 * @param matricula
	 * 			Matricula do funcionario que deseja fazer login no sistema.			
	 * 
	 * @param senha
	 * 			Senha do funcionario que deseja fazer login no sistema.
	 * 
	 * @throws Exception
	 */
	private void validaLogin(String matricula, String senha) throws Exception {
		
		if(matricula == null || matricula.equals("")){
			throw new Exception("Erro no login de funcionario. Matricula Invalida");
		}
		if(senha == null || senha.equals("")){
			throw new Exception("Erro no login de funcionario. Digite uma senha");
		}
		if(!(funcionarios.containsKey(matricula))){ //se nao contem a matricula(chave) informada
			throw new Exception("Nao foi possivel realizar o login. Funcionario nao cadastrado.");
		}
		if(!(funcionarios.get(matricula).getSenha().equals(senha))){ //se a senha for diferente da senha do usuario associado a matricula informada.
			throw new Exception("Nao foi possivel realizar o login. Senha incorreta.");
		}
	}

	/**
	 * Recebe atributos de um funcionario e verifica se sao validos de acordo com as regras
	 * para o cadastro de um novo funcionario. 
	 * Caso algum nao seja valido, sao lancadas excecoes.
	 * 
	 * @param nome
	 *			Nome do funcionario que sera cadastrado.
	 *
	 * @param dataNascimento
	 *			Date de Nascimento do funcionario que sera cadastrado.
	 *
	 * @param cargo
	 *			Cargo do funcionario que sera cadastrado.
	 * 
	 * @throws Exception
	 */
	private void validaFuncionario(String nome, String cargo, String dataNascimento) throws Exception {
		
		if(nome == null || nome.equals("")){
			throw new Exception("Erro no cadastro de funcionario. Nome do funcionario nao pode ser vazio.");
		}
		if(dataNascimento == null || dataNascimento.equals("")){ // falta excecao para coisas tipo 87/00/2016
			throw new Exception("Erro no cadastro de funcionario. Data invalida.");
		}
		if(cargo == null || cargo.equals("")){
			throw new Exception("Erro no cadastro de funcionario. Nome do cargo nao pode ser vazio.");
		}
		if(!(cargo.equals("Diretor Geral")) && !(cargo.equals("Medico")) && !(cargo.equals("Tecnico Administrativo"))){
			throw new Exception("Erro no cadastro de funcionario. Cargo invalido.");
		}
		if(cargo.equals("Diretor Geral")){
			throw new Exception("Erro no cadastro de funcionario. Nao eh possivel criar mais de um Diretor Geral.");
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