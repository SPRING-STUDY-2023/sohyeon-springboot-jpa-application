package jpabook.jpashop.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	/**
	 * 회원 가입
	 */
	@Transactional
	public Long join(Member member) {
		validateDuplicateMember(member); // 중복 회원 검증
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplicateMember(Member member) {
		List<Member> foundMembers = memberRepository.findByUsername(member.getUsername());
		if (!foundMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	/**
	 * 회원 전체 조회
	 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	/**
	 * 회원 단건 조회
	 */
	public Member findOne(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원"));
	}

	/**
	 * 회원 수정
	 */
	@Transactional
	public void update(Long id, String name) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원"));
		member.setUsername(name);
	}
}
