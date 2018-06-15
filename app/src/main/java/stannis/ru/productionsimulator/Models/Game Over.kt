package stannis.ru.productionsimulator.Models

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import stannis.ru.productionsimulator.StartActivity

fun gameover(ctx: Context){
    DatabaseFactory.getInstance(ctx).removeAllMessageReaded()
    DatabaseFactory.getInstance(ctx).removeAllMessage()
    DatabaseFactory.getInstance(ctx).removePlayer()
    DatabaseFactory.getInstance(ctx).removeAllCredits()
    DatabaseFactory.getInstance(ctx).removeAllLabor()
    DatabaseFactory.getInstance(ctx).removeAllNames()
    DatabaseFactory.getInstance(ctx).removeAllStaff()
    val intent = Intent(ctx, StartActivity::class.java)
    Toast.makeText(ctx, "Извините, но вы банктрот... Если хотите попробовать еще раз нажмите кнопку 'Новая игра'\nЕсли вы не понимаете почему, то зайдите в новую игру и прочитайте письмо.", Toast.LENGTH_LONG).show()
    generateUnhappyMessage(ctx)
    startActivity(ctx, intent, Bundle.EMPTY)
}