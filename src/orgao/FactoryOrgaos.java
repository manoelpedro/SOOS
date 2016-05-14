package orgao;

public class FactoryOrgaos {

	public Orgao recebeNovoOrgao(String nome, String tipoSanguineo) throws Exception{
		
		ValidaCriacaoDeOrgaos(nome, tipoSanguineo);
		
		Orgao novoOrgao = new Orgao(nome, tipoSanguineo);
		
		return novoOrgao;
		
	}
	
	private void ValidaCriacaoDeOrgaos(String nome, String tipoSanguineo) throws Exception{
		
		if(nome == null || nome.trim().equals("")){
			throw new Exception("O banco de orgaos apresentou um erro. Nome do orgao nao pode ser vazio.");
		}
		
		if(tipoSanguineo == null || tipoSanguineo.trim().equals("") || (!(tipoSanguineo.equalsIgnoreCase("A+"))
				&& !(tipoSanguineo.equalsIgnoreCase("A-"))
				&& !(tipoSanguineo.equalsIgnoreCase("B+"))
				&& !(tipoSanguineo.equalsIgnoreCase("B-"))
				&& !(tipoSanguineo.equalsIgnoreCase("AB+"))
				&& !(tipoSanguineo.equalsIgnoreCase("AB-"))
				&& !(tipoSanguineo.equalsIgnoreCase("O+"))
				&& !(tipoSanguineo.equalsIgnoreCase("O-")))){
			
			throw new Exception("O banco de orgaos apresentou um erro. Tipo sanguineo invalido.");
		}
		
	}
	
}
