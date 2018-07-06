package pl.coderslab.controllers;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
	@ResponseBody
	public String test() {
		CardHolder player = cardHolderRepository.findOne(1l);
		List<Card> dealt = cardRepository.findByCardHolder(player);

		return dealt.get(0).getCardHolder().toString() + "<br>" + cardHolderRepository.findOne(2l).toString();
	}

	@GetMapping("/deal")
	@Transactional
	public String deal() {
		List<Card> shuffled = cardRepository.findAll();
		Collections.shuffle(shuffled);
		// shuffled = shuffled.subList(0, 10);
		// Hibernate.initialize(player.getCards());

		for (Card card : shuffled.subList(0, 10)) {
			card.setCardHolder(cardHolderRepository.findByName("player"));
		}

		for (Card card : shuffled.subList(10, 20)) {
			card.setCardHolder(cardHolderRepository.findByName("BOT"));
		}

		return "redirect:/test";
	}

	@GetMapping("/init")
	public String createCards() {
		String[] colors = { "D", "C", "H", "S" };// { "diams", "clubs", "hearts", "spades" }
		for (String color : colors) {
			for (int i = 1; i < 14; i++) {
				Card card = new Card();
				card.setValue(i);
				card.setColor(color);
				cardRepository.save(card);
			}
		}
		return "redirect:/createHolders";
	}

	@GetMapping("/createHolders")
	public String createHolders() {

		CardHolder player = new CardHolder();
		player.setName("player");
		cardHolderRepository.save(player);
		CardHolder bot = new CardHolder();
		bot.setName("bot");
		cardHolderRepository.save(bot);

		return "redirect:/deal";
	}
}
