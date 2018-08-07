package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.graphics.Color

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import kotlinx.android.synthetic.main.message_view.view.*


import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.TouchListeners.MessageTouch

class MailActivity : AppCompatActivity() {
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)

        var listview: ListView = findViewById(R.id.listView)
        var dataArray = Message.getAllMessages()
        val adapter = MessageAdapter(this, dataArray)
        listview.adapter = adapter
        var ctx = this
        var x = 0.toFloat()
        listview.setOnTouchListener(MessageTouch(listview, ctx, dataArray))

    }

}

class MessageAdapter(var ctx: Context, var list: ArrayList<Message>) : BaseAdapter() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val mes = list[p0]
        var inflator = ctx!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.message_view, null)

        itemView.sender.text = mes.sender
        itemView.date.text = mes.dateToString()
        itemView.caption.text = mes.caption
        val cond = mes.isRead()
        Log.d("TOUCH_COND", cond.toString())
        itemView.body.setBackgroundColor(if (cond) Color.rgb(255, 255, 255) else Color.rgb(230, 200, 200))


        return itemView
    }


    override fun getCount(): Int = list.size


    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

}
