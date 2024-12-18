package algo_arena.member.repository;

import algo_arena.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //검색어 포함(대소문자 무시) 닉네임 모두 검색
    @Query("select m from Member m where lower(m.nickname) like lower(concat('%', :keyword, '%'))")
    List<Member> findByNickname(@Param("keyword") String keyword);
}