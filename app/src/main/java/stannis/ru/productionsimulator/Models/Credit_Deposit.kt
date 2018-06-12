package stannis.ru.productionsimulator.Models

class Credit_Deposit(var amount: Int, var percent: Double, var date: Array<String>, var type: Int) {


    fun takeDep_payOff(sum: Int): Boolean {
        if (sum >= this.amount) {
            this.amount = 0
            return true
        }
        this.amount = amount - sum;
        return false

    }


    override fun toString(): String {
        if (type == 1) {
            return "Ваш депозит состовляет ${amount}$, а ставка ${percent}% \n" +
                    "Дата открытия: ${date[0]}.${date[1]}.${date[2]}"
        }
        return "Ваш кредит состовляет ${amount}$, а ставка ${percent}% \n" +
                "Дата открытия: ${date[0]}.${date[1]}.${date[2]}"
    }


    fun rise() {
        amount += (amount * (percent / 100.0)).toInt()
    }


}