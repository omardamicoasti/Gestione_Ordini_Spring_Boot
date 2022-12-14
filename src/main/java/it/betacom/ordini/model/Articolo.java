package it.betacom.ordini.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table
@Data
public class Articolo implements Serializable {
	private static final long serialVersionUID = -1538290772321398177L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idArticolo;
	
	@Column(nullable = false)
	private String marca;
	
	@Column(nullable = false)
	private String modello;
	
	@Column(nullable = false)
	private double prezzo;
	
	@Column
	private boolean disponibile = true;
	
	@OneToMany(mappedBy = "articolo", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<OrdineArticolo> oa = new HashSet<OrdineArticolo>();

	public long getIdArticolo() {
		return idArticolo;
	}

	public void setIdArticolo(long idArticolo) {
		this.idArticolo = idArticolo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public boolean isDisponibile() {
		return disponibile;
	}

	public void setDisponibile(boolean disponibile) {
		this.disponibile = disponibile;
	}

	public Set<OrdineArticolo> getOa() {
		return oa;
	}

	public void setOa(Set<OrdineArticolo> oa) {
		this.oa = oa;
	}
	
	
}
