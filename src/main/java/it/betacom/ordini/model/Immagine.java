package it.betacom.ordini.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;

@Entity
@Table
@Data
public class Immagine implements Serializable {
	private static final long serialVersionUID = 8033670500829446937L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_articolo")
	private long idImmagine;
	
	@Column(nullable = false)
	private String path;
	
	@Column(nullable = false)
	private String descrizione;
	
	@OneToOne
	@MapsId
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Articolo articolo;

	public long getIdImmagine() {
		return idImmagine;
	}

	public void setIdImmagine(long idImmagine) {
		this.idImmagine = idImmagine;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Articolo getArticolo() {
		return articolo;
	}

	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}
	
	
}
