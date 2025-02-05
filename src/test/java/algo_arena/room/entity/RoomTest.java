package algo_arena.room.entity;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.member.entity.Member;
import algo_arena.problem.entity.Problem;
import algo_arena.submission.enums.Language;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomTest {

    Room room;

    Problem problem1, problem2;

    Member host;

    @BeforeEach
    void init() {
        problem1 = createProblem(1L);
        problem2 = createProblem(2L);
        host = createMember(1L, "host");
        room = Room.builder()
            .name("코딩테스트방")
            .maxRoomMembers(3)
            .roomProblems(getRoomProblems(room, problem1, problem2))
            .host(host)
            .language(Language.KOTLIN)
            .timeLimit(60)
            .build();
    }

    @Test
    @DisplayName("테스트방 정보를 변경할 수 있다")
    void update() {
        //given
        Room updateInfo = Room.builder()
            .maxRoomMembers(20)
            .language(Language.C_PP)
            .timeLimit(30)
            .build();

        //when
        room.update(updateInfo);

        //then
        assertThat(room.getMaxRoomMembers()).isEqualTo(20);
        assertThat(room.getLanguage()).isEqualTo(Language.C_PP);
        assertThat(room.getTimeLimit()).isEqualTo(30);
    }

    @Test
    @DisplayName("테스트방의 문제를 변경할 수 있다")
    void setProblems() {
        //given: 1,2번 -> 1,3번 문제 변경
        Problem problem3 = createProblem(3L);
        List<Problem> problems = new ArrayList<>();
        problems.add(problem1); // 기존 유지
        problems.add(problem3); // 새로 추가

        //when
        room.setProblems(problems);
        List<Problem> resultProblems = room.getRoomProblems().stream()
            .map(RoomProblem::getProblem)
            .toList();

        //then
        assertThat(resultProblems.size()).isEqualTo(2);
        assertThat(resultProblems).contains(problem1);
        assertThat(resultProblems).contains(problem3);
    }

    @Test
    @DisplayName("참가자 중 가장 먼저 입장한 사람이 자동으로 방장으로 변경된다")
    void changeHost() {
        //given
        room.enter(createMember(2L, "second"));
        room.enter(createMember(3L, "third"));
        room.enter(createMember(4L, "fourth"));

        //when
        room.changeHost();

        //then
        assertThat(room.getHost().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("테스트방에 참가자가 입장할 수 있다")
    void enter() {
        //given
        Member member1 = createMember(100L, "member1");
        Member member2 = createMember(200L, "member2");

        //when
        room.enter(member1);
        room.enter(member2);

        List<Member> members = room.getRoomMembers().stream()
            .map(RoomMember::getMember).toList();

        //then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members).containsExactly(member1, member2);
    }

    @Test
    @DisplayName("테스트방에서 참가자가 퇴장할 수 있다")
    void exit() {
        //given
        Member member1 = createMember(100L, "member1");
        Member member2 = createMember(200L, "member2");
        room.enter(member1);
        room.enter(member2);

        //when
        room.exit("member2");

        List<Member> members = room.getRoomMembers().stream()
            .map(RoomMember::getMember).toList();

        //then
        assertThat(members.size()).isEqualTo(1);
        assertThat(members).containsExactly(member1);
    }

    @Test
    @DisplayName("테스트방에 참가자가 없을 경우, 이를 조회할 수 있다")
    void existMembers_false() {
        //given

        //when
        boolean result = room.existMembers();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("테스트방에 참가자가 존재할 경우, 이를 조회할 수 있다")
    void existMembers_true() {
        //given
        room.enter(createMember(2L, "member"));

        //when
        boolean result = room.existMembers();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("테스트방에 참가자가 최대 정원보다 적은 경우, 이를 조회할 수 있다")
    void isFull_no() {
        //given
        room.enter(createMember(2L, "member"));

        //when
        boolean result = room.isFull();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("테스트방에 참가자가 최대 정원인 경우, 이를 조회할 수 있다")
    void isFull_yes() {
        //given
        room.enter(createMember(2L, "second"));
        room.enter(createMember(3L, "third"));
        room.enter(createMember(4L, "fourth"));

        //when
        boolean result = room.isFull();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("회원 이름으로 테스트방의 방장여부를 조회할 수 있다")
    void isHost() {
        //given

        //when
        boolean result = room.isHost(host.getName());

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("회원 이름으로 테스트방의 멤버 여부를 조회할 수 있다")
    void isMember_notHost() {
        //given
        String memberName = "member";
        room.enter(createMember(2L, memberName));

        //when
        boolean result1 = room.isMember(memberName);
        boolean result2 = room.isMember("wrongName");

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("호스트의 경우도 테스트방의 멤버로 간주한다")
    void isMember_host() {
        //given

        //when
        boolean result = room.isMember(host.getName());

        //then
        assertThat(result).isTrue();
    }

    private Member createMember(Long memberId, String memberName) {
        return Member.builder().id(memberId).name(memberName).build();
    }

    private Problem createProblem(Long problemNumber) {
        return Problem.builder().number(problemNumber).build();
    }

    private List<RoomProblem> getRoomProblems(Room room, Problem... problems) {
        return Arrays.stream(problems)
            .map(problem -> RoomProblem.from(room, problem))
            .collect(Collectors.toList());
    }
}