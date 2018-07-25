package pl.coderslab.melds;

import java.util.List;

import pl.coderslab.entity.Card;

public interface Melds {
	public void find(List<Card> cards);

	public default void reset(List<Card> cards) {
		for (Card card : cards) {
			card.setInMeld(false);
		}
	}
}
