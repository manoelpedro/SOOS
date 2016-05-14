package cartao;

public class Vip implements Tipo {
	
	private double desconto = (70/100); // Desconto de 30%
	private double bonus = (110/100); // Bonus de 10%

	@Override
	public double getBonus(){
		return bonus;		
	}

	@Override
	public double getDesconto() {
		return desconto;
	}
	
	
}

