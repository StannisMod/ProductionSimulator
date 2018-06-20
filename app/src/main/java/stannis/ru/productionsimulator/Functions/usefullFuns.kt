package stannis.ru.productionsimulator.Functions

import android.content.Context
import stannis.ru.productionsimulator.Models.*
import kotlin.math.roundToInt


fun round(a: Double, radix: Int): Double {
    var b = a
    b *= Math.pow(10.0, radix.toDouble())
    b = b.roundToInt().toDouble()
    b /= Math.pow(10.0, radix.toDouble())
    return b

}
fun saveAllExceptInventory(ctx : Context){
    for(i in 0 until Factory.factories.size){
        Factory.getFactoryById(i)!!.save(ctx)
    }
    Player.save(ctx)
    DataTime.save(ctx)
    MoneyForDay.save(ctx)

}