package pl.coderslab.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cards")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int value;
	private String color;
	private String visibleValue;
	@ManyToOne
	private CardHolder cardHolder;

	@Override
	public String toString() {
		return String.format("%s%s", visibleValue, color);
	}

	public Card() {

	}

	public Card(int value, String color) {
		this.color = color;
		switch (value) {
		case (1):
			this.value = value;
			visibleValue = "A";
			break;
		case (11):
			this.value = 10;
			visibleValue = "J";
			break;
		case (12):
			this.value = 10;
			visibleValue = "Q";
			break;
		case (13):
			this.value = 10;
			visibleValue = "K";
			break;
		default:
			this.value = value;
			visibleValue = Integer.toString(value);
		}
	}

	public boolean equalsByValue(Card card) {
		return this.value == card.getValue();
	}

	public boolean isNext(Card card) {
		return (this.equalsByColor(card) && this.value + 1 == card.getValue());
	}

	public boolean equalsByColor(Card card) {
		return this.color == card.getColor();
	}

	public int getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}
}
