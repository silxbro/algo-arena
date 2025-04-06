package algo_arena.domain.member.repository;

import algo_arena.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //검색어 포함(대소문자 무시) 이름 모두 검색
    @Query("select m from Member m where lower(m.name) like lower(concat('%', :name, '%'))")
    List<Member> findAllByName(@Param("name") String name);

    Optional<Member> findByName(String name);

    Optional<Member> findByEmail(String email);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);
}