package algo_arena.room.entity;

import algo_arena.common.entity.BaseEntity;
import algo_arena.member.entity.Member;
import algo_arena.problem.entity.Problem;
import algo_arena.submission.entity.Language;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper=false)
public class Room extends BaseEntity implements Serializable {

    @Id
    @Column(name = "room_id")
    @EqualsAndHashCode.Include
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Builder.Default
    private String name = "이름 없음";

    private Integer maxRoomMembers;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomProblem> roomProblems = new ArrayList<>();

    @Builder.Default
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member host = Member.builder().build();

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMember> roomMembers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Language language;

    private Integer timeLimit; //분 단위

    @TimeToLive
    @Builder.Default
    private Long timeToLive = 60L * 60L;

    public void update(Room updateInfo) {
        this.name = updateInfo.getName();
        this.maxRoomMembers = updateInfo.getMaxRoomMembers();
        this.roomProblems = updateInfo.getRoomProblems();
        this.language = updateInfo.getLanguage();
        this.timeLimit = updateInfo.getTimeLimit();
    }

    public void initHost(Member host) {
        this.host = host;
    }
    public void initProblems(List<Problem> problems) {
        this.roomProblems = problems.stream()
            .map(problem -> RoomProblem.from(this, problem))
            .toList();
    }

    public Member changeHost() {
        RoomMember firstEntered = roomMembers.remove(0);
        host = firstEntered.getMember();
        return host;
    }

    public boolean addMember(Member member) {
        //TODO: 이미 소속된 테스트방이 존재하는 경우: 예외처리 / 불가능한 경우인데, 예외처리가 필요할까?
        if (isFull()) {
            return false;
        }
        roomMembers.add(RoomMember.from(this, member));
        return true;
    }

    public Member removeMember(Long memberId) {
        for (RoomMember roomMember : roomMembers) {
            Member member = roomMember.getMember();
            if (member.equalsId(memberId)) {
                roomMembers.remove(roomMember);
                return member;
            }
        }
        return null;
    }

    public boolean existMembers() {
        return !roomMembers.isEmpty();
    }

    public boolean isFull() {
        return roomMembers.size() == maxRoomMembers;
    }

    public boolean isHost(Long memberId) {
        return host.equalsId(memberId);
    }

    public boolean isMember(Long memberId) {
        return roomMembers.stream()
            .anyMatch(roomMember -> roomMember.getMember().equalsId(memberId));
    }
}