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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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

    private String name;

    private Integer maxRoomMembers;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomProblem> roomProblems = new ArrayList<>();

    @Builder.Default
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
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
        this.maxRoomMembers = updateInfo.getMaxRoomMembers();
        this.language = updateInfo.getLanguage();
        this.timeLimit = updateInfo.getTimeLimit();
    }

    public void setProblems(List<Problem> problems) {
        Set<RoomProblem> newRoomProblems = problems.stream()
            .map(problem -> RoomProblem.from(this, problem))
            .collect(Collectors.toSet());

        roomProblems.removeIf(existing -> !newRoomProblems.contains(existing));

        newRoomProblems.forEach(newItem -> {
            if (!roomProblems.contains(newItem)) {
                roomProblems.add(newItem);
            }
        });
    }

    public void changeHost() {
        RoomMember front = roomMembers.remove(0);
        host = front.getMember();
    }

    public void enter(Member member) {
        roomMembers.add(RoomMember.from(this, member));
    }

    public void exit(Long memberId) {
        for (RoomMember roomMember : roomMembers) {
            Member member = roomMember.getMember();
            if (member.equalsId(memberId)) {
                roomMembers.remove(roomMember);
                return;
            }
        }
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