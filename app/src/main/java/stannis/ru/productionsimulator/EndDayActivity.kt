package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
import stannis.ru.productionsimulator.Models.ItemStack
import java.util.*

class EndDayActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_day)
        val ins = PlayerStatsDatabase.getInstance(this)
        val curData = ins.getDataTime()
        number.text = curData.toString()
        gridView.adapter = MoneyStatsAdapter(this, arrayListOf(0,1))

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
        val data = PlayerStatsDatabase.getInstance(context!!).getDataTime()!!

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.end_day_view, null)
        if (position == 0) {
            itemView.namePunct.text = "Зарплаты:"
            itemView.value.text = "-${data.getAllWages(context!!)}$"
            itemView.value.setTextColor(Color.RED)
        }else{
            if(position == 1){
                itemView.namePunct.text = "Продажи:"
                itemView.value.text = "+${10}$"
                itemView.value.setTextColor(Color.GREEN)
            }else{

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

