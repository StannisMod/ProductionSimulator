package stannis.ru.productionsimulator.Enums

enum class Profs(val prof: String, val averSalary: Int) {
    TOK("Токарь", 10),
    SLES("Слесарь", 8),
    TOCH("Точильщик", 3),
    LUD("Лудильщик", 12),
    MAN("Менеджер", 20);

    fun getProff(): String = prof
    fun getSalary(): Int = averSalary

    companion object {


        fun findById(id: Int): Profs {
            when (id) {
                0 -> return TOK
                1 -> return SLES
                2 -> return TOCH
                3 -> return TOCH
                4 -> return LUD
                5 -> return MAN

            }
            return TOK
        }
    }
}