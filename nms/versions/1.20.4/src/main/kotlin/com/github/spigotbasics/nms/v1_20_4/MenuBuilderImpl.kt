package com.github.spigotbasics.nms.v1_20_4

import com.github.spigotbasics.nms.v1_20_4.MenuBuilderImpl.ContainerProvider
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.AnvilMenu
import net.minecraft.world.inventory.CartographyTableMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.EnchantmentMenu
import net.minecraft.world.inventory.GrindstoneMenu
import net.minecraft.world.inventory.LoomMenu
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.inventory.SmithingMenu
import net.minecraft.world.inventory.StonecutterMenu
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.event.inventory.InventoryType

object MenuBuilderImpl {
    private val builder = mutableMapOf<InventoryType, ContainerProvider>()
    private val titles = mutableMapOf<InventoryType, Component>()

    init {
        builder[InventoryType.ANVIL] = worldAccess(::AnvilMenu)
        builder[InventoryType.ENCHANTING] = worldAccess(::EnchantmentMenu)
        builder[InventoryType.LOOM] = worldAccess(::LoomMenu)
        builder[InventoryType.CARTOGRAPHY] = worldAccess(::CartographyTableMenu)
        builder[InventoryType.GRINDSTONE] = worldAccess(::GrindstoneMenu)
        builder[InventoryType.SMITHING] = worldAccess(::SmithingMenu)
        builder[InventoryType.STONECUTTER] = worldAccess(::StonecutterMenu)
    }

    init {
        titles[InventoryType.ANVIL] = Component.translatable("container.repair")
        titles[InventoryType.ENCHANTING] = Component.translatable("container.enchant")
        titles[InventoryType.LOOM] = Component.translatable("container.loom")
        titles[InventoryType.CARTOGRAPHY] = Component.translatable("container.cartography_table")
        titles[InventoryType.GRINDSTONE] = Component.translatable("container.grindstone_title")
        titles[InventoryType.SMITHING] = Component.translatable("container.upgrade")
        titles[InventoryType.STONECUTTER] = Component.translatable("container.stonecutter")
    }

    fun build(
        serverPlayer: ServerPlayer,
        inventoryType: InventoryType,
    ): AbstractContainerMenu? {
        return (builder[inventoryType] ?: return null).supply(serverPlayer, serverPlayer.inventory)
    }

    fun title(inventoryType: InventoryType): Component? {
        return titles[inventoryType]
    }

    private fun interface ContainerProvider {
        fun supply(
            player: ServerPlayer,
            playerInventory: Inventory,
        ): AbstractContainerMenu
    }

    private fun tile(
        block: Block,
        entity: (BlockPos, BlockState) -> MenuConstructor,
    ): ContainerProvider {
        return ContainerProvider { player, inventory ->
            return@ContainerProvider entity.invoke(BlockPos.ZERO, block.defaultBlockState())
                .createMenu(player.nextContainerCounter(), inventory, player)!!
        }
    }

    private fun worldAccess(accessor: (Int, Inventory, ContainerLevelAccess) -> AbstractContainerMenu): ContainerProvider {
        return ContainerProvider { player, inventory ->
            return@ContainerProvider accessor.invoke(
                player.nextContainerCounter(),
                inventory,
                ContainerLevelAccess.create(player.level(), player.blockPosition()),
            )
        }
    }
}
