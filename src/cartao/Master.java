package cartao;

public class Master implements Tipo {
	
	private double desconto = (85/100); // Desconto de 15%
	private double bonus = (105/100); // Bonus de 5%
	
	@Override
	public double getBonus(){
		return bonus;
	}
	@Override
	public double getDesconto() {
		return desconto;
	}

}
