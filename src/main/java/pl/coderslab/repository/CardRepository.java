package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.coderslab.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}
