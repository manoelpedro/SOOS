package programas;

import easyaccept.EasyAccept;

public class Main {

	public static void main(String[] args) {
	  
		args = new String[] {
	    	
				"programas.Facade", 
	    		"testes/usecase_1.txt",	"testes/usecase_2.txt"};	
	    	//	"testes/usecase_3.txt"},		
	    	//	"testes/usecase_4.txt"};
	   
		EasyAccept.main(args);
	}

}