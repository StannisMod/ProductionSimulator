package stannis.ru.productionsimulator.Models

import android.content.Context
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import kotlin.math.roundToInt

class Staff(var name: String, var age: Int, var prof: String, var quality: Int, var nation: String, var salary: Int, var birth: Pair<String, String>) {

    var added = false
    override fun toString(): String {
        return "${name}, ${prof}, ${age}"
    }

    fun toDetailedString(): String = "${name},${age},${prof},${nation},${salary},${birth.first},${birth.second}"

    fun getPromotion() {
        salary += (0.2*salary.toDouble()).roundToInt()
    }

    fun getPromotionN_t() {
        salary -= (0.05 * salary).roundToInt();
    }

    fun birth_Day(toLabor:Boolean, ctx:Context) {
        age++
        val ins = DatabaseFactory.getInstance(ctx)
        if(toLabor){
            ins.setLaborExchangeWithProperties(this)
        }else{
            ins.setStaffWithProperties(this)
        }
    }


}
