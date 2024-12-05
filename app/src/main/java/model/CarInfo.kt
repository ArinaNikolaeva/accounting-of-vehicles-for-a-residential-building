package model
data class CarInfo(
    val carNumber: String,  //номер машины
    val brand: String,      //марка
    val color: String,      //цвет
    var owner: String,      // Владелец
    var phone: String,      // Телефон
    var apartment: String   // Квартира
)

