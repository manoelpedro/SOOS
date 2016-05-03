package programas;

import static org.junit.Assert.*;
import org.junit.Test;

public class ControllerTest {
	
	Controller controle;
	
	public ControllerTest(){
		controle = new Controller();
	}
	
	//CLASSE APENAS PARA FINS DE TESTES

	@Test
	public void ControllerTest() throws Exception{
	
		System.out.println("############ teste classe controller ############");
		System.out.println();
		
		
		System.out.println("# TENTANDO LIBERAR O SISTEMA");
		try{
			controle.liberaSistema("chaveErrada", "Marrie Curie", "07/11/1967");			
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		
		System.out.println("# LIBERANDO O SISTEMA");
		controle.liberaSistema("c041ebf8", "Marie Curie", "07/11/1967");			
		System.out.println();
		
		
		System.out.println("# ALGUEM TENTADO LIBERAR NOVAMENTE O SISTEMA");
		try{
			controle.liberaSistema("c041ebf8", "Marie Curie", "07/11/1967");			
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		
		System.out.println("# ALGUEM TENTADO LIBERAR NOVAMENTE O SISTEMA");
		try {
			controle.liberaSistema("chaveErrada", "Marie Curie", "07/11/1967");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		
		System.out.println("# LOGANDO NO SISTEMA");
		controle.login("12016001", "19671201");
		System.out.println();
		
		
		System.out.println("# CADASTRANDO UM TEC ADM");
		controle.cadastraFuncionario("Manoel", "Tecnico Administrativo", "11/11/1994");
		System.out.println();
		
		
		System.out.println("# CADASTRANDO UM MEDICO");
		controle.cadastraFuncionario("Drauzio Varella", "Medico", "15/12/1960");
		System.out.println();
		
		
		System.out.println("# CADASTRANDO UM FUNCIONARIO COM NOME VAZIO");
		try{
			controle.cadastraFuncionario("", "Medico", "10/10/1994");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		
		System.out.println("# CADASTRANDO UM FUNCIONARIO COM DATA INVALIDA");
		try{
			controle.cadastraFuncionario("Fulano da Silva", "Medico", "");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		
		System.out.println("# CADASTRANDO UM FUNCIONARIO COM CARGO VAZIO");
		try{
			controle.cadastraFuncionario("Ninguem", "", "10/10/1994");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		
		System.out.println("# CADASTRANDO UM FUNCIONARIO COM CARGO INVALIDO");
		try{
			controle.cadastraFuncionario("Bill", "faz nada", "10/10/1994");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		
		System.out.println("# CADASTRANDO UM OUTRO DIRETOR GERAL");
		try{
			controle.cadastraFuncionario("Gugu Liberato", "Diretor Geral", "01/01/1970");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		System.out.println(controle.getInfoFuncionario("32016002", "Nome"));
		
		try{
			System.out.println(controle.pesquisaFuncionario("12016001").getNome());			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		try{
			controle.logout();
			System.out.println("fez logout");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		try{
			controle.logout();
			System.out.println("fez logout");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		try{
			controle.login("1216001", "19671201");
			System.out.println("fez login");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
}
