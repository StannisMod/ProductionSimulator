package stannis.ru.productionsimulator.Enums

enum class Profs(val prof: String, val averSalary: Int) {
    GENERAL_WORKER("Разнорабочий", 3),
    FOREMAN("Прораб", 6),
    MANAGER("Мэнеджер", 15);


    companion object {


        fun findById(id: Int): Profs {
            when (id) {
                0 -> return GENERAL_WORKER
                1 -> return FOREMAN
                2 -> return MANAGER

            }
            return GENERAL_WORKER
        }
    }
}