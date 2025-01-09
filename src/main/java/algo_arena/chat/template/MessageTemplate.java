package algo_arena.chat.template;

import algo_arena.room.entity.Room;

@FunctionalInterface
public interface MessageTemplate {

    String getMessage(Room room, String memberName);

}