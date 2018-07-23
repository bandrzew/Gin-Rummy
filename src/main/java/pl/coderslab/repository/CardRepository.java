package pl.coderslab.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.coderslab.entity.Card;
import pl.coderslab.entity.CardHolder;

public interface CardRepository extends JpaRepository<Card, Long> {
	List<Card> findByCardHolder(CardHolder cardHolder);

	List<Card> findByCardHolderName(String cardHolderName);

	@Query("select c from Card c where c.cardHolder.name = ?1 and c.value = ?2 and c.isInMeld = false")
	List<Card> findByValueToSet(String cardHolderName, int value);

	@Query("select c from Card c where c.cardHolder.name = ?1")
	List<Card> findByNameAndSort(String cardHolderName, Sort sort);

	@Query("select c from Card c where c.cardHolder.name = ?1 and c.color = ?2")
	List<Card> findByNameAndColorWithSort(String cardHolderName, String color, Sort sort);

	Card findFirstByCardHolderName(String cardHolderName);

	Card findByValueAndColor(int value, String color);

	Card findByVisibleValueAndColor(String visibleValue, String color);
}
