package it.betacom.ordini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.betacom.ordini.model.Immagine;

@Repository
public interface ImmagineRepository extends JpaRepository<Immagine, Long> {
}
