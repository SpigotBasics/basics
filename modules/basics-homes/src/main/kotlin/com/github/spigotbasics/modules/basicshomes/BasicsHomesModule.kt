package com.github.spigotbasics.modules.basicshomes

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.Serialization
import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import com.github.spigotbasics.core.storage.NamespacedStorage
import com.github.spigotbasics.modules.basicshomes.commands.DelHomeCommand
import com.github.spigotbasics.modules.basicshomes.commands.HomeCommand
import com.github.spigotbasics.modules.basicshomes.commands.SetHomeCommand
import com.github.spigotbasics.modules.basicshomes.data.Home
import com.github.spigotbasics.modules.basicshomes.data.HomeList
import com.google.gson.TypeAdapter
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

class BasicsHomesModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    private var storage: NamespacedStorage? = null
    private val homes = mutableMapOf<UUID, HomeList>()
    private val messages = getConfig(ConfigName.MESSAGES)

    val permissionHome = permissionManager.createSimplePermission("basics.home", "Allows to access the /home command")

    val permissionSetHome = permissionManager.createSimplePermission("basics.sethome", "Allows to access the /sethome command")
    val permissionSetHomeMultiple = permissionManager.createSimplePermission("basics.sethome.multiple", "Allows to set multiple named homes")
    val permissionSetHomeUnlimited = permissionManager.createSimplePermission("basics.sethome.multiple.unlimited", "Allows to set unlimited homes")

    val permissionDelHome = permissionManager.createSimplePermission("basics.delhome", "Allows to access the /delhome command")

    val msgHomeSet get() = messages.getMessage("home-set")
    val msgHomeDeleted get() = messages.getMessage("home-deleted")
    val msgHomeTeleported get() = messages.getMessage("home-teleported")
    val msgHomeNotFound get() = messages.getMessage("home-not-found")
    val msgHomeNoneSet get() = messages.getMessage("home-none-set")
    val msgHomeLimitReached get() = messages.getMessage("home-limit-reached")

    override fun onEnable() {
        storage = createStorage()

        createCommand()
            .name("home")
            .permission(permissionHome)
            .description("Teleports you to one of your homes")
            .usage("/home [name]")
            .executor(HomeCommand(this))
            .register()

        createCommand()
            .name("sethome")
            .permission(permissionSetHome)
            .description("Sets a home")
            .usage("/sethome [name]")
            .executor(SetHomeCommand(this))
            .register()

        createCommand()
            .name("delhome")
            .permission(permissionDelHome)
            .description("Deletes a home")
            .usage("/delhome [name]")
            .executor(DelHomeCommand(this))
            .register()
    }

    fun getHomeList(uuid: UUID): HomeList {
        return homes[uuid] ?: error("Home list is null")
    }

    private fun loadHomeListBlocking(uuid: UUID): HomeList {
        val storage = storage ?: error("Storage is null")
        try {
            val json = storage.getJsonElement(uuid.toString()).get()
            return if (json == null) {
                HomeList()
            } else {
                Serialization.fromJson(json, HomeList::class.java)
            }
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to load home list for $uuid", e)
            return HomeList()
        }
    }


    override fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            val homeList = loadHomeListBlocking(uuid)
            homes[uuid] = homeList
        }
    }

    override fun savePlayerData(uuid: UUID): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            val homes = homes[uuid] ?: error("Homes is null")
            val storage = storage ?: error("Storage is null")
            try {
                storage.setJsonElement(uuid.toString(), Serialization.toJson(homes)).get()
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Failed to save home list for $uuid", e)
            }
        }
    }

    override fun forgetPlayerData(uuid: UUID) {
        homes.remove(uuid)
    }


    fun parseHomeCmd(context: BasicsCommandContext): Either<Home, Boolean> {
        if(context.sender !is Player) {
            plugin.messages.commandNotFromConsole.sendToSender(context.sender)
            return Either.Right(true)
        }
        val player = context.sender as Player

        var homeName = "home"
        if(context.args.size == 1) {
            homeName = context.args[0]
        } else if(context.args.size > 1) {
            return Either.Right(false)
        }

        val homeList = getHomeList(player.uniqueId)

        if(homeList.isEmpty()) {
            msgHomeNoneSet.sendToSender(player)
            return Either.Right(true)
        }

        val home = homeList.getHome(homeName)

        if(home == null) {
            msgHomeNotFound.tagUnparsed("home", homeName).sendToSender(player)
            return Either.Right(true)
        }

        return Either.Left(home)
    }


}