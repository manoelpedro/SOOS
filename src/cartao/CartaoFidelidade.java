package cartao;

public class CartaoFidelidade {
	
	private double pontosFidelidade;
	private Tipo tipo;
	
	public CartaoFidelidade(){
		this.pontosFidelidade = 0;
		this.tipo = new Padrao();
	}
	
	public void verificaTipo(double pontos, Tipo tipo){
		if (pontos > 350 ){
			setTipo(new Vip());
		}
		else if((pontos <= 350) && (pontos >= 150)){
			setTipo(new Master());
			}
	}

	public double getPontosFidelidade() {
		return pontosFidelidade;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
	
	public void adicionaPontos(int pontosProcedimento ){
		pontosFidelidade += (tipo.getBonus() * pontosProcedimento);
		
	}
	

}
