package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.media.MediaExtractor
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.message_view.view.*
import org.jetbrains.anko.ctx
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Models.Message

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

        listview.setOnItemClickListener { parent, view, position, id ->
            val mes = Message.getAllMessages()[position]
            mes.makeRead()
            val intent = Intent(this, MessageActivity::class.java)
            intent.putExtra("message", position)
            startActivity(intent)
        }
        var ctx = this
        var x = 0.toFloat()
        var stX = listview.x
        var position: Int? = null
        var t = 0L
        class TOUCH() : View.OnTouchListener {
            override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
                if (motionEvent != null) {
                    if (position == null) {
                        position = motionEvent.y.toInt() / 200

                    }
                    var tp = motionEvent.action

                    if (tp == MotionEvent.ACTION_DOWN) {
                        x = motionEvent.x
                        t = System.currentTimeMillis()
                    } else if (tp == MotionEvent.ACTION_MOVE) {
                        if (position != null) {
                            if (listview.getChildAt(position!!) != null) {
                                listview.getChildAt(position!!).x = if ((motionEvent.x - x) < 0) listview.getChildAt(position!!).x else listview.getChildAt(position!!).x + (motionEvent.x - x)
                                x = motionEvent.x
                            }
                        }
                    } else if (tp == MotionEvent.ACTION_UP) {
                        if (position != null) {
                            if (listview.getChildAt(position!!) != null) {
                                val diffX = (listview.getChildAt(position!!).x - stX)
                                val diffT = (System.currentTimeMillis() - t)
                                Log.d("AVERAGEvelocity",(diffX*1000/diffT).toString() )
                                if (diffX*1000/diffT>2500) {
                                    dataArray[position!!].remove()
                                    dataArray = Message.messages
                                    listview.adapter = MessageAdapter(ctx, dataArray)
                                } else {
                                    listview.getChildAt(position!!).x = stX
                                }
                                position = null
                            }
                        }
                    }
                }
                return p0?.onTouchEvent(motionEvent) ?: true
            }

        }
        listview.setOnTouchListener(TOUCH())

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
