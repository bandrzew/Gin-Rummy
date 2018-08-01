package pl.coderslab.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.coderslab.entity.Card;
import pl.coderslab.entity.CardHolder;
import pl.coderslab.model.MeldsFinder;
import pl.coderslab.model.RunsFinder;
import pl.coderslab.model.SetsFinder;
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
		if (cardRepository.findByCardHolderName("stockPile").size() > 2
				&& cardHolderRepository.findByName("player").countDeadwoodPoints() != 0) {
			return "main";
		} else {
			return "redirect/deal";
		}
	}

	@GetMapping("/bot")
	@Transactional
	public String botRandomMove() {
		Random rand = new Random();
		int n = rand.nextInt(10000);
		CardHolder bot = cardHolderRepository.findByName("bot");
		Card picked;

		if (n % 2 == 0) {
			picked = this.discardPile.remove(this.discardPile.size() - 1);
		} else {
			picked = cardRepository.findFirstByCardHolderName("stockPile");
		}

		Card passed = cardRepository.findByNameAndSort("bot", new Sort("points")).get(9);
		cardRepository.findOne(picked.getId()).setCardHolder(bot);
		passed.setCardHolder(cardHolderRepository.findByName("discardPile"));
		this.discardPile.add(passed);

		return "redirect:/main";
	}

	@GetMapping("/melds")
	@Transactional
	public String melds() {
		this.findMelds("player");
		return "redirect:/bot";
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

		return "redirect:/melds";
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

		return "redirect:/melds";
	}

	/*
	 * Setting up the deck
	 **********************************************************************************/

	@GetMapping("/deal")
	@Transactional
	public String dealCards() {
		List<Card> cards = cardRepository.findAll();

		for (Card card : cards) {
			card.setCardHolder(cardHolderRepository.findByName("stockPile"));
		}
		this.discardPile.clear();
		Set<Integer> dealingSet = new HashSet<>();
		Random rand = new Random();

		while (dealingSet.size() < 21) {
			int n = rand.nextInt(52);
			dealingSet.add(n);
		}

		List<Integer> dealt = new ArrayList<>(dealingSet);

		for (int i : dealt.subList(0, 10)) {
			cards.get(i).setCardHolder(cardHolderRepository.findByName("player"));
		}

		for (int i : dealt.subList(10, 20)) {
			cards.get(i).setCardHolder(cardHolderRepository.findByName("bot"));
		}

		Card top = cards.get(dealt.get(20));
		top.setCardHolder(cardHolderRepository.findByName("discardPile"));
		this.discardPile.add(top);

		return "redirect:/main";
	}

	@GetMapping("/cards")
	public String createCards() {
		List<Card> cards = new ArrayList<>();

		for (int i = 1; i < 14; i++) {
			for (String color : this.colors) {
				Card card = new Card();
				card.setValue(i);
				card.setColor(color);
				card.setInMeld(false);
				cards.add(card);
			}
		}

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

	/*
	 * Models Attributes
	 **********************************************************************************/

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

	/*
	 * Private methods
	 **********************************************************************************/

	@Transactional
	private void findMelds(String cardHolderName) {
		int deadwoodPoints;
		CardHolder cardHolder = cardHolderRepository.findByName(cardHolderName);

		this.findSetsFirst(cardHolderName);
		deadwoodPoints = cardHolder.countDeadwoodPoints();
		this.findRunsFirst(cardHolderName);

		if (deadwoodPoints >= cardHolder.countDeadwoodPoints()) {
			return;
		} else {
			this.findSetsFirst(cardHolderName);
		}
	}

	private void findRunsFirst(String cardHolderName) {
		MeldsFinder melds = makeRunsFinder(cardHolderName);
		melds.reset();
		melds.findAll();
		makeSetsFinder(cardHolderName).findAll();
	}

	private void findSetsFirst(String cardHolderName) {
		MeldsFinder melds = makeSetsFinder(cardHolderName);
		melds.reset();
		melds.findAll();
		makeRunsFinder(cardHolderName).findAll();
	}

	private MeldsFinder makeRunsFinder(String cardHolderName) {
		List<List<Card>> hand = new ArrayList<>();
		for (String color : this.colors) {
			hand.add(cardRepository.findByColorToRun(cardHolderName, color, new Sort("value")));
		}
		return new RunsFinder(hand);
	}

	private MeldsFinder makeSetsFinder(String cardHolderName) {
		List<List<Card>> hand = new ArrayList<>();
		for (int i = 1; i < 14; i++) {
			hand.add(cardRepository.findByValueToSet(cardHolderName, i));
		}
		return new SetsFinder(hand);
	}

}
