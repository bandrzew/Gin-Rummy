package pl.coderslab.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.coderslab.entity.Card;
import pl.coderslab.entity.CardHolder;
import pl.coderslab.melds.Runs;
import pl.coderslab.repository.CardHolderRepository;
import pl.coderslab.repository.CardRepository;

@Controller
public class DeckController {
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private CardHolderRepository cardHolderRepository;

	private List<Card> discardPile = new ArrayList<>();

	private String[] colors = { "D", "C", "H", "S" };// { "diams", "clubs", "hearts", "spades" }

	@GetMapping("/main")
	public String main() {
		return "main";
	}

	@GetMapping("/bot")
	@Transactional
	@ResponseBody
	public String botRandomMove() {
		Random rand = new Random();
		int n = rand.nextInt(10000);
		Card picked = null;

		if (n % 2 == 0) {
			picked = this.discardPile.remove(this.discardPile.size() - 1);
		} else {
			picked = cardRepository.findFirstByCardHolderName("stockPile");
		}

		Card passed = cardRepository.findByNameAndSort("bot", new Sort("points")).get(n % 10);
		picked.setCardHolder(cardHolderRepository.findByName("bot"));
		passed.setCardHolder(cardHolderRepository.findByName("discardPile"));
		this.discardPile.add(passed);

		return "redirect:/main";
	}

	@GetMapping("/runs")
	@ResponseBody
	public String findRuns() {
		for (String color : this.colors) {
			Runs runs = new Runs();
			runs.find(cardRepository.findByNameAndColorWithSort("player", color, new Sort("value")));
		}
		return "redirect:/main";
	}

	@GetMapping("/discard/{card}")
	@Transactional
	public String discard(@PathVariable String card) {
		String visibleValue = card.substring(0, card.length() - 1);
		String color = card.substring(card.length() - 1);

		Card picked = this.discardPile.remove(this.discardPile.size() - 1);
		Card passed = cardRepository.findByVisibleValueAndColor(visibleValue, color);

		cardRepository.findOne(picked.getId()).setCardHolder(cardHolderRepository.findByName("player"));
		passed.setCardHolder(cardHolderRepository.findByName("discardPile"));
		this.discardPile.add(passed);

		return "redirect:/bot";
	}

	@GetMapping("/stock/{card}")
	@Transactional
	public String stock(@PathVariable String card) {
		String visibleValue = card.substring(0, card.length() - 1);
		String color = card.substring(card.length() - 1);

		Card picked = cardRepository.findFirstByCardHolderName("stockPile");
		Card passed = cardRepository.findByVisibleValueAndColor(visibleValue, color);

		picked.setCardHolder(cardHolderRepository.findByName("player"));
		passed.setCardHolder(cardHolderRepository.findByName("discardPile"));
		this.discardPile.add(passed);

		return "redirect:/bot";
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
		discardPile.add(cards.get(20));

		return "redirect:/main";
	}

	@GetMapping("/reset")
	public String reset() {
		cardRepository.deleteAll();
		return "redirect:/cards";
	}

	@GetMapping("/cards")
	@Transactional
	public String createCards() {
		List<Card> cards = new ArrayList<>();

		for (String color : this.colors) {
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
		if (this.discardPile.size() == 0) {
			return null;
		}
		return cardRepository.findByNameAndSort("player", new Sort("value"));
	}

	@ModelAttribute("discard")
	public Card getTopDiscard() {
		if (this.discardPile.size() == 0) {
			return null;
		}
		return this.discardPile.get(this.discardPile.size() - 1);
	}

}
