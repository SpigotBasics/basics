package com.github.spigotbasics.nms.v1_20_4

import com.github.spigotbasics.nms.v1_20_4.MenuBuilderImpl.ContainerProvider
import net.minecraft.core.BlockPos
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
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.event.inventory.InventoryType

object MenuBuilderImpl {

    private val builder = mutableMapOf<InventoryType, ContainerProvider>()

    init {
        builder[InventoryType.ANVIL] = worldAccess(::AnvilMenu)
        builder[InventoryType.ENCHANTING] = worldAccess(::EnchantmentMenu)
        builder[InventoryType.LOOM] = worldAccess(::LoomMenu)
        builder[InventoryType.CARTOGRAPHY] = worldAccess(::CartographyTableMenu)
        builder[InventoryType.GRINDSTONE] = worldAccess(::GrindstoneMenu)
        builder[InventoryType.SMITHING] = worldAccess(::SmithingMenu)
    }

    fun build(serverPlayer: ServerPlayer, inventoryType: InventoryType): AbstractContainerMenu? {
        return (builder[inventoryType] ?: return null).supply(serverPlayer, serverPlayer.inventory)
    }

    private fun interface ContainerProvider {
        fun supply(player: ServerPlayer, playerInventory: Inventory): AbstractContainerMenu
    }

    private fun tile(block: Block, entity: (BlockPos, BlockState) -> MenuConstructor): ContainerProvider {
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
                ContainerLevelAccess.create(player.level(), player.blockPosition())
            )
        }
    }
}
