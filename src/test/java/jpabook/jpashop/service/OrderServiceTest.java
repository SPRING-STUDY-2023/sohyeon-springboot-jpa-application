package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughtStockException;
import jpabook.jpashop.repository.OrderRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	EntityManager em;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;

	@Test
	public void 상품주문() throws Exception {
		// given
		Member member = createMember();

		int stockQuantity = 10;
		Item book = createBook(stockQuantity);

		int orderCount = 2;

		// when
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// then
		Order foundOrder = orderRepository.findOne(orderId);

		assertEquals(OrderStatus.ORDER, foundOrder.getStatus());
		assertEquals(1, foundOrder.getOrderItems().size());
		assertEquals(book.getPrice() * orderCount, foundOrder.getTotalPrice());
		assertEquals(stockQuantity - orderCount, book.getStockQuantity());
	}

	@Test
	public void 상품주문_재고수량초과() throws Exception {
		// given
		Member member = createMember();

		int stockQuantity = 10;
		Item book = createBook(stockQuantity);

		int orderCount = 11;

		// when
		// then
		assertThrows(NotEnoughtStockException.class, () ->
			orderService.order(member.getId(), book.getId(), orderCount));
	}

	@Test
	public void 주문취소() throws Exception {
		// given
		Member member = createMember();

		int stockQuantity = 10;
		Item book = createBook(stockQuantity);

		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// when
		orderService.cancelOrder(orderId);

		// then
		Order foundOrder = orderRepository.findOne(orderId);

		assertEquals(OrderStatus.CANCEL, foundOrder.getStatus());
		assertEquals(stockQuantity, book.getStockQuantity());
	}

	private Item createBook(int stockQuantity) {
		Item book = Book.builder()
			.name("시골 JPA")
			.price(10000)
			.stockQuantity(stockQuantity)
			.author("김작가")
			.isbn("111 - 111")
			.build();
		em.persist(book);
		return book;
	}

	private Member createMember() {
		Member member = Member.builder()
			.username("회원1")
			.address(new Address("서울", "상도로", "123-123"))
			.build();
		em.persist(member);
		return member;
	}
}