package stannis.ru.productionsimulator.Functions

import android.content.Context
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Enums.Nations
import stannis.ru.productionsimulator.Enums.Profs
import java.util.*

fun fillDb(ctx: Context) {
    DatabaseFactory.Index(0)
    val ins = DatabaseFactory.getInstance(ctx)
    ins.removeAllLabor()
    ins.removeAllStaff()

    Inventory.getInventory("buy").clear()
    Inventory.getInventory("sell").clear()
    Inventory.getInventory().clear()

    val invent = Inventory.getInventory("buy")
    if (invent != null) {
        invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.SHOVEL.getId()), ItemStack(Items.SHOVEL
                .itemId, 8, invent.getInventoryStackLimit()))
        invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.IRON.getId()), ItemStack(Items.IRON
                .itemId, 3, invent.getInventoryStackLimit()))

        invent.save(ctx)
    }
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
    kek.addNames(arrayNames, arraySecondNames)
    kek.addPlayerStatsWithProperties(1200, 0,0,50,100)
    kek.addDataTimeWithProperties("24", "04", "2014", 0,0,0,0)
}

fun testFillDb(ctx: Context) {

}

