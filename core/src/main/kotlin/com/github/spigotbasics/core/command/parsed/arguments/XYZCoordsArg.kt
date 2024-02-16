package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.common.leftOrNull
import com.github.spigotbasics.common.rightOrNull
import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.model.XYZCoords
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

// TODO: Use Player's coordinates as base for relative coordinates when console is using relative coordinates
//  Maybe always use the teleportee's coordinates as base for relative coordinates?
//  This would also make the command more consistent, as it would always use the same base for relative coordinates
//  This would however be a breaking change, but it would make more sense
class XYZCoordsArg(name: String) : CommandArgument<XYZCoords>(name) {
    private enum class ErrorType {
        INVALID_COORDS,
        CANT_USE_RELATIVE_COORDS_AS_CONSOLE,
        NOT_ENOUGH_ARGUMENTS, // This shouldn't be possible, as the ArgumentPath would already check it
    }

    override fun parse(
        sender: CommandSender,
        value: String,
    ): XYZCoords? {
        val result = parseCoords(sender, value)
        return result.rightOrNull()
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        val result = parseCoords(sender, value)
        return when (result) {
            is Either.Left ->
                when (result.value) {
                    ErrorType.INVALID_COORDS -> super.errorMessage(sender, value)
                    ErrorType.CANT_USE_RELATIVE_COORDS_AS_CONSOLE -> Basics.messages.cantUseRelativeCoordsFromConsole
                    ErrorType.NOT_ENOUGH_ARGUMENTS -> error("This should never happen")
                }
            is Either.Right -> throw IllegalStateException("This should never happen")
        }
    }

    private fun parseCoords(
        sender: CommandSender,
        value: String,
    ): Either<ErrorType, XYZCoords> {
        val split = value.split(" ")
        if (split.size != 3) return Either.Left(ErrorType.NOT_ENOUGH_ARGUMENTS)
        val x = parseCoord(sender, split[0], Location::getX)
        val y = parseCoord(sender, split[1], Location::getY)
        val z = parseCoord(sender, split[2], Location::getZ)
        val firstError = x.leftOrNull() ?: y.leftOrNull() ?: z.leftOrNull()
        if (firstError != null) {
            return Either.Left(firstError)
        }
        return Either.Right(XYZCoords(x.rightOrNull()!!, y.rightOrNull()!!, z.rightOrNull()!!))
    }

    override val length = 3

    private fun parseCoord(
        sender: CommandSender,
        input: String,
        coordinateProvider: (Location) -> Double,
    ): Either<ErrorType, Double> {
        val relative = input.startsWith("~")
        if (relative && sender !is Player) {
            return Either.Left(ErrorType.CANT_USE_RELATIVE_COORDS_AS_CONSOLE)
        }
        if (relative) {
            val player = sender as Player
            val originalValue = coordinateProvider(player.location)
            val givenOffset = input.substring(1).let { if (it.isEmpty()) 0.toDouble() else it.toDoubleOrNull() }
            if (givenOffset == null) {
                return Either.Left(ErrorType.INVALID_COORDS)
            }
            return Either.Right(originalValue + givenOffset)
        } else {
            val double = input.toDoubleOrNull()
            return if (double == null) {
                Either.Left(ErrorType.INVALID_COORDS)
            } else {
                Either.Right(double)
            }
        }
    }
}
