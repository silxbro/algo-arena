package algo_arena.domain.room.repository;

import static algo_arena.domain.room.entity.QRoom.room;

import algo_arena.domain.room.dto.request.RoomSearchRequest;
import algo_arena.domain.room.entity.Room;
import algo_arena.domain.submission.enums.CodeLanguage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.util.StringUtils;

public class RoomRepositoryImpl implements RoomQueryRepository {

    private final JPAQueryFactory query;

    public RoomRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Room> findRoomsBySearch(RoomSearchRequest request) {
        String roomName = request.getRoomName();
        Integer maxRoomMembers = request.getMaxRoomMembers();
        String languageName = request.getLanguageName();
        Integer minProblems = request.getMinProblems();
        Integer maxProblems = request.getMaxProblems();
        Integer minTimeLimit = request.getMinTimeLimit();
        Integer maxTimeLimit = request.getMaxTimeLimit();

        return query
            .selectFrom(room)
            .where(
                roomNameContains(roomName),
                maxRoomMembers(maxRoomMembers),
                languageEq(languageName),
                problemsBetween(minProblems, maxProblems),
                timeLimitBetween(minTimeLimit, maxTimeLimit)
            )
            .fetch();
    }

    private BooleanExpression roomNameContains(String roomName) {
        if (!StringUtils.hasText(roomName)) {
            return null;
        }
        return room.name.toUpperCase().contains(roomName.toUpperCase());
    }

    private BooleanExpression maxRoomMembers(Integer maxRoomMembers) {
        if (maxRoomMembers == null) {
            return null;
        }
        return room.maxRoomMembers.loe(maxRoomMembers);
    }

    private BooleanExpression languageEq(String languageName) {
        CodeLanguage language = CodeLanguage.fromName(languageName);
        if (language == null) {
            return null;
        }
        return room.language.eq(language);
    }

    private BooleanExpression problemsBetween(Integer minProblems, Integer maxProblems) {
        if (minProblems == null && maxProblems == null) {
            return null;
        }
        if (maxProblems == null) {
            return room.roomProblems.size().goe(minProblems);
        }
        if (minProblems == null) {
            return room.roomProblems.size().loe(maxProblems);
        }
        return room.roomProblems.size().between(minProblems, maxProblems);
    }

    private BooleanExpression timeLimitBetween(Integer minTimeLimit, Integer maxTimeLimit) {
        if (minTimeLimit == null && maxTimeLimit == null) {
            return null;
        }
        if (maxTimeLimit == null) {
            return room.timeLimit.goe(minTimeLimit);
        }
        if (minTimeLimit == null) {
            return room.timeLimit.loe(maxTimeLimit);
        }
        return room.timeLimit.between(minTimeLimit, maxTimeLimit);
    }
}
