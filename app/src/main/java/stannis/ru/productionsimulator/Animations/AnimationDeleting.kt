package stannis.ru.productionsimulator.Animations

import android.content.Context
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

import android.widget.ListView
import stannis.ru.productionsimulator.MessageAdapter
import stannis.ru.productionsimulator.Models.Message

fun deleteAnimation(from: Float, to: Float, position: Int, listView: ListView, dataArray : ArrayList<stannis.ru.productionsimulator.Models.Message>, ctx : Context): Animation {
    var animation = TranslateAnimation(from, to, 0.toFloat(), 0.toFloat())
    animation.fillAfter = true
    animation.duration = 200
    class AnimationListener() : Animation.AnimationListener {
        override fun onAnimationEnd(p0: Animation?) {
            dataArray[position].remove()
            dataArray.remove(dataArray[position])
            var adapter = MessageAdapter(ctx, dataArray)
            adapter.notifyDataSetChanged()
            listView.adapter = adapter
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationStart(p0: Animation?) {
        }
    }
    animation.setAnimationListener(AnimationListener())
    return animation
}