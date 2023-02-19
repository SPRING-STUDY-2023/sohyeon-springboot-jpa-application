package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class OrderSearch  {

	private String memberName;
	private OrderStatus orderStatus;
}
