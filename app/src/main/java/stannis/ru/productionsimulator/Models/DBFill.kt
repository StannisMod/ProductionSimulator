package stannis.ru.productionsimulator.Models

import android.content.Context
import stannis.ru.productionsimulator.Items
import stannis.ru.productionsimulator.Nations
import stannis.ru.productionsimulator.Profs
import java.util.*

fun fillDb(ctx: Context) {
    val ins = DatabaseFactory.getInstance(ctx)
    ins.removeAllLabor()
    ins.removeAllStaff()
    ins.removePlayer()
    ins.removeAllCredits()
    ins.removeDataTime()
    Inventory.getInventory("buy").clear()
    Inventory.getInventory("sell").clear()
    Inventory.getInventory().clear()
    ins.removeAllNames()
    val invent = Inventory.getInventory("buy")
    if (invent != null) {
        invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.SHOVEL.getId()), ItemStack(Items.SHOVEL
                .itemId, 8, invent.getInventoryStackLimit()))
        invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.IRON.getId()), ItemStack(Items.IRON
                .itemId, 3, invent.getInventoryStackLimit()))

        invent.save(ctx)
    }
    val arrayNames = arrayOf("Абрам", " Август", " Авдей", " Аверкий", " Адам", " Адриан", " Азарий", " Аким", " Александр", " Алексей", " Амвросий", " Амос", " Ананий", " Анатолий", " Андрей", " Андриан", " Андрон", " Аристарх", " Аркадий", " Арсен", " Арсений", " Артём", " Артемий", " Архип", " Аскольд", " Афанасий", " Афиноген", "Кирилл", " Карл", " Касим", " Кастор", " Касьян", " Каюм", " Кеша", " Кирсан", " Клим", " Кондрат", " Корней", " Корнелий", " Косьма", " Кристиан", " Кузьма",
            "Лавр", " Лаврентий", " Ладимир", " Лазарь", " Леонид", " Леонтий", " Лонгин", " Лука", " Наум", " Нестор", " Нестер", " Никандр", " Никанор", " Никита", " Никифор", " Никодим", " Никола", " Николай", " Никон", " Нил", " Нифонт",

            "Олег", " Оскар", " Остап", " Остромир",

            "Павел", " Панкрат", " Парфений", " Пахом", " Петр", " Пимен", " Платон", " Поликарп", " Порфирий", " Потап", " Пров", " Прокл", " Прокоп", " Прокопий", " Прокофий", " Прохор",

            "Радим", " Радислав", " Радован", " Ратибор", " Ратмир", " Рафаил", " Родион", " Роман", " Ростислав", " Руслан", " Рюрик",

            "Стас", " Савва", " Савелий", " Спартак", " Степан",

            " Тарас", " Твердислав", " Творимир", " Терентий", " Тимофей", " Тимур", " Тит", " Тихон", " Трифон", " Трофим")
    val arraySecondNames = arrayOf("Смирнов", "Иванов", " Кузнецов", " Соколов", " Попов", " Лебедев", " Козлов", " Новиков ", "Морозов ", "Петров ", "Волков ", "Соловьёв ", "Васильев ", "Зайцев ", "Павлов ", "Семёнов ", "Голубев", ""
            , "Виноградов", "Богданов"
            , "Воробьёв"
            , "Фёдоров"
            , "Михайлов"
            , "Беляев"
            , "Тарасов"
            , "Белов"
            , "Комаров"
            , "Орлов"

            , "Веселов"
            , "Филиппов"
            , "Марков"
            , "Большаков"
            , "Суханов"
            , "Миронов"
            , "Ширяев"
            , "Александров"
            , "Коновалов"
            , "Шестаков"
            , "Казаков"
            , "Ефимов"
            , "Денисов"
            , "Громов"
            , "Фомин"
            , "Давыдов"
            , "Мельников"
            , "Щербаков"
            , "Блинов"
            , "Колесников"
            , "Карпов"
            , "Афанасьев"
            , "Власов"
            , "Маслов"
    )
    ins.addNames(arrayNames, arraySecondNames)




    ins.addLaborExchangeWithProperties("${ins.getName()}", 30, Profs.findById(Random().nextInt(6)).getProff(), 10, Nations.findById(2).getNationality(), 5, "01", "01")
    ins.addLaborExchangeWithProperties("${ins.getName()}", 30, Profs.findById(Random().nextInt(6)).getProff(), 10, Nations.findById(3).getNationality(), 5, "01", "02")
    ins.addLaborExchangeWithProperties("${ins.getName()}", 30, Profs.findById(Random().nextInt(6)).getProff(), 10, Nations.findById(5).getNationality(), 5, "01", "03")
    ins.addPlayerStatsWithProperties(500, 0, 0, 50, 0, 100)
    ins.addDataTimeWithProperties("25", "12", "2018", 0, 0, 0, 0)
    ins.added = true

    ins.removeMessage("Здравствуйте.\nВаша лесопилка попала в список предприятий, владельцы которых претендуют на новую печку. Для того чтобы получить печку, вам надо взять кредит на сумму 10$. Чтобы мы были уверены в вашей финансовой состоятельности и не оказалось, что вы просто жулик\nС уважением комиссия лото 'ТОТО'".hashCode())
    ins.removeMessage("Если хочешь увидеть своего брата живым, то положи на свой счет 1000000$ и передай нам номер этого счета. Номер счета напиши на бумажке положи в холодильник на складе и чтобы все покинули лесопилку до 31 июня! Всех кто завтра будет на лесопилке. Убьём!!!".hashCode())
    ins.removeMessage("Здраствуйте.\nМы сообщаем Вам, что теперь всем владельцам лесопилок и других лесных сооруженний, нужно платить налог на сохранение лесов, в размере 1% от стоимости недвижимости на територии леса.".hashCode())
    ins.removeMessage("Здраствуйте, Кирилл Юрьевич.\nВаша задолженность банку составляет 5000$. Просим Вас до 31.12.2018 выплатить задолженность, иначе нам придется заблокировать ваш счет и забрать вашу лесопилку.\nС любовью банк!".hashCode())

    ins.addMessageWithProperties(Message(caption = "Вы выграли приз!", sender = "Лото 'ТОТО'", text = "Здравствуйте.\nВаша лесопилка попала в список предприятий, владельцы которых претендуют на новую печку. Для того чтобы получить печку, вам надо взять кредит на сумму 10$. Чтобы мы были уверены в вашей финансовой состоятельности и не оказалось, что вы просто жулик\nС уважением комиссия лото 'ТОТО'", date = arrayOf(ins.getDataTime()!!.currentDay, ins.getDataTime()!!.currentMonth, ins.getDataTime()!!.currentYear)))
    ins.addMessageWithProperties(Message(sender = "ANONYMOUS", caption = "Твой брат у нас!", text = "Если хочешь увидеть своего брата живым, то положи на свой счет 1000000$ и передай нам номер этого счета. Номер счета напиши на бумажке положи в холодильник на складе и чтобы все покинули лесопилку до 31 июня! Всех кто завтра будет на лесопилке. Убьём!!!", date = arrayOf(ins.getDataTime()!!.currentDay, ins.getDataTime()!!.currentMonth, ins.getDataTime()!!.currentYear)))
    ins.addMessageWithProperties(Message(sender = "Власть", caption = "Новый закон о налогообложении", text = "Здраствуйте.\n" +
            "Мы сообщаем Вам, что теперь всем владельцам лесопилок и других лесных сооруженний, нужно платить налог на сохранение лесов, в размере 1% от стоимости недвижимости на територии леса.", date = arrayOf(ins.getDataTime()!!.currentDay, ins.getDataTime()!!.currentMonth, ins.getDataTime()!!.currentYear)))
    ins.addMessageWithProperties(Message(date = arrayOf(ins.getDataTime()!!.currentDay, ins.getDataTime()!!.currentMonth, ins.getDataTime()!!.currentYear)))

}

