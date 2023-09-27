package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepositoryOld MemberRepositoryOld;

	@Test
	public void 회원가입() throws Exception {
		// given
		Member member = Member.builder()
			.username("kim")
			.build();

		// when
		Long savedId = memberService.join(member);

		// then
		assertEquals(member, MemberRepositoryOld.findOne(savedId));
	}

	@Test
	public void 중복_회원_예외() throws Exception {
		// given
		Member member = Member.builder()
			.username("kim")
			.build();

		Member duplicatedMember = Member.builder()
			.username("kim")
			.build();

		// when
		memberService.join(member);

		// then
		assertThrows(IllegalStateException.class, () -> memberService.join(duplicatedMember));
	}

}