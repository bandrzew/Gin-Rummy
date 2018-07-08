package pl.coderslab.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.coderslab.entity.Card;
import pl.coderslab.entity.CardHolder;
import pl.coderslab.repository.CardHolderRepository;
import pl.coderslab.repository.CardRepository;

@Controller
public class DeckController {
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private CardHolderRepository cardHolderRepository;

	@GetMapping("/test")
	public String test() {
		return "main";
	}

	@GetMapping("/discard/{card}")
	@ResponseBody
	public String discard(@PathVariable String card) {
		String visibleValue;
		String color;
		if (card.length() == 2) {

		}
		return cardRepository.toString();
	}

	@GetMapping("/stock/{card}")
	@ResponseBody
	public String stock(@PathVariable String card) {
		return cardRepository.toString();
	}

	@GetMapping("/deal")
	@Transactional
	public String dealCards() {
		List<Card> cards = cardRepository.findAll();

		for (Card card : cards.subList(0, 10)) {
			card.setCardHolder(cardHolderRepository.findByName("player"));
		}

		for (Card card : cards.subList(10, 20)) {
			card.setCardHolder(cardHolderRepository.findByName("bot"));
		}

		cards.get(20).setCardHolder(cardHolderRepository.findByName("discardPile"));

		return "redirect:/test";
	}

	@GetMapping("/cards")
	@Transactional
	public String createCards() {
		String[] colors = { "D", "C", "H", "S" };// { "diams", "clubs", "hearts", "spades" }
		List<Card> cards = new ArrayList<>();
		for (String color : colors) {
			for (int i = 1; i < 14; i++) {
				Card card = new Card();
				card.setValue(i);
				card.setColor(color);
				card.setCardHolder(cardHolderRepository.findByName("stockPile"));
				cards.add(card);
			}
		}
		Collections.shuffle(cards);
		cardRepository.save(cards);
		return "redirect:/deal";
	}

	@GetMapping("/init")
	public String createHolders() {
		CardHolder stockPile = new CardHolder();
		stockPile.setName("stockPile");
		cardHolderRepository.save(stockPile);

		CardHolder player = new CardHolder();
		player.setName("player");
		cardHolderRepository.save(player);

		CardHolder bot = new CardHolder();
		bot.setName("bot");
		cardHolderRepository.save(bot);

		CardHolder discardPile = new CardHolder();
		discardPile.setName("discardPile");
		cardHolderRepository.save(discardPile);

		return "redirect:/cards";
	}

	@ModelAttribute("hand")
	public List<Card> getPlayerCards() {
		if (cardHolderRepository.count() == 0) {
			return null;
		}
		return cardHolderRepository.findByName("player").getCards();
	}

	@ModelAttribute("discard")
	public Card getDiscardPile() {
		List<Card> discardPile = cardRepository.findByCardHolderName("discardPile");
		if (discardPile.size() == 0) {
			return null;
		}
		return discardPile.get(discardPile.size() - 1);
	}

}
