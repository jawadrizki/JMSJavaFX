package main;

public class Document {
	private Long id;
	private String nom;
	private String format;
	private double taille;
	
	
	public Document(String nom, String format, double taille) {
		super();
		this.nom = nom;
		this.format = format;
		this.taille = taille;
	}
	public Document() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public double getTaille() {
		return taille;
	}
	public void setTaille(double taille) {
		this.taille = taille;
	}
	
	
}
