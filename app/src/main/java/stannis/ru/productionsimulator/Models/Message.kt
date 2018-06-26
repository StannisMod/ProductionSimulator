package stannis.ru.productionsimulator.Models

import android.content.Context
import android.util.Log
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase

class Message(var caption: String = "Отдайте деньги!", var text: String = "Здраствуйте, Кирилл Юрьевич.\nВаша задолженность банку составляет 5000$. Просим Вас до 31.12.2018 выплатить задолженность, иначе нам придется заблокировать ваш счет и забрать вашу лесопилку.\nС любовью банк!", var sender: String = "Банк", var date: Array<String> = arrayOf("19", "12", "2001"), var readed: String = "0") : Comparable<Message> {
    override fun compareTo(other: Message): Int {
        if (this.date[2] != other.date[2]) {
            return -(this.date[2].toInt() - other.date[2].toInt())
        }
        if (this.date[1] != other.date[1]) {
            return -(this.date[1].toInt() - other.date[1].toInt())
        }
        return -(this.date[0].toInt() - other.date[0].toInt())
    }

    companion object {
        fun setBegin() {
            instance = Array(2) { i -> ArrayList<Message>() }
        }

        var instance: Array<ArrayList<Message>> = emptyArray()
        var messages: ArrayList<Message> = ArrayList()
        private fun getMessage(secondIndex: Int, index: Int): Message {
            return instance[secondIndex][index]!!
        }

        fun getMessage(index: Int): Message {
            return getMessage(0, index = index)
        }

        fun getMessageReaded(index: Int): Message {
            return getMessage(1, index = index)
        }

        fun loadAll(ctx: Context) {
            for (mes in PlayerStatsDatabase.getInstance(ctx).getMessage()) {
                instance[0].add(mes)
            }
            for (mes in PlayerStatsDatabase.getInstance(ctx).getMessageReaded()) {
                instance[1].add(mes)
            }
            Log.d("Messages", instance[0].toString())
            Log.d("Messages", instance[1].toString())
        }

        fun getAllMessages(): ArrayList<Message> {
            if (messages.isEmpty()) {
                buildMessages()
            }
            return messages

        }

        fun addMessage(mes: Message) {
            instance[0].add(mes)
            buildMessages()
        }

        private fun buildMessages() {
            var list0 = ArrayList<Message>()
            for (mes in instance[0]) {
                list0.add(mes)
            }
            for (mes in instance[1]) {
                list0.add(mes)
            }
            list0.sort()
            messages = list0
            Log.d("Messages", messages.toString())
        }

        fun saveAll(ctx: Context) {
            PlayerStatsDatabase.getInstance(ctx).removeAllMessage()
            PlayerStatsDatabase.getInstance(ctx).removeAllMessageReaded()

            for (arr in instance) {
                for (mes in arr) {
                    mes.save(ctx)
                }
            }
        }

        private fun getList(index: Int, ctx: Context): ArrayList<Message> {
            if (instance[index].isEmpty()) {
                Log.d("Messages", "The list is empty")
            }
            return instance[index]
        }


        fun getMessages(ctx: Context) = getList(0, ctx)
        fun getMessagesReaded(ctx: Context) = getList(1, ctx)

        fun clear() {
            instance = emptyArray()
        }

        fun sizeOfUnRead(): Int = instance[0].size
    }


    fun toCaption(): String {
        return "${sender}   /${date[0]}.${date[1]}.${date[2]}\n     ${caption}"
    }

    fun toStringArray(): Array<String> {
        return arrayOf(caption, text, sender, "${date[0]}.${date[1]}.${date[2]}")
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }

    override fun toString(): String = toCaption()
    fun dateToString(): String = "${date[0]}.${date[1]}.${date[2]}"

    fun isEqual(mes: Message): Boolean = this.hashCode() == mes.hashCode()


    fun save(ctx: Context) {
        for (mes in instance[0]) {
            if (mes.isEqual(this)) {
                PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(this)
                return
            }
        }
        for (mes in instance[1]) {
            if (mes.isEqual(this)) {
                PlayerStatsDatabase.getInstance(ctx).addMessageReadedWithProperties(this)
                return
            }
        }
    }

    fun makeRead() {
        instance[0].remove(this)
        instance[1].add(this)
        Log.d("Messages", instance.toString())
    }

    fun remove() {
        instance[0].remove(this)
        instance[1].remove(this)
        buildMessages()
    }

    fun isRead(): Boolean {
        for (mes in instance[0]) {
            if (mes.isEqual(this))
                return false
        }
        return true
    }
}