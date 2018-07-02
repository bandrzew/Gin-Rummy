package pl.coderslab.controllers;

import java.util.Collections;
import java.util.List;

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
		CardHolder player = new CardHolder();
		player.setName("player");
		List<Card> shuffled = cardRepository.findAll();
		Collections.shuffle(shuffled);
		shuffled = shuffled.subList(0, 10);
		for (Card card : shuffled) {
			card.setCardHolder(player);
			player.getCards().add(card);
		}
		cardHolderRepository.save(player);
		return cardHolderRepository.findAll().toString();
	}

	@GetMapping("/create")
	public String createCards() {
		String[] colors = { "D", "C", "H", "S" };// { "diams", "clubs", "hearts", "spades" }
		if (cardRepository.count() == 0) {
			for (String color : colors) {
				for (int i = 1; i < 14; i++) {
					Card card = new Card();
					card.setValue(i);
					card.setColor(color);
					cardRepository.save(card);
				}
			}
		}
		return "redirect:/test";
	}
}
