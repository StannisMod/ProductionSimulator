package stannis.ru.productionsimulator.Models

import android.content.ClipData
import android.content.Context
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Enums.Items

class Inventory(val name : String, val size : Int, val maxStackSize : Int) {

    companion object {
        const val TAG = "PlayerInv"
        var instance: Inventory? = null
        val inventories = HashMap<String, Inventory?>()

        @Synchronized
        fun getInventory(name: String = TAG): Inventory {
            Log.d("Inv", name)
            if (instance == null && name == TAG) {
                instance = Inventory(TAG, 16, 64)
            } else if (name != TAG) {
                // if (inventories.get(name) == null)
                //     inventories.put(name, load())
                if (inventories.get(name) == null)
                    createInventory(name, 16, 64)
                return inventories.get(name)!!
            }
            return instance!!
        }

        fun saveInventories(ctx: Context) {
            val iterator = inventories.iterator()

            getInventory().save(ctx)
            while (iterator.hasNext())
                iterator.next().value?.save(ctx)
        }

        fun setNulls() {
            val iterator = inventories.iterator()

            instance = null

            while (iterator.hasNext())
                iterator.next().setValue(null)
        }

        fun createInventory(name: String, size: Int, maxStackSize: Int) = inventories.put(name, Inventory(name, size, maxStackSize))

        fun transferItem(from: Inventory, to: Inventory, slotIndex: Int, quantity: Int): Boolean {
            if (from.getInventorySlotContents(slotIndex).stackSize < quantity)
                return false

            var i = to.findFirstEqualSlot(from.getInventorySlotContents(slotIndex).itemId)
            if (to.isSlotEmpty(i)) {
                Log.d("Store", "created")
                to.setInventorySlotContents(i, ItemStack(from.getInventorySlotContents(slotIndex).itemId, quantity, to.getInventoryStackLimit()))
            } else {
                Log.d("Store", "added")
                to.getInventorySlotContents(i).stackSize += quantity
            }
            return from.decrStackSize(slotIndex, quantity)
        }

        fun load(ctx: Context, name: String): Inventory? = DatabaseFactory.getInstance(ctx).getInventory(name)
    }

    var inv = Array(size, { ItemStack(0, 0, this.maxStackSize) })

    fun setInventorySlotContents(slotIndex: Int, stack: ItemStack) {
        if (slotIndex < 0 || slotIndex > maxStackSize - 1)
            return

        inv.set(slotIndex, stack)
    }

    fun getInventoryStackLimit(): Int = maxStackSize

    fun getInventorySize(): Int = size

    fun getInventorySlotContents(slotIndex: Int): ItemStack = inv.get(slotIndex)

    fun isSlotEmpty(slotIndex: Int): Boolean = getInventorySlotContents(slotIndex).isEmpty()

    fun setSlotEmpty(slotIndex: Int) {
        inv.drop(slotIndex)
        getInventorySlotContents(slotIndex).stackSize = 0
    }

    fun findFirstEmptySlot(): Int {
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

    fun findFirstEqualSlot(id: Int): Int {
        for (i in 0..(size - 1))
            if (getInventorySlotContents(i).itemId == id)
                return i

        return findFirstEmptySlot()
    }

    fun decrStackSize(slotIndex: Int, denominator: Int): Boolean {
        if (slotIndex < 0 || slotIndex > getInventorySize() - 1)
            return false

        Log.d("DECR", "DECR!!!")

        if (getInventorySlotContents(slotIndex).stackSize <= denominator) {
            setSlotEmpty(slotIndex)
            normalize()
            return true
        } else {
            getInventorySlotContents(slotIndex).stackSize -= denominator
            return false
        }
    }

    fun save(ctx: Context) {
        if (DatabaseFactory.getInstance(ctx).getInventory(this.name) == null)
            DatabaseFactory.getInstance(ctx).addInventory(this)
        else
            DatabaseFactory.getInstance(ctx).updateInventory(this)
    }

    fun normalize() {
        var tmpInv = Array(size, { ItemStack(0, 0, maxStackSize) })
        var i = 0
        for (st in inv) {
            if (!st.isEmpty()) {
                tmpInv.set(i, st)
                i++
            }
        }
        inv = tmpInv
        Log.d("Normalize", inv.toDetailedString())
    }

    fun clear() {
        for (i in 0..(getInventorySize() - 1))
            setSlotEmpty(i)
    }


}
fun Array<ItemStack>.toDetailedString():String{
    var str : String=""
    for(inv in this){
        str = str + Items.findById(inv.itemId).name
    }
    return str
}