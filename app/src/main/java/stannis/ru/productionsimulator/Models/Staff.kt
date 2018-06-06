package stannis.ru.productionsimulator.Models

class Staff(var name: String, var age: Int, var prof: String, var quality: Int, var nation: String, var salary: Int, var birth: Pair<String, String>) {
    override fun toString(): String {
        return "${name}, ${prof}, ${age}"
    }

    fun toDetailedString(): String = "${name},${age},${prof},${nation},${salary},${birth.first},${birth.second}"

    fun getPromotion() {
        salary += (0.05 * salary) as Int;
    }

    fun getPromotionN_t() {
        salary -= (0.05 * salary) as Int;
    }

    fun birth_Day() {
        age++
    }


}
