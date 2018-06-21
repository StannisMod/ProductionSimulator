package stannis.ru.productionsimulator.Functions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.widget.Toast
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.GameOverActivity
import stannis.ru.productionsimulator.MessageActivity
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Models.Player
import stannis.ru.productionsimulator.StartActivity

fun gameover(ctx: Context){
    PlayerStatsDatabase.getInstance(ctx).removePlayer()
    Player.clear()
    PlayerStatsDatabase.getInstance(ctx).removeAllMessage()
    generateUnhappyMessage(ctx)
    var list : ArrayList<Message> = PlayerStatsDatabase.getInstance(ctx).getMessage()
    val intent = Intent(ctx, GameOverActivity::class.java)
    intent.putExtra("message", list[0].toStringArray())
    Log.d("GameOver", "2")
    GO = true
    Toast.makeText(ctx, "Извините, но вы банктрот... Если хотите попробовать еще раз нажмите кнопку 'Новая игра'\nЕсли вы не понимаете почему, то зайдите в новую игру и прочитайте письмо.", Toast.LENGTH_LONG).show()
    Log.d("GameOver", "3")
    startActivity(ctx, intent, Bundle.EMPTY)
}