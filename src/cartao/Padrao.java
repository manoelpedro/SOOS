package cartao;

public class Padrao implements Tipo{

	private double desconto = (0/100);
	private double bonus = (0/100);
	
	@Override
	public double getBonus(){
		return bonus;
	}

	@Override
	public double getDesconto() {
		return desconto;
	}
	
}