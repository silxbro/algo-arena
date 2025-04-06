package algo_arena.domain.chat.template;

import algo_arena.domain.room.entity.Room;

@FunctionalInterface
public interface MessageTemplate {

    String getMessage(Room room, String memberName);

}