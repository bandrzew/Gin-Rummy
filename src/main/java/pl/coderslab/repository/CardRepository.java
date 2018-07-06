package pl.coderslab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.coderslab.entity.Card;
import pl.coderslab.entity.CardHolder;

public interface CardRepository extends JpaRepository<Card, Long> {
	List<Card> findByCardHolder(CardHolder cardHolder);
}
