package pl.coderslab.melds;

import java.util.List;

import pl.coderslab.entity.Card;

public class Runs implements Melds {

	@Override
	public void find(List<Card> cards) {
		this.reset(cards);

		for (int i = 0; i < cards.size() - 2; i++) {
			if (cards.get(i).getValue() + 1 == cards.get(i + 1).getValue()
					&& cards.get(i).getValue() + 2 == cards.get(i + 2).getValue()) {
				cards.get(i).setInMeld(true);
				cards.get(i + 1).setInMeld(true);
				cards.get(i + 2).setInMeld(true);
			}
		}

	}

	private void reset(List<Card> cards) {
		for (Card card : cards) {
			card.setInMeld(false);
		}
	}
}
