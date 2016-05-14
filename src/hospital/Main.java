package hospital;

import easyaccept.EasyAccept;

public class Main {

	public static void main(String[] args) {
	  
		args = new String[] {
	    	
				"hospital.Facade", 
	    		"testes/usecase_1.txt",	"testes/usecase_2.txt", "testes/usecase_3.txt", "testes/usecase_4.txt",
	    		"testes/usecase_5.txt",  "testes/usecase_6.txt", "testes/usecase_7.txt"};
	   
		EasyAccept.main(args);
	}

}
