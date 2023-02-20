package jpabook.jpashop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping("/items/new")
	public String createForm(Model model) {
		model.addAttribute("form", new BookForm());
		return "items/createItemForm";
	}

	@PostMapping("/items/new")
	public String create(BookForm form) {
		Book book = Book.builder()
			.name(form.getName())
			.price(form.getPrice())
			.stockQuantity(form.getStockQuantity())
			.author(form.getAuthor())
			.isbn(form.getIsbn())
			.build();

		itemService.saveItem(book);

		return "redirect:/";
	}
}
