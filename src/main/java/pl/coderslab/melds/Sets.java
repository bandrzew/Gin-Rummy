package pl.coderslab.melds;

import java.util.List;

import pl.coderslab.entity.Card;

public class Sets implements Melds {

	@Override
	public void find(List<Card> cards) {
		if (cards != null && cards.size() > 2) {
			for (Card card : cards) {
				card.setInMeld(true);
			}
		}
	}

}
