package com.github.spigotbasics.modules.basicshomes.v2

import com.github.spigotbasics.core.command.parsed.arguments.AnyStringArg
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.modules.basicshomes.v2.command.CommandDelHome
import com.github.spigotbasics.modules.basicshomes.v2.command.CommandHome
import com.github.spigotbasics.modules.basicshomes.v2.command.CommandHomeList
import com.github.spigotbasics.modules.basicshomes.v2.command.CommandSetHome
import com.github.spigotbasics.modules.basicshomes.v2.command.HomesArgument
import java.util.UUID
import java.util.concurrent.CompletableFuture

class BasicsHomesModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permissionHome =
        permissionManager.createSimplePermission("basics.home", "Allows to access the /home command")
    private val permissionSetHome =
        permissionManager.createSimplePermission("basics.sethome", "Allows to access the /sethome command")
    private val permissionDelHome =
        permissionManager.createSimplePermission("basics.delhome", "Allows to access the /delhome command")

    val permissionSetHomeMultiple =
        permissionManager.createSimplePermission(
            "basics.sethome.multiple",
            "Allows to set multiple named homes",
        )
    val permissionSetHomeUnlimited =
        permissionManager.createSimplePermission(
            "basics.sethome.multiple.unlimited",
            "Allows to set unlimited homes",
        )

    val regex get() = config.getString("regex", "[a-zA-Z_0-9-]+")!!

    private lateinit var homeStore: HomeStore

    override val messages: Messages = getConfig(ConfigName.MESSAGES, Messages::class.java)

    override fun onEnable() {
        homeStore = HomeStore(this)

        commandFactory.parsedCommandBuilder("sethome", permissionSetHome).mapContext {
            description("Teleports you to one of your homes")
            usage = "[name]"

            path {
                playerOnly()
            }

            path {
                // TODO: Permission Home Multiple
                playerOnly()
                arguments {
                    named("name", AnyStringArg("name"))
                }
            }
        }.executor(CommandSetHome(this, homeStore)).register()

        commandFactory.parsedCommandBuilder("delhome", permissionDelHome).mapContext {
            description("Deletes one of your homes")
            usage = "[name]"

            path {
                playerOnly()
            }

            path {
                playerOnly()
                arguments {
                    named("home", HomesArgument("home", homeStore))
                }
            }
        }.executor(CommandDelHome(this, homeStore)).register()

        commandFactory.parsedCommandBuilder("homes", permissionHome).mapContext {
            description("Provides a list of your homes")

            path {
                playerOnly()
            }
        }.executor(CommandHomeList(this, homeStore)).register()

        commandFactory.parsedCommandBuilder("home", permissionHome).mapContext {
            description("Teleports you to one of your homes")

            path {
                playerOnly()
            }

            path {
                playerOnly()
                arguments {
                    named("home", HomesArgument("home", homeStore))
                }
            }
        }.executor(CommandHome(this, homeStore)).register()
    }

    override fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            homeStore.loadHomeListBlocking(uuid)
        }
    }

    override fun savePlayerData(uuid: UUID): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            homeStore.saveHomeListBlocking(uuid)
        }
    }

    override fun forgetPlayerData(uuid: UUID) {
        homeStore.forgetHomeList(uuid)
    }
}
