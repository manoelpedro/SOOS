package hospital;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;

import exceptions.*;
import farmacia.*;
import funcionario.*;
import paciente.*;
import procedimento.Procedimentos;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

import orgao.FactoryOrgaos;
import orgao.Orgao;

/**
 * Classe que gerencia a execucao do sistema.
 */
public class Controller {

	private HashMap<String, Funcionario> funcionarios;
	private ArrayList<Paciente> pacientes;
	private ArrayList<Prontuario> prontuarios;
	private ArrayList<Orgao> orgaos;
	
	private FactoryFuncionario factoryFuncionario;
	private FactoryPaciente factoryPaciente;
	private FactoryOrgaos factoryOrgao;

	private Funcionario funcionarioLogado;
	private Farmacia farmacia; 

	private final String CHAVE;
	private int qtd_funcionarios_ja_cadastrados;
	private int qtd_pacientes_jah_cadastrados;
	private boolean sistemaLiberado;
	private LocalDate dataDeHoje;

	/**
	 * Metodo Construtor da classe Controller
	 */
	public Controller() {
		funcionarios = new HashMap<String, Funcionario>();
		pacientes = new ArrayList<Paciente>();
		prontuarios = new ArrayList<Prontuario>();
		orgaos = new ArrayList<Orgao>();
		
		factoryFuncionario = new FactoryFuncionario();
		factoryPaciente = new FactoryPaciente();
		factoryOrgao = new FactoryOrgaos();
		
		farmacia = new Farmacia();

		CHAVE = "c041ebf8";
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
	 * @throws Exception 
	 */
	public String liberaSistema(String chave, String nome, String dataNascimento) throws Exception {

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
	 * @throws Exception 
	 */
	public String cadastraFuncionario(String nome, String cargo, String dataNascimento) throws Exception {

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
		//nao eh verificado se funcionario ja existe
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
		
		switch (atributo.trim().toLowerCase()) {
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
		if (nome == null || nome.trim().equalsIgnoreCase("")) {
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
	 * Gera uma matricula utilizando o ID do funcionario (1 caractere),
	 * o ano atual (4 caracteres), 
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

		qtd_funcionarios_ja_cadastrados += 1;

		DecimalFormat formatar = new DecimalFormat("000");
		String cadastros_realizados_3digitos = formatar.format(qtd_funcionarios_ja_cadastrados);

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
	
	/**
	 * Cadastra um paciente.
	 * 
	 * @param nome
	 * 			Nome do paciente que sera cadastrado.
	 * @param dataNascimento
	 * 			Data de nascimento do paciente.
	 * @param peso
	 * 			Peso do paciente.
	 * @param sexo
	 * 			Sexo do paciente.
	 * @param genero
	 * 			Genero do paciente.
	 * @param tipoSanguineo
	 * 			Tipo sanguineo do paciente.
	 * @throws Exception
	 */
	public int cadastraPaciente(String nome, String dataNascimento, double peso, String sexo, String genero, String tipoSanguineo) throws Exception{
		
		if (!(funcionarioLogado instanceof TecAdministrativo)) { // se nao for Tecnico Administrativo
			throw new Exception("Nao foi possivel cadastrar o paciente. O funcionario "+funcionarioLogado.getNome()+" nao tem permissao para cadastrar pacientes.");
		}
		//validacao dos demais parametros de entrada estao sendo feitos dentro da Factory de Paciente
		Paciente novoPaciente = factoryPaciente.criaPaciente(nome, dataNascimento, peso, sexo, genero, tipoSanguineo);
		
		for (Paciente paciente : pacientes) {
			if (paciente.equals(novoPaciente)){
				throw new Exception("Nao foi possivel cadastrar o paciente. Paciente ja cadastrado.");
			}
		}
		novoPaciente.setId(qtd_pacientes_jah_cadastrados += 1);
		
		Prontuario prontuario = new Prontuario();
		prontuario.setPaciente(novoPaciente);
		
		prontuarios.add(prontuario);
		Collections.sort(prontuarios);
		pacientes.add(novoPaciente);
		
		return novoPaciente.getId();
	}
	
	/**
	 * Retorna informacoes de um paciente.
	 * 
	 * @param id
	 * 			Id do paciente que se desja obter a informacao.
	 * @param atributo
	 * 			Atributo que sera retornado do paciente informado.
	 * @return
	 * 			Valor do atributo especificado.
	 * @throws Exception
	 */
	public String getInfoPaciente(int id, String atributo) throws Exception{
		
		Paciente paciente = pesquisaPaciente(id);
		
		switch(atributo.toLowerCase()){
		case "nome":
			return paciente.getNome();
		case "data":
			return paciente.getDataNascimento().toString();
		case "sexo":
			return paciente.getSexoBiologico();
		case "genero":
			return paciente.getGenero();
		case "tiposanguineo":
			return paciente.getTipoSanguineo();
		case "peso":
			return String.valueOf(paciente.getPeso());
		case "idade":
			paciente.setIdade(Integer.parseInt(calculaIdade(paciente)));
			return String.valueOf(paciente.getIdade());
		default:
			throw new Exception("Atributo invalido");
		}
	}
	
	/**
	 * Calcula a idade com base na data de nascimento e data atual.
	 * 
	 * @param paciente
	 * 			Paciente que se deseja calcular a idade.
	 * @return
	 * 			Idade em anos do paciente.
	 */
	private String calculaIdade(Paciente paciente){
		
		LocalDate nasc = paciente.getDataNascimento();
		LocalDate hoje = LocalDate.now();
		
		Period idade = Period.between(nasc, hoje); //periodo entre datas
		String anos = String.valueOf(idade.getYears());
		
		return anos;
	}
	
	/**
	 * Pesquisa paciente
	 * 
	 * @param id
	 * 			Id do paciente que se busca.
	 * @return
	 * 			Retorna uma instancia do paciente caso encontre.
	 * @throws Exception
	 */
	public Paciente pesquisaPaciente(int id) throws Exception{
		
		for (Paciente paciente : pacientes) {
			if(paciente.getId() == id){
				return paciente;
			}
		}
		throw new Exception("Erro na pesquisa. Paciente nao cadastrado.");
	}
	
	/**
	 * Retorna um prontuario de um paciente.
	 * 
	 * @param posicao
	 * 			Posicao do prontuario na lista de prontuarios.
	 * @return
	 * 			Retorna o id do paciente.
	 * @throws Exception
	 */
	public int getProntuario(int posicao) throws Exception{
		
		if(posicao < 0){
			throw new Exception("Erro ao consultar prontuario. Indice do prontuario nao pode ser negativo.");
		}else if(posicao > qtd_funcionarios_ja_cadastrados){
			throw new Exception("Erro ao consultar prontuario. Nao ha prontuarios suficientes (max = "+prontuarios.size()+").");
		}
		Prontuario prontuario = prontuarios.get(posicao);
		int id = prontuario.getPaciente().getId();
		
		return id;
	}
	
	//comunicacao com o controller da farmacia

	/**
	 * Metodo utilizado com a finalidade gerar comunicacao entre o Controller(Hospital) e a Farmacia.
	 * 
	 * @return
	 * 			Retorna uma instancia da farmacia.
	 */
	public Farmacia getFarmacia() {
		return farmacia;
	}
	
	public String cadastraMedicamento(String nome, String tipo, double preco, int quantidade, String categorias) throws Exception {
		if(!(funcionarioLogado instanceof TecAdministrativo)){
			throw new Exception("Erro no cadastro de medicamento. O funcionario "+funcionarioLogado.getNome()+" nao tem permissao para cadastrar medicamentos.");
		}
		return farmacia.cadastraMedicamento(nome, tipo, preco, quantidade, categorias);
	}
	
	public String getInfoMedicamento(String atributo, String nome) throws SOOSException{
		return farmacia.getInfoMedicamento(atributo, nome);
	}
	
	public String atualizaMedicamento(String nome, String atributo, String novoValor) throws SOOSException{
		return farmacia.atualizaMedicamento(nome, atributo, novoValor);
	}	

	public String consultaMedCategoria(String categoria) throws Exception{
		return farmacia.consultaMedCategoria(categoria);
		
	}

	public String consultaMedNome(String nome)throws SOOSException{
		return farmacia.consultaMedNome(nome);
	}

	public String getEstoqueFarmacia(String ordenacao) throws SOOSException{
		return farmacia.getEstoqueFarmacia(ordenacao);
	}
	
	//orgaos
	
	/**
	 * Cadastra um orgao no sistema.
	 * 
	 * @param nome
	 * @param tipoSanguineo
	 * @throws Exception
	 */
	public void cadastraOrgao(String nome, String tipoSanguineo) throws Exception{
		Orgao novoOrgao = factoryOrgao.recebeNovoOrgao(nome, tipoSanguineo);
		orgaos.add(novoOrgao);
	}
	
	/**
	 * Busca um orgao recebendo um tipo sanguineo e retorna o nome dos orgao encontrados.
	 * 
	 * @param tipoSanguineo
	 * @return
	 * @throws Exception
	 */
	public String buscaOrgPorSangue(String tipoSanguineo) throws Exception{
		
		if(verificaTipoSanguineo(tipoSanguineo) == false){
			throw new Exception("O banco de orgaos apresentou um erro. Tipo sanguineo invalido.");
		}
		String retorno = "";
		
		LinkedHashSet<String> nomesOrgaos = new LinkedHashSet<String>();
			
		for(Orgao orgao : orgaos){
			if(orgao.getTipoSanguineo().equalsIgnoreCase(tipoSanguineo)){
				nomesOrgaos.add(orgao.getNome());
			}
		}
		if(nomesOrgaos.isEmpty()){
			throw new Exception("O banco de orgaos apresentou um erro. Nao ha orgaos cadastrados para esse tipo sanguineo.");
		}
		for (String nome : nomesOrgaos) {
			retorno = retorno + nome + ",";	
		}
		return retorno.substring(0, retorno.length()-1);
	}
	
	/**
	 * Busca um orgao recebendo seu nome e retorna o tipo sanguineo.
	 * 
	 * @param tipoSanguineo
	 * @return
	 * @throws Exception
	 */
	public String buscaOrgPorNome(String nome) throws Exception{
		
		String retorno = "";
		
		for (Orgao orgao : orgaos) {
			if(orgao.getNome().equalsIgnoreCase(nome)){
				retorno = retorno + orgao.getTipoSanguineo() + ",";
			}
		}
		//se nao encontrar o orgao especificado como parametro
		if(retorno == ""){
			throw new Exception("O banco de orgaos apresentou um erro. Orgao nao cadastrado."); 
		}
		//se encontrar
		return retorno.substring(0,retorno.length()-1);
	}
	
	/**
	 * Verifica se o tipo sanguineo informado apresenta uma formatacao valida.
	 * 
	 * @param tipoSanguineo
	 * 			Tipo sanguineo a ser verificado.
	 * @return
	 * 			Retorna true se for um tipo valido, caso contrario retorna false.
	 */
	public boolean verificaTipoSanguineo(String tipoSanguineo){
		if(tipoSanguineo == null || tipoSanguineo.trim().equals("")){
			return false;
		}
		if(tipoSanguineo.matches("(?i)[abo]{1,2}+[+|-]")){ //utilizando expressoes regulares para validacao
			return true;
		}
		return false;
	}
	
	/**
	 * Busca orgao pelo nome e tipo sanguineo, retornando se exite ou nao.
	 * 
	 * @param nome
	 * 			Nome do orgao se busca.
	 * @param tipoSanguineo
	 * 			Tipo sanguineo do orgao.
	 * @return
	 * 			Retorna true caso encontre ou false se nao encontrar o orgao.
	 * @throws Exception
	 */
	public boolean buscaOrgao(String nome, String tipoSanguineo) throws Exception{
		
		if(verificaTipoSanguineo(tipoSanguineo) == false){
			throw new Exception("O banco de orgaos apresentou um erro. Tipo sanguineo invalido.");
		}
		for (Orgao orgao : orgaos) {
			if(orgao.getNome().equalsIgnoreCase(nome) && orgao.getTipoSanguineo().equalsIgnoreCase(tipoSanguineo)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retira um orgao do banco de orgaos.
	 * 
	 * @param nome
	 * 			Nome do orgao que se deseja retirar.
	 * @param tipoSanguineo
	 * 			Tipo sanguineo do orgao.
	 * @throws Exception
	 */
	public void retiraOrgao(String nome, String tipoSanguineo) throws Exception{
		
		if(verificaTipoSanguineo(tipoSanguineo) == false){
			throw new Exception("Erro na retirada de orgaos. Tipo sanguineo invalido.");
		}
		Orgao orgaoEncontrado = null;
		
		for (Orgao orgao : orgaos) {
			if(orgao.getNome().equalsIgnoreCase(nome) && orgao.getTipoSanguineo().equalsIgnoreCase(tipoSanguineo)){
				orgaoEncontrado = orgao;
			}
		}
		if(orgaoEncontrado == null){
			throw new Exception("Erro na retirada de orgaos. Orgao nao cadastrado.");
		}else{
			orgaos.remove(orgaoEncontrado);
		}
	}
	
	/**
	 * Retorna a quantidade de um orgao especifico.
	 * 
	 * @param nome
	 * 			Nome do orgao que deseja-se a quantidade.
	 * @return
	 * 			Quantidade dsponivel do orgao informado .
	 * @throws Exception
	 */
	public int qtdOrgaos(String nome) throws Exception{
		
		int qtd = 0;
		for (Orgao orgao : orgaos) {
			if(orgao.getNome().equalsIgnoreCase(nome)){
				qtd += 1;
			}
		}
		if(qtd == 0){
			throw new Exception("O banco de orgaos apresentou um erro. Orgao nao cadastrado.");
		}
		return qtd;
	}
	
	/**
	 * Retona quantidade total de orgaos disponiveis no banco de orgaos.
	 * 
	 * @return
	 * 			Quantidade de orgaos.
	 */
	public int totalOrgaosDisponiveis(){
		return orgaos.size();
	}
	
	/**
	 * Pesquisa e retorna um prontuario.
	 * 
	 * @param IdPaciente
	 * 			Retorna um prontuario recebendo o ID do paciente.
	 * @return
	 * 			Prontuario encontrado.
	 */
	public Prontuario pesquisaProntuario(String IdPaciente){
		for(Prontuario p : prontuarios){
			if(p.getPaciente().getId() == Integer.parseInt(IdPaciente)){
				return p;
			}
		}
		return null;
	}
	
	//IMPLEMENTACAO DO PASSO 6 
	
	/**
	 * Retorna o ID de um paciente se este estiver cadastrado no sistema.
	 * 
	 * @param nome
	 * 			Nome do paciente.
	 * @return
	 * 			Retorna o ID do paciente convertido em String.
	 * @throws Exception
	 */
	public int getPacienteID(String nome) throws Exception{
		
		for (Paciente p : pacientes) {
			if(p.getNome().equalsIgnoreCase(nome)){
				return p.getId();
			}
		}
		throw new Exception("Paciente nao cadastrado.");
	}
	
	/**
	 * Calcula os gastos com medicamentos utilizados em um procedimento;
	 * 
	 * @param nomesMedicamentos
	 * @return
	 * @throws Exception
	 */
	private double gastoComMedicamentos(String nomesMedicamentos) throws Exception{
		
		double gasto = 0;
		String[] medicamentos = nomesMedicamentos.split(",");
		for (String nome : medicamentos) {
			Medicamento medicamento = farmacia.pesquisaMedicamento(nome);
			if(medicamento == null){
				throw new Exception("Erro na realizacao de procedimentos. Medicamento nao cadastrado.");
			}
			gasto += medicamento.getPreco();
		}
		return gasto;
	}
	
	/**
	 * Realiza procedimento.
	 * 
	 * @param procedimento
	 * 			Procedimento que sera realizado.
	 * @param NomePaciente
	 * 			Nome do paciente.
	 * @param nomesMedicamentos
	 * 			Nomes dos medicamentos necessarios para o procedimento.
	 * @throws Exception
	 */
	public void realizaProcedimento(String procedimento, String idPaciente, String nomesMedicamentos) throws Exception{
		
		if (!(funcionarioLogado instanceof Medico)) { 
			throw new Exception("Erro na realizacao de procedimentos. O funcionario "+funcionarioLogado.getNome()+" nao tem permissao para realizar procedimentos.");
		}
		
		if(idPaciente == null || idPaciente.trim().equals("")){
			throw new Exception("Erro na realizacao de procedimentos. ID do paciente nao pode ser vazio.");
		}
		if(nomesMedicamentos == null || nomesMedicamentos.trim().equals("")){
			throw new Exception("Erro na realizacao de procedimentos. Nome do medicamento nao pode ser vazio.");
		}
		
		double gasto = gastoComMedicamentos(nomesMedicamentos);
		
		Paciente p = pesquisaPaciente(Integer.parseInt(idPaciente));
		Prontuario prontuario = pesquisaProntuario(idPaciente);
		
		switch(procedimento.trim().toLowerCase()){
		case "redesignacao sexual":
			if(p.getGenero().equalsIgnoreCase("masculino")){
				p.setGenero("feminino");
			}
			p.setGenero("masculino");
			prontuario.getProcedimentos().add(procedimento);
			gasto += 9300;
			p.setTotalGasto(gasto);
			break;
		
		case "cirurgia bariatrica":
			double novoPeso = p.getPeso() - (0.10 * p.getPeso());
			p.setPeso(novoPeso);
			prontuario.getProcedimentos().add(procedimento);
			gasto += 7600;
			p.setTotalGasto((int)gasto);
			break;
		
		case "consulta clinica":
			prontuario.getProcedimentos().add(procedimento);
			gasto += 350;
			p.setTotalGasto(gasto);
			break;
		
		default:
			throw new Exception("Erro na realizacao de procedimentos. Procedimento invalido.");
		}
		
		//atualizar preco total(medicamento + procedimento)
		
	}
	
	
	/**
	 * Realiza procedimento recebendo orgao.
	 * 
	 * @param procedimento
	 * 			Procedimento que sera realizado.
	 * @param NomePaciente
	 * 			Nome do paciente.
	 * @param orgao
	 * 			Orgao que sera transplantado.
	 * @param nomesMedicamentos
	 * 			Medicamentos necessarios para o procedimento.
	 * @throws Exception
	 */
	public void realizaProcedimento(String procedimento, String idPaciente, String orgao, String nomesMedicamentos) throws Exception{
		
		if (!(funcionarioLogado instanceof Medico)) { 
			throw new Exception("Erro na realizacao de procedimentos. O funcionario "+funcionarioLogado.getNome()+" nao tem permissao para realizar procedimentos.");
		}
		
		if(!(procedimento.trim().equalsIgnoreCase("Transplante de orgaos"))){
			throw new Exception("Erro na realizacao de procedimentos. Procedimento invalido.");
		}
		if(orgao == null || orgao.trim().equals("")){
			throw new Exception("Erro na realizacao de procedimentos. Nome do orgao nao pode ser vazio.");
		}
		
		Paciente paciente = pesquisaPaciente(Integer.parseInt(idPaciente));
		procuraOrgao(orgao, paciente.getTipoSanguineo());
		
		double gasto = gastoComMedicamentos(nomesMedicamentos);
		paciente.setTotalGasto(gasto + 12500);
		
		Prontuario prontuario = pesquisaProntuario(idPaciente);
		prontuario.getProcedimentos().add(procedimento);
		
		//aqui vai o preco do transplante
			
	}
	
	/**
	 * Procura um orgao disponivel para realizacao de um transplante.
	 * 
	 * @param orgao
	 * @param tipoSanguineo
	 * @return
	 * @throws Exception
	 */
	private boolean procuraOrgao(String orgao, String tipoSanguineo) throws Exception{
		for (Orgao o : orgaos) {
			if(orgao.equalsIgnoreCase(o.getNome()) && tipoSanguineo.equalsIgnoreCase(o.getTipoSanguineo())){
				return true;
			}
		}
		throw new Exception("Erro na realizacao de procedimentos. Banco nao possui o orgao especificado.");
	}
	
	/**
	 * Valida procedimento.
	 * 
	 * @param procedimento
	 * @return
	 * @throws Exception
	 */
	public boolean verificaProcedimento(String procedimento) throws Exception{ //rever
		Procedimentos[] procedimentosExistentes = Procedimentos.values();
		for (Procedimentos p : procedimentosExistentes) {
			if(procedimento.trim().equalsIgnoreCase(p.name())){
				return true;
			}
		}
		throw new Exception("Erro na realizacao de procedimentos. Procedimento invalido.");
	}
	
	/**
	 * Retorna a quantida de procedimentos de um determinado paciente.
	 * 
	 * @param id
	 * 			ID do paciente.
	 * @return
	 * 			Quantidade de procedimentos realizados
	 */
	public int getTotalProcedimento(String id){
		Prontuario p = pesquisaProntuario(id);
		return p.getTotalProcedimento();
	}
	
	public int getPontosFidelidade(String id) throws Exception{
		Paciente p = pesquisaPaciente(Integer.parseInt(id));
		return p.getCartaoFidelidade().getPontosFidelidade();
	}
	
	public double getGastosPaciente(String id) throws Exception{
		Paciente p = pesquisaPaciente(Integer.parseInt(id));
		return p.getTotalGasto();
		
	}
	
}