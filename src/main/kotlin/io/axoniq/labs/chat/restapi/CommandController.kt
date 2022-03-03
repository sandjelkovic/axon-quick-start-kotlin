package io.axoniq.labs.chat.restapi

import io.axoniq.labs.chat.coreapi.CreateRoomCommand
import io.axoniq.labs.chat.coreapi.JoinRoomCommand
import io.axoniq.labs.chat.coreapi.LeaveRoomCommand
import io.axoniq.labs.chat.coreapi.PostMessageCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.Future

@RestController
class CommandController(private val commandGateway: CommandGateway) {
    @PostMapping("/rooms")
    fun createChatRoom(@RequestBody room: Room): Future<String?>? {
        requireNotNull(room.name) { "name is mandatory for a chatroom" }
        return commandGateway.send(CreateRoomCommand(room.roomId ?: UUID.randomUUID().toString(), room.name))
    }

    @PostMapping("/rooms/{roomId}/participants")
    fun joinChatRoom(@PathVariable roomId: String, @RequestBody participant: Participant): Future<Void> {
        requireNotNull(participant.name) { "name is mandatory for a chatroom" }
        return commandGateway.send(JoinRoomCommand(roomId, participant.name))
    }

    @PostMapping("/rooms/{roomId}/messages")
    fun postMessage(@PathVariable roomId: String, @RequestBody message: Message): Future<Void> {
        requireNotNull(message.name) { "'name' missing - please provide a participant name" }
        requireNotNull(message.message) { "'message' missing - please provide a message to post" }
        return commandGateway.send(PostMessageCommand(roomId, message.name, message.message))
    }

    @DeleteMapping("/rooms/{roomId}/participants")
    fun leaveChatRoom(@PathVariable roomId: String, @RequestBody participant: Participant): Future<Void?>? {
        requireNotNull(participant.name) { "name is mandatory for a chatroom" }
        return commandGateway.send(LeaveRoomCommand(roomId, participant.name))
    }

    data class Message(
        val name: String? = null,
        val message: String? = null
    )

    class Participant(
        val name: String? = null
    )

    class Room(
        val roomId: String? = null,
        val name: String? = null
    )
}
