package pl.coderslab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.coderslab.entity.Card;
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
		String[] colors = { "D", "C", "H", "S" };// { "diams", "clubs", "hearts", "spades" }
		for (String color : colors) {
			for (int i = 1; i < 14; i++) {
				cardRepository.save(new Card(i, color));
			}
		}
		return cardRepository.findAll().toString();
	}
}
