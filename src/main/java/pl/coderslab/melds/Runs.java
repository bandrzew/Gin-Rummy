package pl.coderslab.melds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.coderslab.entity.Card;

public class Runs implements Melds {
	@Override
	public void find(List<Card> cards) {
		Map<Integer, Integer> results = new HashMap<>();// <runStart card index, run length>
		int runStart = -1, shift = 1;
		this.reset(cards);

		for (int i = 0; i < cards.size() - 2; i++) {
			if (cards.get(i).getValue() + 1 == cards.get(i + 1).getValue()
					&& cards.get(i).getValue() + 2 == cards.get(i + 2).getValue()) {
				if (runStart > shift - 1 && runStart == i - shift) {
					results.replace(runStart, results.get(runStart) + shift);
					shift++;
				} else {
					shift = 1;
					runStart = i;
					results.put(i, 3);
				}
			}
		}

		for (int index : results.keySet()) {
			for (shift = 0; shift < results.get(index); shift++) {
				cards.get(index + shift).setInMeld(true);
			}
		}
	}

	private void reset(List<Card> cards) {
		for (Card card : cards) {
			card.setInMeld(false);
		}
	}
}
