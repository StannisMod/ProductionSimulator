package stannis.ru.productionsimulator.Functions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.GameOverActivity
import stannis.ru.productionsimulator.MessageActivity
import stannis.ru.productionsimulator.Models.Message
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
    generateUnhappyMessage(ctx)
    var list : ArrayList<Message> = ins.getMessage()
    val intent = Intent(ctx, GameOverActivity::class.java)
    intent.putExtra("message", list[0].toStringArray())
    startActivity(ctx, intent, Bundle.EMPTY)
}