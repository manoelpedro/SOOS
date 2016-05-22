package cartao;

public class CartaoFidelidade {
	
	private int pontosFidelidade;
	private Tipo tipo;
	
	public CartaoFidelidade(){
		this.pontosFidelidade = 0;
		this.tipo = new Padrao();
	}
	
	public void verificaTipo(int pontos, Tipo tipo){
		if (pontos > 350 ){
			setTipo(new Vip());
		}
		else if((pontos <= 350) && (pontos >= 150)){
			setTipo(new Master());
			}
	}

	public int getPontosFidelidade() {
		return pontosFidelidade;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
	
	public void adicionaPontos(int pontosProcedimento ){
		pontosFidelidade += (tipo.getBonus() * pontosProcedimento);
		
	}
	

}
