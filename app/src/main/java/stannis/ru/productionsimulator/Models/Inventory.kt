package stannis.ru.productionsimulator.Models

import android.content.Context
import android.util.Log

class Inventory(val name : String, val size : Int, val maxStackSize : Int) {

    companion object {
        const val TAG = "PlayerInv"
        var instance: Inventory? = null
        private val inventories = HashMap<String, Inventory>()

        @Synchronized
        fun getInventory(name : String = TAG): Inventory {
            Log.d("Inv", name)
            if (instance == null && name == TAG) {
                instance = Inventory(TAG, 16, 64)
            } else if (name != TAG) {
                if( inventories.get(name) == null)
                     createInventory(name, 16, 64)
                return inventories.get(name)!!
            }
            return instance!!
        }

        fun createInventory(name : String, size : Int, maxStackSize : Int) = inventories.put(name, Inventory(name, size, maxStackSize))

        fun transferItem(from : Inventory, to : Inventory, slotIndex : Int, quantity : Int) {
            if (from.getInventorySlotContents(slotIndex).stackSize < quantity)
                return

            var i = to.findFirstEqualSlot(from.getInventorySlotContents(slotIndex).itemId)
            if (to.isSlotEmpty(i))
                to.setInventorySlotContents(i, ItemStack(from.getInventorySlotContents(slotIndex).itemId, quantity, to.getInventoryStackLimit()))
            else
                to.getInventorySlotContents(i).stackSize += quantity

            from.decrStackSize(slotIndex, quantity)
        }

        fun load(ctx : Context, name : String) : Inventory? = DatabaseFactory.getInstance(ctx).getInventory(name)
    }

    var inv = Array(size, {ItemStack(0, 0, this.maxStackSize)})

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
        for (i in 0..(size - 1)) {
            // Log.d("FIND", getInventorySlotContents(i).toString())
            // Log.d("FIND", isSlotEmpty(i).toString())
            if (isSlotEmpty(i)) {
                // Log.d("FIND", "RETURNING $i")
                return i
            }
        }
        return -1
    }

    fun findFirstEqualSlot(id : Int) : Int {
        for (i in 0..(size - 1))
            if (getInventorySlotContents(i).itemId == id)
                return i

        return findFirstEmptySlot()
    }

    fun decrStackSize(slotIndex: Int, denominator : Int) {
        if (slotIndex < 0 || slotIndex > getInventorySize() - 1)
            return

        Log.d("DECR", "DECR!!!")

        if (getInventorySlotContents(slotIndex).stackSize <= denominator)
            setSlotEmpty(slotIndex)
        else
            getInventorySlotContents(slotIndex).stackSize -= denominator
    }

    fun save(ctx : Context) {
        if (DatabaseFactory.getInstance(ctx).getInventory(this.name) == null) {
            //Log.d("Shutdown thread", "Added!")
            DatabaseFactory.getInstance(ctx).addInventory(this)
        }
        else {
            //Log.d("Shutdown thread", "Updated!")
            DatabaseFactory.getInstance(ctx).updateInventory(this)
            //Log.d("Inv_shutdown", DatabaseFactory.getInstance(ctx).getInventory(this.name)?.getInventorySlotContents(0).toString())
        }
    }
}