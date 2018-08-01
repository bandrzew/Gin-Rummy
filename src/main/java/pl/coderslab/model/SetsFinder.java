package pl.coderslab.model;

import java.util.ArrayList;
import java.util.List;

import pl.coderslab.entity.Card;

public class SetsFinder implements MeldsFinder {

	private List<List<Card>> hand = new ArrayList<>();

	public SetsFinder(List<List<Card>> hand) {
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
		if (cards != null && cards.size() > 2) {
			for (Card card : cards) {
				card.setInMeld(true);
			}
		}
	}

}
