package stannis.ru.productionsimulator.Functions

import android.content.Context
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Models.Credit_Deposit
import stannis.ru.productionsimulator.Models.DataTime
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Models.Player
import java.util.*

fun generateMessage(ctx: Context){
    var player = Player.getInstance(ctx)
    var r : Random = Random()
    //УСЛОВИЯ: Письмо приходит по истечение 6 месяцев с взятия не погашенного кредита. ПОСЛЕДСТВИЯ: Нет
    val list = PlayerStatsDatabase.getInstance(ctx).getListOfCreditDeposit()
    for (crDep in list) {
        if(crDep.type == 2 && crDep.date[0].toInt() == DataTime.getInstance(ctx).currentDay.toInt()){
            if((DataTime.getInstance(ctx).currentYear.toInt()-crDep.date[2].toInt())*12 + DataTime.getInstance(ctx).currentMonth.toInt() - crDep.date[1].toInt()>= 6){
                generateCreditBankMessage(crDep, ctx)
            }
        }
    }

    //УСЛОВИЯ: Письмо приходит по истечение 12 месяцев с взятия не погашенного кредита. ПОСЛЕДСТВИЯ: Погибает один из работников и забираются все средства, достаточные для погашения кредита. Если их не достаточно, то изымаем 80%
    for (crDep in list) {
        if(crDep.type == 2 && crDep.date[0].toInt() == DataTime.getInstance(ctx).currentDay.toInt()){
            if((DataTime.getInstance(ctx).currentYear.toInt()-crDep.date[2].toInt())*12 + DataTime.getInstance(ctx).currentMonth.toInt() - crDep.date[1].toInt()>= 12){
                generateCollectorMessage(ctx)
            }
        }
    }

    //УСЛОВИЕ: Ежедневно с вероятностью 5% ПОСЛЕДСТВИЯ: Нет
    if(r.nextDouble() < 0.05){
        generateWorkerMessage(ctx)
    }

    //УСЛОВИЕ: Ежедневно с вероятностью 0,5% ПОСЛЕДСТВИЯ: Добавляется 5000$
    if(r.nextDouble() < 0.005){
        generateLotoMessage(ctx)

        if(player != null) {
            player.money += 5000
            
        }
    }

    //УСЛОВИЕ: В дни, когда репутация меньше 10 с вероятностью 15% ПОСЛЕДСТВИЯ: GAME OVER
    if(player.reputation < 10 && r.nextDouble() < 0.15){
        gameover(ctx)
    }

    //УСЛОВИЕ: Верятность 5,1%-(реп)/20 ПОСЛЕДСТВИЯ: ежедневный налог увеличен на 100$
    if(r.nextDouble() < 0.051- player.reputation/2000){
        generateControlMessage(ctx)
        var player = player
        if(player != null) {
            player.nalog += 100
            
        }
    }

    //УСЛОВИЕ: Если репутация меньше 10 с вероятностью 10% ПОСЛЕДСТВИЯ: Изымается 70% от текушей суммы. И репутация приравнивается 40
    if(player.reputation < 10 && r.nextDouble()<0.1){
        generateFNSControlMessage(ctx)
        var player = player
        if(player != null) {
            player.money = (player.money*0.3).toInt()
            player.reputation = 40
            
        }
    }

    //УСЛОВИЕ: Ежедневно с вероятностью 1% ПОСЛЕДСТВИЯ: Сумма денег уменьшена на 300
    if(r.nextDouble() < 0.01){
        var player = player
        if(player != null) {
            player.money -= 300
            
        }
    }

    //УСЛОВИЕ: Если деньги отрицательны, то вероятность 5% ПОСЛЕДСТВИЯ: Репутация всегда 0
    if(player.money < 0 && r.nextDouble() < 0.05){

        if(player != null) {
            player.reputation = 0
            player.money *= 100
            
        }
    }
}

fun generateCreditBankMessage(credit : Credit_Deposit, ctx : Context){//УСЛОВИЯ: Письмо приходит по истечение 6 месяцев с взятия не погашенного кредита. ПОСЛЕДСТВИЯ: Нет
    var message : Message = Message()
    message.caption = "У вас задолженность"
    message.sender = "Банк"
    message.text = "Здравствуйте.\nБолее ${credit.date[1].toInt()- DataTime.getInstance(ctx).currentMonth.toInt()-1} месяцев прошло с момента взятия кредита. Вы должны банку ${credit.amount}$.\nПожалуйста, погасите кредит или нам придется принять меры."
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}

fun generateCollectorMessage(ctx: Context){//УСЛОВИЯ: Письмо приходит по истечение 12 месяцев с взятия не погашенного кредита. ПОСЛЕДСТВИЯ: Погибает один из работников и забираются все средства, достаточные для погашения кредита. Если их не достаточно, то изымаем 80%
    var message : Message = Message()
    message.caption = "Зря не платил!"
    message.sender = "ANONYMOUS"
    message.text = "Шалом, бараноподобные курицы!\nВы не на свой огород заехали. У тебя, симбиоз бабочки с куклой, есть один день, чтобы погасить кредит. Ты вообще о чем думал, когда неоплачивал кредит целый год. У тебя реально перфаратор вместо башки! Если не будет денег. Твоему работничку хана! И твоим деньгам! И лесопилке! И твоим правам, тыква!\n С уважением, Петербургские коллекторы."
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}

fun generateWorkerMessage(ctx: Context){//УСЛОВИЕ: Ежедневно с вероятностью 5% ПОСЛЕДСТВИЯ: Нет
    var message : Message = Message()
    message.caption = "Хазяйна, денег дай. А?"
    message.sender = "АНАНИМ"
    message.text = "Здоровья, вам.\nДеля харошо идют. Только жена моей из Кареи не хватает деньга на пропитание. А я им все моё посылаю. Не погуби. Повысь зарплату. А то придется мне уйти с лесопилки в дом."
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}

fun generateLotoMessage(ctx : Context){//УСЛОВИЕ: Ежедневно с вероятностью 0,5% ПОСЛЕДСТВИЯ: Добавляется 5000$
    var message : Message = Message()
    message.caption = "Вам крупно повезло!"
    message.sender = "Лотерея 'Хобот'"
    message.text = "Здравствуйте.\nПоздравляем! Вы выиграли главный приз нашей лотереи. Это 5000$. Завра вам их пришлют. \nЖелаем успехов."
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}

fun generateUnhappyMessage(ctx : Context){//УСЛОВИЕ: В дни, когда репутация меньше 10 с вероятностью 15% ПОСЛЕДСТВИЯ: GAME OVER
    var message : Message = Message()
    message.caption = "Вам крупно не повезло!"
    message.sender = "Гидрометцентр"
    message.text = "Здравствуйте.\nВ вашу сторону надвигается мощный пожар. Видимо, Бог вас за что-то покарал. Ваша лесопилка наверняка сгорит. А вы останетесь банкротом.\nС уважением судьба."
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}

fun generateControlMessage(ctx : Context){//УСЛОВИЕ: Верятность 5,1%-(реп)/20 ПОСЛЕДСТВИЯ: ежедневный налог увеличен на 100$
    var message : Message = Message()
    message.caption = "Новый налог"
    message.sender = "Власть"
    message.text = "Все владельцы недвижимости на территории леса, обязаны платить 0,1% от стоимости недвижимости ежедневно.\nВ Вашем случае это 100$"
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}

fun generateFNSControlMessage(ctx : Context){//УСЛОВИЕ: Если репутация меньше 10 с вероятностью 20% ПОСЛЕДСТВИЯ: Изымается 70% от текушей суммы. И репутация приравнивается 40
    var message : Message = Message()
    message.caption = "Что-то налоги вы не платите"
    message.sender = "ФНС"
    message.text = "Здравствуйте.\nВ казну перестали поступать ваши ежедневные налоги. Нас это насторожило, не случилось ли у вас чего-нибудь. А оказалось у вас все хорошо. За издевательство над нами, мы все-таки нервничали, мы изымаем у вас ${(Player.getInstance(ctx).money*0.70).toInt()}$.\nС нами лучше не шутить!"
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}

fun generateFootballMessage(ctx : Context){//УСЛОВИЕ: Ежедневно с вероятностью 1% ПОСЛЕДСТВИЯ: Сумма денег уменьшена на 300
    var message : Message = Message()
    message.caption = "Любишь футбол?"
    message.sender = "ФNФА"
    message.text = "Привет.\nВ России чемпионат мира по футболу, а ты до сих пор без билета?! Надо это исправить! Ты покупаешь у нас два билета на матч Россия-Германия и без разговоров! Всего за 300$. Матч завтра в Сочи. Чтобы был в 18:00, как штык\n\nP.S.Мы тебя развели как слепого дальнобойщика.\nP.P.S.Россия не будет играть с Германией; хе-хе-хе"
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}

fun generateNegativeMoneyMessage(ctx : Context){//УСЛОВИЕ: Если деньги отрицательны, то вероятность 5% ПОСЛЕДСТВИЯ: Репутация всегда 0
    var message : Message = Message()
    message.caption = "Отрицательные деньги - это нехорошо"
    message.sender = "Разработчики"
    message.text = "От лица разработчиков, которые не захотели прописывать кучу костылей из-за положительности денег, мы высказаваем тебе наше 'фи'. Зря ты нас обидел. Если ты не знал, то программисты самые ранимые люди. И ты нас ранил. Но программисты еще и мстительны. Поэтому твоя репутация теперь у чертика. И упс... Произошел баг и к твоим деньгам приписалось два нолика, а они же отрицательные! И Не надо было нас обижать! Скоро ты обанкротишься\nС любовью команда разработчиков"
    message.date = arrayOf(DataTime.getInstance(ctx).currentDay, DataTime.getInstance(ctx).currentMonth, DataTime.getInstance(ctx).currentYear)
    PlayerStatsDatabase.getInstance(ctx).addMessageWithProperties(message)
}