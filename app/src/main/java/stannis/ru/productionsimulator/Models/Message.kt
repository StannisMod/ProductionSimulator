package stannis.ru.productionsimulator

class Message(val caption : String = "Отдайте деньги!", val text : String = "Здаравствуйте, Кирилл Юрьевич.\nВаша задолженность банку составляет 5000$. Просим Вас до 31.12.2018 выплатить задолженность, иначе нам придется заблокировать ваш счет и забрать вашу лесопилку.\nС любовью банк!", val sender : String = "Банк", var date : List<Int> = List<Int>(3) {19; 12; 2001}) {
    override fun toString(): String {
        return "${caption}\\ntext"
    }

    fun toCaption(): String{
        return "${sender}, ${date[0]}.${date[1]}.${date[2]}\n${caption}"
    }
}