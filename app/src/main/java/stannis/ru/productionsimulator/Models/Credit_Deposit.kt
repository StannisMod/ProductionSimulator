package stannis.ru.productionsimulator.Models

class Credit_Deposit(var amount: Int, var percent: Double, var date: Pair<String, String>, var type: Int) {
    fun takeDep(sum: Int): Int {
        if (type == 1) {
            if (sum >= this.amount) {
                //TODO delete from database
                return amount
            } else {
                amount -= sum;
                return sum
            }
        } else {
            return 0
        }
    }

    fun payOff(sum: Int) {
        if (type == 0) {
            if (sum >= this.amount) {
                //TODO delete from database
            } else {
                amount -= sum;
            }
        }
    }

    fun rise() {
        amount += (amount * (percent / 100.0)).toInt()
    }


}