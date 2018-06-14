package stannis.ru.productionsimulator.Models

import android.content.Context
import java.util.*

fun generateMessage(ctx: Context){
    //УСЛОВИЯ: Письмо приходит по истечение 6 месяцев с взятия не погашенного кредита. ПОСЛЕДСТВИЯ: Нет
    val list = DatabaseFactory.getInstance(ctx).getListOfCreditDeposit()
    for (crDep in list) {
        if(crDep.type == 2 && crDep.date[0].toInt() == DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay.toInt()){
            if((DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear.toInt()-crDep.date[2].toInt())*12 + DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth.toInt() - crDep.date[1].toInt()>= 6){
                generateCreditBankMessage(crDep, ctx)
            }
        }
    }

    //УСЛОВИЯ: Письмо приходит по истечение 12 месяцев с взятия не погашенного кредита. ПОСЛЕДСТВИЯ: Погибает один из работников и забираются все средства, достаточные для погашения кредита. Если их не достаточно, то изымаем 80%
    for (crDep in list) {
        if(crDep.type == 2 && crDep.date[0].toInt() == DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay.toInt()){
            if((DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear.toInt()-crDep.date[2].toInt())*12 + DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth.toInt() - crDep.date[1].toInt()>= 12){
                generateCollectorMessage(ctx)
            }
        }
    }

    //УСЛОВИЕ: Ежедневно с вероятностью 5% ПОСЛЕДСТВИЯ: Нет
    if(Math.random() < 0.05){
        generateWorkerMessage(ctx)
    }

    //УСЛОВИЕ: Ежедневно с вероятностью 0,5% ПОСЛЕДСТВИЯ: Добавляется 5000$
    if(Math.random() < 0.005){
        generateLotoMessage(ctx)
        var player = DatabaseFactory.getInstance(ctx).getPlayerStats()
        if(player != null) {
            player.money += 5000
            DatabaseFactory.getInstance(ctx).setPlayerWithProperties(player)
        }
    }

    //УСЛОВИЕ: В дни, когда репутация меньше 10 с вероятностью 15% ПОСЛЕДСТВИЯ: GAME OVER
    if(DatabaseFactory.getInstance(ctx).getPlayerStats()!!.reputation < 10 && Math.random() < 0.15){
        generateUnhappyMessage(ctx)
        //gameover()
    }

    //УСЛОВИЕ: Верятность 5,1%-(реп)/20 ПОСЛЕДСТВИЯ: ежедневный налог увеличен на 100$
    if(Math.random() < 5.1-DatabaseFactory.getInstance(ctx).getPlayerStats()!!.reputation/20){
        generateControlMessage(ctx)
        var player = DatabaseFactory.getInstance(ctx).getPlayerStats()
        if(player != null) {
            player.nalog += 100
            DatabaseFactory.getInstance(ctx).setPlayerWithProperties(player)
        }
    }

    //УСЛОВИЕ: Если репутация меньше 10 с вероятностью 20% ПОСЛЕДСТВИЯ: Изымается 70% от текушей суммы. И репутация приравнивается 40
    if(DatabaseFactory.getInstance(ctx).getPlayerStats()!!.reputation < 10 && Math.random()<0.2){
        generateFNSControlMessage(ctx)
        var player = DatabaseFactory.getInstance(ctx).getPlayerStats()
        if(player != null) {
            player.money = (player.money*0.3).toInt()
            player.reputation = 40
            DatabaseFactory.getInstance(ctx).setPlayerWithProperties(player)
        }
    }

    //УСЛОВИЕ: Ежедневно с вероятностью 1% ПОСЛЕДСТВИЯ: Сумма денег уменьшена на 300
    if(Math.random() < 0.01){
        var player = DatabaseFactory.getInstance(ctx).getPlayerStats()
        if(player != null) {
            player.money -= 300
            DatabaseFactory.getInstance(ctx).setPlayerWithProperties(player)
        }
    }

    //УСЛОВИЕ: Если деньги отрицательны, то вероятность 5% ПОСЛЕДСТВИЯ: Репутация всегда 0
    if(DatabaseFactory.getInstance(ctx).getPlayerStats()!!.money < 0 && Math.random() < 0.05){
        var player = DatabaseFactory.getInstance(ctx).getPlayerStats()
        if(player != null) {
            player.reputation = 0
            player.money *= 100
            DatabaseFactory.getInstance(ctx).setPlayerWithProperties(player)
        }
    }
}

fun generateCreditBankMessage(credit : Credit_Deposit, ctx : Context){//УСЛОВИЯ: Письмо приходит по истечение 6 месяцев с взятия не погашенного кредита. ПОСЛЕДСТВИЯ: Нет
    var message : Message = Message()
    message.caption = "У вас задолженность"
    message.sender = "Банк"
    message.text = "Здравствуйте.\nБолее ${credit.date[1].toInt()-DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth.toInt()-1} месяцев прошло с момента взятия кредита. Вы должны банку ${credit.amount}$.\nПожалуйста, погасите кредит или нам придется принять меры."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateCollectorMessage(ctx: Context){//УСЛОВИЯ: Письмо приходит по истечение 12 месяцев с взятия не погашенного кредита. ПОСЛЕДСТВИЯ: Погибает один из работников и забираются все средства, достаточные для погашения кредита. Если их не достаточно, то изымаем 80%
    var message : Message = Message()
    message.caption = "Зря не платил!"
    message.sender = "ANONYMOUS"
    message.text = "Шалом, бараноподобные курицы!\nВы не на свой огород заехали. У тебя, симбиоз бабочки с куклой, есть один день, чтобы погасить кредит. Ты вообще о чем думал, когда неоплачивал кредит целый год. У тебя реально перфаратор вместо башки! Если не будет денег. Твоему работничку хана! И твоим деньгам! И лесопилке! И твоим правам, тыква!\n С уважением, Петербургские коллекторы."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateWorkerMessage(ctx: Context){//УСЛОВИЕ: Ежедневно с вероятностью 5% ПОСЛЕДСТВИЯ: Нет
    var message : Message = Message()
    message.caption = "Хазяйна, денег дай. А?"
    message.sender = "АНАНИМ"
    message.text = "Здоровья, вам.\nДеля харошо идют. Только жена моей из Кареи не хватает деньга на пропитание. А я им все моё посылаю. Не погуби. Повысь зарплату. А то придется мне уйти с лесопилки в дом."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateLotoMessage(ctx : Context){//УСЛОВИЕ: Ежедневно с вероятностью 0,5% ПОСЛЕДСТВИЯ: Добавляется 5000$
    var message : Message = Message()
    message.caption = "Вам крупно повезло!"
    message.sender = "Лотерея 'Хобот'"
    message.text = "Здравствуйте.\nПоздравляем! Вы выиграли главный приз нашей лотереи. Это 5000$. Завра вам их пришлют. \nЖелаем успехов."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateUnhappyMessage(ctx : Context){//УСЛОВИЕ: В дни, когда репутация меньше 10 с вероятностью 15% ПОСЛЕДСТВИЯ: GAME OVER
    var message : Message = Message()
    message.caption = "Вам крупно не повезло!"
    message.sender = "Гидрометцентр"
    message.text = "Здравствуйте.\nВ вашу сторону надвигается мощный пожар. Видимо, Бог вас за что-то покарал. Ваша лесопилка наверняка сгорит. А вы останетесь банкротом.\nС уважением судьба."
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateControlMessage(ctx : Context){//УСЛОВИЕ: Верятность 5,1%-(реп)/20 ПОСЛЕДСТВИЯ: ежедневный налог увеличен на 100$
    var message : Message = Message()
    message.caption = "Новый налог"
    message.sender = "Власть"
    message.text = "Все владельцы недвижимости на территории леса, обязаны платить 0,1% от стоимости недвижимости ежедневно.\nВ Вашем случае это 100$"
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateFNSControlMessage(ctx : Context){//УСЛОВИЕ: Если репутация меньше 10 с вероятностью 20% ПОСЛЕДСТВИЯ: Изымается 70% от текушей суммы. И репутация приравнивается 40
    var message : Message = Message()
    message.caption = "Что-то налоги вы не платите"
    message.sender = "ФНС"
    message.text = "Здравствуйте.\nВ казну перестали поступать ваши ежедневные налоги. Нас это насторожило, не случилось ли у вас чего-нибудь. А оказалось у вас все хорошо. За издевательство над нами, мы все-таки нервничали, мы изымаем у вас ${(DatabaseFactory.getInstance(ctx).getPlayerStats()!!.money*0.70).toInt()}$.\nС нами лучше не шутить!"
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateFootballMessage(ctx : Context){//УСЛОВИЕ: Ежедневно с вероятностью 1% ПОСЛЕДСТВИЯ: Сумма денег уменьшена на 300
    var message : Message = Message()
    message.caption = "Любишь футбол?"
    message.sender = "ФNФА"
    message.text = "Привет.\nВ России чемпионат мира по футболу, а ты до сих пор без билета?! Надо это исправить! Ты покупаешь у нас два билета на матч Россия-Германия и без разговоров! Всего за 300$. Матч завтра в Сочи. Чтобы был в 18:00, как штык\n\nP.S.Мы тебя развели как слепого дальнобойщика.\nP.P.S.Россия не будет играть с Германией; хе-хе-хе"
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}

fun generateNegativeMoneyMessage(ctx : Context){//УСЛОВИЕ: Если деньги отрицательны, то вероятность 5% ПОСЛЕДСТВИЯ: Репутация всегда 0
    var message : Message = Message()
    message.caption = "Отрицательные деньги - это нехорошо"
    message.sender = "Разработчики"
    message.text = "От лица разработчиков, которые не захотели прописывать кучу костылей из-за положительности денег, мы высказаваем тебе наше 'фи'. Зря ты нас обидел. Если ты не знал, то программисты самые ранимые люди. И ты нас ранил. Но программисты еще и мстительны. Поэтому твоя репутация теперь у чертика. И упс... Произошел баг и к твоим деньгам приписалось два нолика, а они же отрицательные! И Не надо было нас обижать! Скоро ты обанкротишься\nС любовью команда разработчиков"
    message.date = arrayOf(DatabaseFactory.getInstance(ctx).getDataTime()!!.currentDay, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentMonth, DatabaseFactory.getInstance(ctx).getDataTime()!!.currentYear)
    DatabaseFactory.getInstance(ctx).addMessageWithProperties(message)
}