package stannis.ru.productionsimulator.Functions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.StartActivity

fun gameover(ctx: Context){
    val ins = PlayerStatsDatabase.getInstance(ctx)
    ins.removeAllMessageReaded()
    ins.removeAllMessage()
    ins.removePlayer()
    ins.removeAllCredits()
    DatabaseFactory.getInstance(ctx).removeAllLabor()
    ins.removeAllNames()
    DatabaseFactory.getInstance(ctx).removeAllStaff()
    clearInstances()
    val intent = Intent(ctx, StartActivity::class.java)
    Toast.makeText(ctx, "Извините, но вы банктрот... Если хотите попробовать еще раз нажмите кнопку 'Новая игра'\nЕсли вы не понимаете почему, то зайдите в новую игру и прочитайте письмо.", Toast.LENGTH_LONG).show()
    generateUnhappyMessage(ctx)
    startActivity(ctx, intent, Bundle.EMPTY)
}