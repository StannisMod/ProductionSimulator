package stannis.ru.productionsimulator.Models

class ItemStack(val itemId: Int, var stackSize: Int, var maxStackSize: Int) {

    fun getType(): Int = itemId

    fun isStackFull(): Boolean = stackSize == maxStackSize

    fun duplicate(): ItemStack = ItemStack(itemId, stackSize, maxStackSize)

    fun isEmpty(): Boolean = stackSize == 0 || itemId == 0

    override fun toString(): String = "ITEM[ID: $itemId, SIZE: $stackSize, MAX: $maxStackSize]"
}