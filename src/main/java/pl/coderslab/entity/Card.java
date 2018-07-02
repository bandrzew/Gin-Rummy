package pl.coderslab.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "cardHolder_ID", referencedColumnName = "ID")
	private CardHolder cardHolder;

	@Override
	public String toString() {
		return String.format("%s%s", visibleValue, color);
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

	public String getVisibleValue() {
		return visibleValue;
	}

	public void setVisibleValue(String visibleValue) {
		this.visibleValue = visibleValue;
	}

	public CardHolder getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(CardHolder cardHolder) {
		this.cardHolder = cardHolder;
	}

	public Long getId() {
		return id;
	}

	public void setValue(int value) {
		switch (value) {
		case (1):
			this.value = value;
			this.setVisibleValue("A");
			break;
		case (11):
			this.value = 10;
			this.setVisibleValue("J");
			break;
		case (12):
			this.value = 10;
			this.setVisibleValue("Q");
			break;
		case (13):
			this.value = 10;
			this.setVisibleValue("K");
			break;
		default:
			this.value = value;
			this.setVisibleValue(Integer.toString(value));
		}
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}
}
