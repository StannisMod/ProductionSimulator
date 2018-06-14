package stannis.ru.productionsimulator.Models

import android.content.Context

class Message(var caption : String = "Отдайте деньги!", var text : String = "Здраствуйте, Кирилл Юрьевич.\nВаша задолженность банку составляет 5000$. Просим Вас до 31.12.2018 выплатить задолженность, иначе нам придется заблокировать ваш счет и забрать вашу лесопилку.\nС любовью банк!", var sender : String = "Банк", var date : Array<String> = arrayOf("19", "12", "2001"), var readed : String = "0") {
    override fun toString(): String {
        return "${caption}\\ntext"
    }

    fun toCaption(): String{
        return "${sender}   /${date[0]}.${date[1]}.${date[2]}\n     ${caption}"
    }

    fun toStringArray(): Array<String>{
        return arrayOf(caption, text, sender, "${date[0]}.${date[1]}.${date[2]}")
    }

    override fun hashCode():Int{
        return text.hashCode()
    }
}