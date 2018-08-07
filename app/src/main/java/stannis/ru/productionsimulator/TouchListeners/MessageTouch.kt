package stannis.ru.productionsimulator.TouchListeners

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity

import android.util.Log
import android.view.MotionEvent
import android.view.View

import android.widget.ListView

import stannis.ru.productionsimulator.Animations.deleteAnimation
import stannis.ru.productionsimulator.MessageActivity

import stannis.ru.productionsimulator.Models.Message


class MessageTouch(var listview: ListView, var ctx: Context, var dataArray: ArrayList<Message>) : View.OnTouchListener {

    var position: Int? = null
    var p = Pair(0.toFloat(), 0.toFloat())
    var deltaX = 100.0.toFloat()
    var maxDeltaY = 200.0.toFloat()
    var from = 0.toFloat()
    var to = 1100.toFloat()

    override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
        if (motionEvent != null) {
            if (position == null) {
                position = motionEvent.y.toInt() / 200
            }
            Log.d("Animation", position.toString())
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> p = Pair(motionEvent.x, motionEvent.y)


                MotionEvent.ACTION_UP ->
                    if (motionEvent.x - p.first > deltaX && Math.abs(motionEvent.y - p.second) < maxDeltaY) {
                        listview.getChildAt(position!!).startAnimation(deleteAnimation(from, to, position!!, listview, dataArray, ctx))
                        position = null
                    } else
                        if (Math.abs(motionEvent.y - p.second) < maxDeltaY) {
                            val mes = dataArray[position!!]
                            mes.makeRead()
                            val intent = Intent(ctx, MessageActivity::class.java)
                            intent.putExtra("message", position!!)
                            position = null
                            startActivity(ctx, intent, Bundle.EMPTY)
                        }


            }
        }
        return p0?.onTouchEvent(motionEvent) ?: true


    }
}