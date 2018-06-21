package stannis.ru.productionsimulator.Functions

import android.content.ClipData
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Enums.Nations
import stannis.ru.productionsimulator.Enums.Profs
import stannis.ru.productionsimulator.Models.Factory
import java.util.*

fun fillDb(ctx: Context) {
    Inventory.setBegin()
    for (i in 0 until EnumFactory.getSize()) {
        DatabaseFactory.index = i
        val ins = DatabaseFactory.getInstance(ctx)
        Inventory.setNulls()
        ins.removeInventory("buy")
        ins.removeInventory("PlayerInv")
        ins.removeInventory("sell")
        ins.removeFactory(i)
        ins.removeAllLabor()
        ins.removeAllStaff()
    }
    DatabaseFactory.index = 0
    Inventory.getInventory("sell")
    Inventory.getInventory("buy")
    Inventory.getInventory()
    Factory(true, 0,true, EnumFactory.SAWMILL.price,EnumFactory.SAWMILL)
    Factory.saveFactories(ctx)
    Inventory.saveInventories(ctx)

    val arrayNames = arrayOf(/*"Абрам", " Август", " Авдей", " Аверкий", " Адам", " Адриан", " Азарий", " Аким", " Александр", " Алексей", " Амвросий", " Амос", " Ананий", " Анатолий", " Андрей", " Андриан", " Андрон", " Аристарх", " Аркадий", " Арсен", " Арсений", " Артём", " Артемий", " Архип", " Аскольд", " Афанасий", " Афиноген", "Кирилл", " Карл", " Касим", " Кастор", " Касьян", " Каюм", " Кеша", " Кирсан", " Клим", " Кондрат", " Корней", " Корнелий", " Косьма", " Кристиан", " Кузьма",
            "Лавр", " Лаврентий", " Ладимир", " Лазарь", " Леонид", " Леонтий", " Лонгин", " Лука", " Наум", " Нестор", " Нестер", " Никандр", " Никанор", " Никита", " Никифор", " Никодим", " Никола", " Николай", " Никон", " Нил", " Нифонт",

            "Олег", " Оскар", " Остап", " Остромир",

            "Павел", " Панкрат", " Парфений", " Пахом", " Петр", " Пимен", " Платон", " Поликарп", " Порфирий", " Потап", " Пров", " Прокл", " Прокоп", " Прокопий", " Прокофий", " Прохор",

            "Радим", " Радислав", " Радован", " Ратибор", " Ратмир", " Рафаил", " Родион", " Роман", " Ростислав", " Руслан", " Рюрик",

            "Стас", " Савва", " Савелий", " Спартак", " Степан",*/

            " Тарас", " Твердислав", " Творимир", " Терентий", " Тимофей", " Тимур", " Тит", " Тихон", " Трифон", " Трофим")
    val arraySecondNames = arrayOf(/*"Смирнов", "Иванов", " Кузнецов", " Соколов", " Попов", " Лебедев", " Козлов", " Новиков ", "Морозов ", "Петров ", "Волков ", "Соловьёв ", "Васильев ", "Зайцев ", "Павлов ", "Семёнов ", "Голубев", ""
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
            , */"Щербаков"
            , "Блинов"
            , "Колесников"
            , "Карпов"
            , "Афанасьев"
            , "Власов"
            , "Маслов"
    )

    val kek = PlayerStatsDatabase.getInstance(ctx)
    kek.removeAllCredits()
    kek.removeDataTime()
    kek.removePlayer()
    kek.removeAllNames()
    kek.removeMoneyForDay()
    kek.removeAllMessage()
    kek.removeAllMessageReaded()
    kek.addNames(arrayNames, arraySecondNames)
    kek.addPlayerStatsWithProperties(400, 0, 0, 50, 5)
    val data = java.util.Calendar.getInstance()
    var day = data.get(Calendar.DAY_OF_MONTH).toString()
    if (data.get(Calendar.DAY_OF_MONTH) < 10) {
        day = "0${day}"
    }
    var month = (data.get(Calendar.MONTH)+1).toString()
    if (data.get(Calendar.MONTH) < 10) {
        month = "0${month}"
    }
    kek.addDataTimeWithProperties(day, month, data.get(Calendar.YEAR).toString(), 0, 0)
    kek.addMoneyForDay(0, 0)
}



