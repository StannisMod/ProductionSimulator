package stannis.ru.productionsimulator.Models

import android.content.Context

fun generateCreditBankMessage(credit : Credit_Deposit, ctx : Context){
    var message : Message = Message()
    message.caption = "У вас задолженность"
    message.sender = "Банк"
    message.text = "Здравствуйте.\nБолее ${credit.date[1].toInt()-DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth.toInt()-1} месяцев прошло с момента взятия кредита. Вы должны банку ${credit.amount}$.\nПожалуйста, погасите кредит или нам придется принять меры."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateCollectorMessage(ctx: Context){
    var message : Message = Message()
    message.caption = "Зря не платил!"
    message.sender = "ANONYMOUS"
    message.text = "Шалом, бараноподобные курицы!\nВы не на свой огород заехали. У тебя, симбиоз бабочки с куклой, есть один день, чтобы погасить кредит. Ты вообще о чем думал, когда неоплачивал кредит целый год. У тебя реально перфаратор вместо башки! Если не будет денег. Твоему работничку хана! И твоим деньгам! И лесопилке! И твоим правам, тыква!\n С уважением, Петербургские коллекторы."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateWorkerMessage(staff: Staff, ctx: Context){
    var message : Message = Message()
    message.caption = "Хазяйна, денег дай. А?"
    message.sender = "${staff.name}"
    message.text = "Здоровья, вам.\nДеля харошо идют. Только жена моей из Кареи не хватает деньга на пропитание. А я им все моё посылаю. Не погуби. Повысь зарплату. А то придется мне уйти с лесопилки в дом."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateLotoMessage(credit : Credit_Deposit, ctx : Context){
    var message : Message = Message()
    message.caption = "Вам крупно повезло!"
    message.sender = "Лотерея 'Хобот'"
    message.text = "Здравствуйте.\nПоздравляем! Вы выиграли главный приз нашей лотереи. Это 5000$. Завра вам их пришлют. \nЖелаем успехов."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateUnhappyMessage(credit : Credit_Deposit, ctx : Context){
    var message : Message = Message()
    message.caption = "Вам крупно не повезло!"
    message.sender = "Гидрометцентр"
    message.text = "Здравствуйте.\nВ вашу сторону надвигается мощный пожар. Видимо, бог вас за что-то покарал. Ваша лесопилка наверняка сгорит. А вы останетесь банкротом.\nС уважением судьба."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}