package stannis.ru.productionsimulator

class Message(var caption : String = "Отдайте деньги!", var text : String = "Здраствуйте, Кирилл Юрьевич.\nВаша задолженность банку составляет 5000$. Просим Вас до 31.12.2018 выплатить задолженность, иначе нам придется заблокировать ваш счет и забрать вашу лесопилку.\nС любовью банк!", var sender : String = "Банк", var day : Int = 19, var month : Int = 12, var year : Int = 2001) {
    override fun toString(): String {
        return "${caption}\\ntext"
    }

    fun toCaption(): String{
        return "${sender}   /${day}.${month}.${year}\n     ${caption}"
    }

    fun toStringArray(): Array<String>{
        return arrayOf(caption, text, sender, "${day}.${month}.${year}")
    }
}