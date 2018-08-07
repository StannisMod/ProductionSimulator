package stannis.ru.productionsimulator.Models

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Enums.EnumFactory
import kotlin.math.roundToInt

class Worker(var name: String, var age: Int, var prof: String, var quality: Int, var nation: String, var salary: Int, var birth: Pair<String, String>) {

    companion object {

        fun contains(str: String): Boolean {
            var res = false
            for (i in 0 until EnumFactory.getSize()) {
                for (j in 0..1) {
                    res = res || instance[i][j].contains(str)
                    if (res) return res
                }

            }
            return res
        }

        fun isEmpty(): Boolean {
            val ind = DatabaseFactory.index
            var res = false
            for (i in 0 until EnumFactory.getSize()) {
                DatabaseFactory.index = i
                res = res || (Worker.sizeOfStaff() > 0)
                if (res) {
                    break
                }
            }
            DatabaseFactory.index = ind
            return !res
        }

        fun setBegin() {
            instance = ArrayList()
            for (i in 0 until EnumFactory.getSize()) {
                instance.add(Array(2) { i -> HashMap<String, Worker>() })
            }
        }

        var instance: ArrayList<Array<HashMap<String, Worker>>> = ArrayList()
        fun getWorkerFromStaffById(firstIndex: Int, id: Int): Worker? {
            Log.d("IIIIFirstIndex", firstIndex.toString())
            var hm = instance[firstIndex][1]
            val iterator = hm.iterator()
            var i = 0
            Log.d("IIII", id.toString())
            while (iterator.hasNext()) {
                if (i == id) {
                    return iterator.next().value
                }
                iterator.next()
                Log.d("IIIIii", i.toString())
                i++
            }
            return null
        }

        fun getStaff(ctx: Context, type: String, name: String): Worker {
            var index = DatabaseFactory.index
            var secondIndex = if (type == "labor") 0 else 1

            if (instance[index][secondIndex].get(name) == null) {
                instance[index][secondIndex].put(name, load(ctx, secondIndex, name)!!)
            }
            return instance[index][secondIndex].get(name)!!
        }

        private fun getHMOfLabor(): HashMap<String, Worker> {
            return instance[DatabaseFactory.index][0]
        }

        fun getListOfLabor(): ArrayList<Worker> {
            val hm = getHMOfLabor()
            val iterator = hm.iterator()
            val res = ArrayList<Worker>()
            while (iterator.hasNext())
                res.add(iterator.next().value)
            return res
        }

        private fun getHMOfStaff(): HashMap<String, Worker> {
            return instance[DatabaseFactory.index][1]
        }

        fun getListOfStaff(): ArrayList<Worker> {
            val hm = getHMOfStaff()
            val iterator = hm.iterator()
            val res = ArrayList<Worker>()
            while (iterator.hasNext())
                res.add(iterator.next().value)
            return res
        }

        private fun load(ctx: Context, secondIndex: Int, name: String): Worker? {
            if (secondIndex == 0)
                return DatabaseFactory.getInstance(ctx).getWorkerFromLabor(name)

            return DatabaseFactory.getInstance(ctx).getWorkerFromStaff(name)

        }

        fun sizeOfStaff(): Int = sizeOfList(1)
        fun sizeOfLabor(): Int = sizeOfList(0)
        private fun sizeOfList(secondIndex: Int): Int {
            var size = 0
            val hm = instance[DatabaseFactory.index][secondIndex]
            val iterator = hm.iterator()
            while (iterator.hasNext()) {
                iterator.next()
                size++
            }
            Log.d("WORKERsize", size.toString())
            return size
        }

        fun saveAll(ctx: Context) {
            var index = 0
            var hm = HashMap<String, Worker>()
            for (al in instance) {
                DatabaseFactory.index = index
                DatabaseFactory.getInstance(ctx).removeAllStaff()
                DatabaseFactory.getInstance(ctx).removeAllLabor()
                hm = al[0]
                var iterator = hm.iterator()
                while (iterator.hasNext()) {
                    iterator.next().value.save(ctx, 0)
                }
                hm = al[1]
                iterator = hm.iterator()
                while (iterator.hasNext()) {
                    iterator.next().value.save(ctx, 1)
                }
                index++
            }
            Log.d("WORKER", "END")
        }

    }

    var added = false
    override fun toString(): String {
        return "${name}, ${prof}, ${age}"
    }

    fun toDetailedString(): String = "${name},${age},${prof},${nation},${salary},${birth.first},${birth.second}"

    fun getPromotion() {
        var tmp = salary
        salary += (0.2 * salary.toDouble()).roundToInt()
        if (tmp == salary) {
            salary *= 2
        }
    }


    fun birth_Day() {
        age++
    }

    fun save(ctx: Context, secondIndex: Int) {
        if (secondIndex == 0) {
            if (DatabaseFactory.getInstance(ctx).getWorkerFromLabor(this.name) == null) {
                DatabaseFactory.getInstance(ctx).addLaborExchangeWithProperties(this)
            } else {
                DatabaseFactory.getInstance(ctx).setLaborExchangeWithProperties(this)
            }
        } else {
            if (DatabaseFactory.getInstance(ctx).getWorkerFromStaff(this.name) == null) {
                DatabaseFactory.getInstance(ctx).addStaffWithProperties(this)
            } else {
                DatabaseFactory.getInstance(ctx).setStaffWithProperties(this)
            }
        }
    }

    fun fire() {

        instance[DatabaseFactory.index][1].remove(this.name)
    }

    fun generate() {
        instance[DatabaseFactory.index][0].put(this.name, this)
        Log.d("WORKERgEnerate", "Hello")
    }

    fun addToStaff() {
        instance[DatabaseFactory.index][1].put(this.name, this)
    }

    fun hire() {
        instance[DatabaseFactory.index][0].remove(this.name)
        instance[DatabaseFactory.index][1].put(this.name, this)
    }

}
