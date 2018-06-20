package stannis.ru.productionsimulator.Enums

enum class Nations(val nation: String, val quality: Int) {
    RUS("русский", 10),
    BEL("белорус", 12),
    UKR("украинец", 13),
    KZH("казах", 20);

    companion object {


        fun findById(id: Int): Nations {
            when (id) {
                0 -> RUS
                1 -> BEL
                2 -> UKR
                3 -> KZH
            }
            return RUS
        }
    }
}