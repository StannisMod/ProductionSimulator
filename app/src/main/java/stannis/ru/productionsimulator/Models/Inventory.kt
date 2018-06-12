package stannis.ru.productionsimulator.Models

import android.content.Context

class Inventory(val name : String, val size : Int, val maxStackSize : Int) {

    companion object {
        private var instance: Inventory? = null

        @Synchronized
        fun getInventory(): Inventory {
            if (instance == null) {
                instance = Inventory("PlayerInv", 16, 64)
            }
            return instance!!
        }

        fun transferItem(from : Inventory, to : Inventory, slotIndex : Int, quantity : Int) {
            if (from.getInventorySlotContents(slotIndex).stackSize < quantity)
                return

            to.setInventorySlotContents(to.findFirstEmptySlot(), ItemStack(from.getInventorySlotContents(slotIndex).itemId, quantity, to.getInventoryStackLimit()))

            from.getInventorySlotContents(slotIndex).stackSize -= quantity
        }

        fun load(ctx : Context, name : String) : Inventory? = DatabaseFactory.getInstance(ctx).getInventory(name)
    }

    var inv = Array<ItemStack>(size, {ItemStack(0, 0, this.maxStackSize)})

    fun setInventorySlotContents(slotIndex : Int, stack : ItemStack) {
        if (slotIndex < 0 || slotIndex > maxStackSize - 1)
            return

        inv.set(slotIndex, stack)
    }

    fun getInventoryStackLimit() : Int = maxStackSize

    fun getInventorySize() : Int = size

    fun getInventorySlotContents(slotIndex : Int) : ItemStack = inv.get(slotIndex)

    fun isSlotEmpty(slotIndex : Int) : Boolean = getInventorySlotContents(slotIndex).isEmpty()

    fun setSlotEmpty(slotIndex : Int) {
        getInventorySlotContents(slotIndex).stackSize = 0
    }

    fun findFirstEmptySlot() : Int {
        var i = 0

        for (i in 0..size) {
            if (isSlotEmpty(i))
                return i
        }

        return -1
    }

    fun decrStackSize(slotIndex: Int, denominator : Int) {
        if (slotIndex < 0 || slotIndex > maxStackSize - 1)
            return

        if (getInventorySlotContents(slotIndex).stackSize <= denominator)
            setSlotEmpty(slotIndex)
        else
            getInventorySlotContents(slotIndex).stackSize -= denominator
    }

    fun save(ctx : Context) {
        if (DatabaseFactory.getInstance(ctx).getInventory(this.name) == null)
            DatabaseFactory.getInstance(ctx).addInventory(this)
        else
            DatabaseFactory.getInstance(ctx).updateInventory(this)
    }
}