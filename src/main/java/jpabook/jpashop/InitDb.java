package jpabook.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		initService.dbInit1();
		initService.dbInit2();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {
		private final EntityManager em;
		public void dbInit1() {
			Member member = new Member();
			member.setUsername("userA");
			member.setAddress(new Address("서울", "관악구", "06969"));
			em.persist(member);

			Book book1 = new Book();
			book1.setName("JPA BOOK1");
			book1.setPrice(10000);
			book1.setStockQuantity(100);
			em.persist(book1);

			Book book2 = new Book();
			book2.setName("JPA BOOK2");
			book2.setPrice(20000);
			book2.setStockQuantity(100);
			em.persist(book2);

			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}

		public void dbInit2() {
			Member member = new Member();
			member.setUsername("userB");
			member.setAddress(new Address("진주", "관악구", "06969"));
			em.persist(member);

			Book book1 = new Book();
			book1.setName("JPA BOOK1");
			book1.setPrice(10000);
			book1.setStockQuantity(100);
			em.persist(book1);

			Book book2 = new Book();
			book2.setName("JPA BOOK2");
			book2.setPrice(20000);
			book2.setStockQuantity(100);
			em.persist(book2);

			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}

	}
}
