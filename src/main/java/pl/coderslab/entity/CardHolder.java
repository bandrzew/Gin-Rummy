package pl.coderslab.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cardHolders")
public class CardHolder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	@OneToMany
	protected List<Card> cards = new ArrayList<>();

	public List<Card> getHand() {
		return cards;
	}

	@Override
	public String toString() {
		return cards.toString();
	}

}
