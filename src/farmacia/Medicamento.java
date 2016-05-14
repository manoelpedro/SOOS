package farmacia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Medicamento implements Comparable<Medicamento>{

	private String nome;
	private double preco;
	private int quantidade;
	private LinkedHashSet <Categorias> categorias;
	private String tipo;
	
	public Medicamento(String nome,String tipo, double preco, int quantidade, LinkedHashSet <Categorias> categorias){
		
		this.nome = nome;
		this.tipo = tipo;
		this.preco = preco;
		this.quantidade = quantidade;
		this.categorias = categorias;
	}

	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public LinkedHashSet <Categorias> getCategorias() {
		return categorias;
	}

	public void setCategorias(LinkedHashSet <Categorias> categorias) {
		this.categorias = categorias;
	}

	@Override
	public int compareTo(Medicamento o) {
		if(this.preco < o.getPreco()){
			return -1;
		}else if(this.preco == o.getPreco()){
			return 0;
		}else{
			return 1;
		}
	
	}

	
	
	
}
