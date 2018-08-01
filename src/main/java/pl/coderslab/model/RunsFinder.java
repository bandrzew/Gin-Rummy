package pl.coderslab.model;

import java.util.ArrayList;
import java.util.List;

import pl.coderslab.entity.Card;

public class RunsFinder implements MeldsFinder {

	private List<List<Card>> hand = new ArrayList<>();

	public RunsFinder(List<List<Card>> hand) {
		super();
		this.hand = hand;
	}

	@Override
	public void reset() {
		for (List<Card> cards : this.hand) {
			for (Card card : cards) {
				card.setInMeld(false);
			}
		}
	}

	@Override
	public void findAll() {
		for (List<Card> cards : this.hand) {
			this.find(cards);
		}
	}

	private void find(List<Card> cards) {
		for (int i = 0; i < cards.size() - 2; i++) {
			if (cards.get(i).getValue() + 1 == cards.get(i + 1).getValue()
					&& cards.get(i).getValue() + 2 == cards.get(i + 2).getValue()) {
				cards.get(i).setInMeld(true);
				cards.get(i + 1).setInMeld(true);
				cards.get(i + 2).setInMeld(true);
			}
		}
	}

}
