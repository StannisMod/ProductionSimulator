package stannis.ru.productionsimulator

enum class Nations(val nation: String, val quality: Int) {
    RUS("россиянин", 10),
    BEL("белорус", 12),
    UKR("украинец", 13),
    KZH("казах", 20);

    fun getNationality(): String = nation
    fun getAvQuality(): Int = quality

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