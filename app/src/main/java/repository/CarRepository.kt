package repository

import model.CarInfo

object CarRepository {
    // Список машин
    val carList = mutableListOf<CarInfo>(
        CarInfo("A123BC", "Toyota", "Белый", "Иванов Иван", "+7 (900) 123-45-67", "12"),
        CarInfo("B456DE", "Honda", "Красный", "Петров Петр", "+7 (900) 234-56-78", "34"),
        CarInfo("C789FG", "Nissan", "Синий", "Сидоров Сидор", "+7 (900) 345-67-89", "56")
    )

    // Метод для добавления новой машины
    fun addCar(car: CarInfo) {
        carList.add(car)
    }

    // Метод для редактирования существующей машины
    fun updateCar(oldCar: CarInfo, newCar: CarInfo) {
        val index = carList.indexOf(oldCar)
        if (index != -1) {
            carList[index] = newCar
        }
    }
}

