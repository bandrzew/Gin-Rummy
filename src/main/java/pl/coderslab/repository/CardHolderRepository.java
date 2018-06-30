package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.coderslab.entity.CardHolder;

public interface CardHolderRepository extends JpaRepository<CardHolder, Long> {

}
