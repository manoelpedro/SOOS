package programas;

import java.util.HashMap;

import exceptions.EntradaException;
import exceptions.SOOSException;
import exceptions.SenhaException;
import exceptions.FuncionarioException;
import exceptions.LoginException;
import exceptions.MatriculaException;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Classe que gerencia a execucao do sistema.
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
	public Controller() {
		funcionarios = new HashMap<String, Funcionario>();
		CHAVE = "c041ebf8";
		factoryFuncionario = new FactoryFuncionario();
		sistemaLiberado = false;
	}

	/**
	 * Libera o sistema recebendo uma chave de desbloqueio e cadastra o primeiro usuario do sistema.
	 * 
	 * @param chave
	 *			Chave de desbloqueio do sistema.
	 * @param nome
	 *			Nome do primeiro ususario do sistema.
	 * @param dataNascimento
	 *			Data de nascimento do primeiro ususario do sistema. 
	 * @return 
	 * 			Retorna a matricula do usuario(funcionario) que desbloqueou o sistema.
	 * @throws SOOSException
	 */
	public String liberaSistema(String chave, String nome, String dataNascimento) throws SOOSException {

		if (sistemaLiberado) {
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

		return matricula;
	}

	/**
	 * Realiza o login do funcionario no sistema.
	 * 
	 * @param matricula
	 *			Matricula do funcionario.
	 * @param senha
	 *			Senha do funcionario. 
	 * @throws SOOSException
	 */
	public void login(String matricula, String senha) throws SOOSException {

		if (funcionarioLogado != null) { // eh pq tem alguem logado
			throw new LoginException("Nao foi possivel realizar o login. Um funcionario ainda esta logado: "
					+ funcionarioLogado.getNome() + ".");
		}

		verificaSeMatriculaTemErro(matricula, "Erro no login de funcionario. ");
		verificaSeSenhaTemErro(senha, "Erro no login de funcionario");

		if (!(funcionarios.containsKey(matricula))) { // se nao contem a matricula(chave) informada
			throw new FuncionarioException("Nao foi possivel realizar o login. Funcionario nao cadastrado.");
		}
		if (!(funcionarios.get(matricula).getSenha().equals(senha))) { // se a senha for diferente da senha do usuario tentando logar.
			throw new SenhaException("Nao foi possivel realizar o login. Senha incorreta.");
		}
		Funcionario funcionarioAcessando = pesquisaFuncionario(matricula);

		funcionarioLogado = funcionarioAcessando;
	}

	/**
	 * Desloga o funcionario do sistema.
	 * 
	 * @throws SOOSException		
	 */
	public void logout() throws SOOSException {
		if (funcionarioLogado == null) {
			throw new FuncionarioException("Nao foi possivel realizar o logout. Nao ha um funcionario logado.");
		} else {
			funcionarioLogado = null;
		}
	}

	/**
	 * Pesquisa um funcionario e retorna sua instancia caso encontre. 
	 * Retorna null se nao encontrar.
	 * 
	 * @param matricula
	 *			Matricula do funcionario.
	 * @return
	 * 			Retorna um Funcionario.
	 * @throws SOOSException
	 */
	public Funcionario pesquisaFuncionario(String matricula) throws SOOSException {
		
		verificaSeMatriculaTemErro(matricula, "Erro na consulta de funcionario. ");

		if (!(funcionarios.containsKey(matricula))) {
			throw new MatriculaException("Erro na consulta de funcionario. Funcionario nao cadastrado.");
		}
		Funcionario funcionario = funcionarios.get(matricula);
		return funcionario;
	}

	/**
	 * Cadastra um novo funcionario.
	 * 
	 * @param nome
	 *			Nome do funcionario que sera cadastrado.
	 * @param dataNascimento
	 *			Data de nascimento do funcionario que sera cadastrado.
	 * @param cargo
	 *			Cargo do funcionario que sera cadastrado.
	 * @throws SOOSException
	 */
	public String cadastraFuncionario(String nome, String cargo, String dataNascimento) throws SOOSException {

		if (!(funcionarioLogado instanceof Diretor)) { // se nao for diretor geral
			throw new FuncionarioException("Erro no cadastro de funcionario. O funcionario "
					+ funcionarioLogado.getNome() + " nao tem permissao para cadastrar funcionarios.");
		}
		verificaSeNomeTemErro(nome, "Erro no cadastro de funcionario. ");
		verificaCargo(cargo, "Erro no cadastro de funcionario. ");
		verificaSeDataTemErro(dataNascimento, "Erro no cadastro de funcionario. ");

		Funcionario novoFuncionario = factoryFuncionario.criaFuncionario(nome, cargo, dataNascimento);

		String matricula = geraMatricula(novoFuncionario);
		novoFuncionario.setMatricula(matricula);

		String senha = geraSenha(novoFuncionario);
		novoFuncionario.setSenha(senha);
		//nao verifica se funcionario ja existe
		funcionarios.put(matricula, novoFuncionario);

		return matricula;
	}

	/**
	 * Verifica se a senha informada eh um valor valido.
	 * 
	 * @param senha
	 * 			Senha que sera validada.
	 * @param localDoErro
	 * 			Texto que informa qual funcionalidade do sistema foi comprometida com o erro.
	 * @throws SOOSException
	 */
	private void verificaSeSenhaTemErro(String senha, String localDoErro) throws SOOSException {
		if (senha == null || senha.equals("")) {
			throw new EntradaException(localDoErro + "Senha invalida.");
		}
	}

	/**
	 * Verifica se a matricula informada eh um valor valido.
	 * 
	 * @param matricula
	 * 			Matricula que sera validada.
	 * @param localDoErro
	 * 			Texto que informa qual funcionalidade do sistema foi comprometida com o erro.
	 * @throws SOOSException
	 */
	private void verificaSeMatriculaTemErro(String matricula, String localDoErro) throws SOOSException {

		if (matricula == null || matricula.equals("")) {
			throw new EntradaException(localDoErro + "Matricula Invalida");
		}
		// se contem letras
		for (int i = 0; i < matricula.length(); i++) {
			char caractere = matricula.charAt(i);
			if (Character.isLetter(caractere)) {
				throw new MatriculaException(localDoErro + "A matricula nao segue o padrao.");
			}
		}
	}
	
	/**
	 * Exclui um funcionario do sistema.
	 * So pode ser executado por um Diretor Geral.
	 * 
	 * @param matricula
	 * 			Matricula do funcionario que sera excluido.
	 * @param senha
	 * 			Senha
	 * @throws SOOSException
	 */
	public void excluiFuncionario(String matricula, String senha) throws SOOSException {

		if (!(funcionarioLogado instanceof Diretor)) { //se usuario nao for o Diretor Geral
			throw new FuncionarioException("Erro ao excluir funcionario. O funcionario " + funcionarioLogado.getNome() + " nao tem permissao para excluir funcionarios.");
		}

		verificaSeMatriculaTemErro(matricula, "Erro ao excluir funcionario. ");
		verificaSeSenhaTemErro(senha, "Erro ao excluir funcionario. ");

		if (!(funcionarios.containsKey(matricula))) { //se nao existe a matricula(chave) informada
			throw new MatriculaException("Erro ao excluir funcionario. Funcionario nao cadastrado.");
		}
		if (!(funcionarioLogado.getSenha().equals(senha))) { //se a senha do usuario logado for dirente da informada
			throw new SenhaException("Erro ao excluir funcionario. Senha invalida.");
		}
		funcionarios.remove(matricula);
	}

	/**
	 * Atualiza informacoes(atributos) de qualquer funcionario.
	 * Apenas o Diretor Geral possui acesso.
	 * 
	 * @param matricula
	 * 			Matricula do funcioanrio que sera atualizado.
	 * @param atributo
	 * 			Atributo que se deseja alterar/atualizar.
	 * @param novoValor
	 * 			Novo valor que sera atribuido ao atributo informado.
	 * @throws SOOSException 
	 */
	public void atualizaInfoFuncionario(String matricula, String atributo, String novoValor) throws SOOSException{
			
		if (!(funcionarioLogado instanceof Diretor)){
			throw new FuncionarioException("Erro ao atualizar funcionario. Apenas diretor possui esse privilegio.");
		}
		
		Funcionario funcionario = pesquisaFuncionario(matricula);
		
		switch (atributo.toLowerCase()) {
		case "matricula":
			throw new EntradaException("Erro ao atualizar funcionario. Matricula nao pode ser alterada.");	
		case "nome":
			verificaSeNomeTemErro(novoValor, "Erro ao atualizar funcionario. ");
			funcionario.setNome(novoValor);
			break;
		case "data":
			verificaSeDataTemErro(novoValor, "Erro ao atualizar funcionario. ");
			String[] array = novoValor.split("/");
			LocalDate data = LocalDate.of(Integer.parseInt(array[2]), Integer.parseInt(array[1]),Integer.parseInt(array[0]));
			funcionario.setDataNascimento(data);
			break;
		case "senha": //Diretor Geral atualiza senha dos demais funcionarios
			verificaSeSenhaTemErro(novoValor, "Erro ao atualizar funcionario. ");
			if(novoValor.length() < 8 || novoValor.length() > 12){
				throw new SenhaException("Erro ao atualizar funcionario. A nova senha deve ter entre 8 - 12 caracteres alfanumericos.");
			}
			funcionario.setSenha(novoValor);
			break;
		default:
			throw new EntradaException("Erro ao atualizar funcionario. Atributo invalido.");
		}
	}
	
	/**
	 * Permite que um funcionario atualize suas informacoes/atributos.
	 * 
	 * @param atributo
	 * 			Atributo que se deseja alterar/atualizar.
	 * @param novoValor
	 * 			Novo valor que sera atribuido ao atributo informado.
	 * @throws SOOSException 
	 */
	public void atualizaInfoFuncionario(String atributo, String novoValor) throws SOOSException{
			
			switch (atributo.toLowerCase()) {
			case "matricula":
				throw new EntradaException("Erro ao atualizar funcionario. Matricula nao pode ser alterada.");
			case "nome":
				verificaSeNomeTemErro(novoValor, "Erro ao atualizar funcionario. ");
				funcionarioLogado.setNome(novoValor);
				break;
			case "data":
				verificaSeDataTemErro(novoValor, "Erro ao atualizar funcionario. ");
				String[] array = novoValor.split("/");
				LocalDate data = LocalDate.of(Integer.parseInt(array[2]), Integer.parseInt(array[1]),Integer.parseInt(array[0]));
				funcionarioLogado.setDataNascimento(data);
				break;
			case "senha":
				String antigaSenha = funcionarioLogado.getSenha();
				String novaSenha = novoValor;
				atualizaSenha(antigaSenha, novaSenha);
				break;
			default:
				throw new EntradaException("Erro ao atualizar funcionario. Atributo invalido.");
			}
	}
	
	/**
	 * Atualiza a senha de um funcionario
	 * 
	 * @param antigaSenha
	 * 			Senha atual do funcionario.
	 * @param novaSenha
	 * 			Nova senha que sera atribuida ao funcionario.
	 * @throws SOOSException
	 */
	public void atualizaSenha(String antigaSenha, String novaSenha) throws SOOSException {
		if(!(funcionarioLogado.getSenha().equals(antigaSenha))){
			throw new SenhaException("Erro ao atualizar funcionario. Senha invalida.");
		}
		if(novaSenha.length() < 8 || novaSenha.length() > 12){
			throw new SenhaException("Erro ao atualizar funcionario. A nova senha deve ter entre 8 - 12 caracteres alfanumericos.");
		}
		funcionarioLogado.setSenha(novaSenha);
	}

	/**
	 * Gera uma senha para um novo funcionario cadastrado no sistema. 
	 * Os 4 primeiros digitios sao o ano de nascimento do funcionario. 
	 * Os 4 ultimos digitos sao os 4 primeiros digitos da matricula.
	 * 
	 * @param funcionario
	 *			Recebe um objeto do tipo Funcionario que recebera uma senha.
	 * @return
	 * 			Retorna uma senha para o funcionario.
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
	 * 			Chave de liberacao do sistema.
	 * @param localDoErro
	 * 			Funcionalidade do sistema que foi comprometida com o erro.
	 * @throws SOOSException
	 */
	private void verificaChave(String chave, String localDoErro) throws SOOSException {

		if (chave == null || chave.equals("")) {
			throw new EntradaException(localDoErro + "Insira uma chave.");
		}
		if (!(chave.equals(CHAVE))) {
			throw new EntradaException(localDoErro + "Chave invalida.");
		}
	}

	/**
	 * Retorna o valor de um atributo do funcionario especificado como parametro.
	 * 
	 * @param matricula
	 *			Matricula de um funcionario.
	 * @param atributo
	 *			Atributo que sera retornado o seu valor.
	 * @return 
	 * 			Retorna o valor do atributo, caso este exista.
	 * @throws SOOSException
	 */
	public String getInfoFuncionario(String matricula, String atributo) throws SOOSException {
		
		Funcionario funcionario = pesquisaFuncionario(matricula);

		if (funcionario == null) {
			throw new FuncionarioException("Funcionario nao cadastrado");
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
			throw new EntradaException("Atributo invalido");
		}
	}

	/**
	 * Verifica se o nome informado eh valido.
	 * 
	 * @param nome
	 *			Nome do funcionario que sera validado.
	 * @param localDoErro
	 * 			Funcionalidade do sistema que foi comprometida em decorrencia do erro.
	 * @throws SOOSException
	 */
	private void verificaSeNomeTemErro(String nome, String localDoErro) throws SOOSException {
		if (nome == null || nome.equals("")) {
			throw new EntradaException(localDoErro + "Nome do funcionario nao pode ser vazio.");
		}
	}

	/**
	 * Verifica se a data de nascimento informada eh valida.
	 * 
	 * @param dataNascimento
	 * 			Data de nascimento que sera validada.
	 * @param localDoErro
	 * 			Funcionalidade do sistema que foi comprometida em decorrencia do erro.
	 * @throws SOOSException
	 */
	private void verificaSeDataTemErro(String dataNascimento, String localDoErro) throws SOOSException {

		if (dataNascimento == null || dataNascimento.equals("")) { 
			throw new EntradaException(localDoErro + "Data invalida.");
		}
		try {
			String[] array = dataNascimento.split("/");
			LocalDate.of(Integer.parseInt(array[2]), Integer.parseInt(array[1]), Integer.parseInt(array[0]));
		} catch (DateTimeException e) {
			throw new DateTimeException(localDoErro + "Data invalida.");
		}
	}

	/**
	 * Verifica se o cargo informado eh valido para o cadastro de um novo funcionario.
	 * 
	 * @param cargo
	 * 			Cargo que sera validado.
	 * @param localDoErro
	 * 			Funcionalidade do sistema que nao executou em decorrencia do erro.
	 * @throws SOOSException
	 */
	private void verificaCargo(String cargo, String localDoErro) throws SOOSException {

		if (cargo == null || cargo.equals("")) {
			throw new EntradaException(localDoErro + "Nome do cargo nao pode ser vazio.");
		}
		if (!(cargo.equalsIgnoreCase("Diretor Geral")) && !(cargo.equalsIgnoreCase("Medico"))
				&& !(cargo.equalsIgnoreCase("Tecnico Administrativo"))) {
			throw new EntradaException(localDoErro + "Cargo invalido.");
		}
		if (cargo.equals("Diretor Geral")) {
			throw new EntradaException(localDoErro + "Nao eh possivel criar mais de um Diretor Geral.");
		}
	}

	/**
	 * Gera uma matricula utilizando o ID do funcionario (1 caractere), o ano atual (4 caracteres), 
	 * e a quantidade de cadastros realizados(formatado em 3 caracteres);
	 * 
	 * @param funcionario
	 *			Recebe um funcionario que ira receber a matricula.
	 * @return 
	 * 			Retorna uma matricula com 8 caracteres numericos.
	 */
	private String geraMatricula(Funcionario funcionario) {

		String id = funcionario.getId();

		dataDeHoje = LocalDate.now(); //dia, mes e ano atual
		int anoAtual = dataDeHoje.getYear(); //apenas o ano

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
	 * 			Retorna uma instanca para um funcionario, ou null.
	 */
	public Funcionario getFuncionarioLogado() {
		return funcionarioLogado;
	}

}