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
	private String visibleValue;
	private int points;
	private String color;
	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "cardHolder_ID", referencedColumnName = "id")
	private CardHolder cardHolder;
	private boolean isInMeld;

	@Override
	public String toString() {
		return String.format("%s%s", visibleValue, color);
	}

	public String getVisibleValue() {
		return visibleValue;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isInMeld() {
		return isInMeld;
	}

	public void setInMeld(boolean isInMeld) {
		this.isInMeld = isInMeld;
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
		this.value = value;

		switch (value) {
		case (1):
			this.setPoints(value);
			this.setVisibleValue("A");
			break;
		case (11):
			this.setPoints(10);
			this.setVisibleValue("J");
			break;
		case (12):
			this.setPoints(10);
			this.setVisibleValue("Q");
			break;
		case (13):
			this.setPoints(10);
			this.setVisibleValue("K");
			break;
		default:
			this.setPoints(value);
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
