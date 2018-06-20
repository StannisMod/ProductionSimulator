package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_end_day.*
import kotlinx.android.synthetic.main.end_day_view.view.*
import kotlinx.android.synthetic.main.item.view.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Functions.saveAllExceptInventory
import stannis.ru.productionsimulator.Models.*
import java.util.*

class EndDayActivity : AppCompatActivity() {
    val curData = DataTime.getInstance(this)
    var tmpSum = 0
    fun setStartSettings() {
        number.text = curData.toString()
        curData.nextDay(this)
        playerNalog.text = Player.getInstance(this).nalog.toString()
        tmpSum = MoneyForDay.getIns(this).getAll()
        allInAllValue.text = "${tmpSum}$"
        if (allInAllValue.text.toString()[0] == '-') {
            allInAllValue.setTextColor(Color.RED)
        } else {
            allInAllValue.text = "+${allInAllValue.text}"
            allInAllValue.setTextColor(Color.GREEN)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_day)
        Inventory.saveInventories(this)

        setStartSettings()

        gridView.adapter = MoneyStatsAdapter(this, arrayListOf(0, 1))
        nalogValue.setOnKeyListener { view, i, keyEvent ->
            if ((view as EditText).text.toString() != "") {
                allInAllValue.text = "${tmpSum - view.text.toString().toInt()}$"
            } else {
                allInAllValue.text = "${tmpSum}$"
            }
            if (allInAllValue.text.toString()[0] == '-') {
                allInAllValue.setTextColor(Color.RED)
            } else {
                allInAllValue.text = "+${allInAllValue.text}"
                allInAllValue.setTextColor(Color.GREEN)
            }
            false
        }
        nextDay.setOnClickListener {
            if (nalogValue.text.toString() != "") {
                Player.getInstance(this).money -= nalogValue.text.toString().toInt()
                MoneyForDay.getIns(this).setNull()
                saveAllExceptInventory(this)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }
}

class MoneyStatsAdapter : BaseAdapter {

    var nums = ArrayList<Int>()
    var context: Context? = null

    constructor(context: Context, nums: ArrayList<Int>) : super() {
        this.context = context
        this.nums = nums

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val mfd = MoneyForDay.getIns(context!!)

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.end_day_view, null)
        if (position == 0) {
            itemView.namePunct.text = "Зарплаты:"
            itemView.value.text = "-${mfd.wages}$"
            itemView.value.setTextColor(Color.RED)
        } else {
            if (position == 1) {
                itemView.namePunct.text = "Продажи:"
                itemView.value.text = "+${mfd.sellings}$"
                itemView.value.setTextColor(Color.GREEN)
            } else {

            }
        }
        return itemView
    }

    override fun getItem(position: Int): Any {
        return nums.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return nums.size
    }

}

