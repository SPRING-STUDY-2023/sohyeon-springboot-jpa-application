package jpabook.jpashop.repository;

import static jpabook.jpashop.domain.QMember.*;
import static jpabook.jpashop.domain.QOrder.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager em;
	private final JPAQueryFactory query;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long orderId) {
		return em.find(Order.class, orderId);
	}

	public List<Order> findAllByString(OrderSearch orderSearch) {
		//language=JPAQL

		String jpql = "select o From Order o join o.member m";
		boolean isFirstCondition = true;
		//주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}
		//회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.username like :name";
		}

		TypedQuery<Order> query = em.createQuery(jpql, Order.class) .setMaxResults(1000); //최대 1000건
		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		return query.getResultList();
	}

	public List<Order> findAll(OrderSearch orderSearch) {
		return query
			.select(order)
			.from(order)
			.join(order.member, member)
			.where(statsEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
			.limit(1000)
			.fetch();
	}

	private BooleanExpression nameLike(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return member.username.like(name);
	}

	private BooleanExpression statsEq(OrderStatus statusCond) {
		if (statusCond == null) {
			return null;
		}
		return order.status.eq(statusCond);
	}

	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery(
			"select o from Order o" +
				" join fetch o.member m" +
				" join fetch o.delivery d", Order.class
		).getResultList();
	}

	public List<Order> findAllWithItem() {
		return em.createQuery(
			"select distinct o from Order o" +
				" join fetch o.member m" +
				" join fetch o.delivery d" +
				" join fetch o.orderItems oi" +
				" join fetch oi.item i", Order.class
		).getResultList();
	}

	public List<Order> findAllWithMemberDelivery(int offset, int limit) {
		return em.createQuery(
			"select o from Order o" +
				" join fetch o.member m" +
				" join fetch o.delivery d", Order.class)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}
}
