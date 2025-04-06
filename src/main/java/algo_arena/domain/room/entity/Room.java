package algo_arena.domain.room.entity;

import algo_arena.common.entity.BaseEntity;
import algo_arena.domain.member.entity.Member;
import algo_arena.domain.problem.entity.Problem;
import algo_arena.domain.submission.enums.CodeLanguage;
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
import java.util.Collection;
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

    private String name;

    private Integer maxRoomMembers;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomProblem> roomProblems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMember> roomMembers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CodeLanguage language;

    private Integer timeLimit; //분 단위

    @TimeToLive
    @Builder.Default
    private Long timeToLive = 60L * 60L;

    public void update(Room updateInfo) {
        this.maxRoomMembers = updateInfo.getMaxRoomMembers();
        this.language = updateInfo.getLanguage();
        this.timeLimit = updateInfo.getTimeLimit();
    }

    public void setProblems(Collection<Problem> problems) {
        List<RoomProblem> newRoomProblems = problems.stream()
            .map(problem -> RoomProblem.from(this, problem))
            .toList();

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

    public void exit(String memberName) {
        for (RoomMember roomMember : roomMembers) {
            Member member = roomMember.getMember();
            if (member.matchesName(memberName)) {
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

    public boolean isHost(String name) {
        return host.getName().equals(name);
    }

    public boolean isMember(String name) {
        return isHost(name) || roomMembers.stream()
            .map(roomMember -> roomMember.getMember().getName())
            .anyMatch(memberName -> memberName.equals(name));
    }
}