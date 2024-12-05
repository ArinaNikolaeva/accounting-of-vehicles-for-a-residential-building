package ui.management

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.myapplication.R
import model.CarInfo
import repository.CarRepository
import repository.UserRepository


class CarManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_management)

        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val searchViewCar = findViewById<SearchView>(R.id.searchView)
        // Заполнение таблицы данными
        populateTable(CarRepository.carList, tableLayout)

        // Обработчик для поиска
        searchViewCar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Фильтрация списка машин по номеру
                val filteredList = CarRepository.carList.filter { it.carNumber.contains(newText ?: "", ignoreCase = true) }
                populateTable(filteredList, tableLayout) // Обновляем таблицу
                return true
            }
        })

        // Обработчик для кнопки "Добавить"
        btnAdd.setOnClickListener {
            showAddEditDialog(isEditMode = false, car = null)
        }

        // Обработчик для кнопки "Удалить"
        btnDelete.setOnClickListener {
            showDeleteDialog()
        }
    }

    // Метод для отображения диалога удаления
    private fun showDeleteDialog() {
        val carNumbers = CarRepository.carList.map { it.carNumber }.toTypedArray()

        if (carNumbers.isEmpty()) {
            Toast.makeText(this, "Список автомобилей пуст, нечего удалять.", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Выберите автомобиль для удаления")
            .setItems(carNumbers) { dialog, which ->
                val selectedCarNumber = carNumbers[which]
                val carToRemove = CarRepository.carList.find { it.carNumber == selectedCarNumber }
                if (carToRemove != null) {
                    CarRepository.carList.remove(carToRemove)
                    populateTable(CarRepository.carList, findViewById(R.id.tableLayout))
                    Toast.makeText(this, "Машина $selectedCarNumber удалена.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showAddEditDialog(isEditMode: Boolean, car: CarInfo?) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_car, null)
        builder.setView(dialogView)

        val carNumberEditText = dialogView.findViewById<EditText>(R.id.editTextCarNumber)
        val brandEditText = dialogView.findViewById<EditText>(R.id.editTextBrand)
        val colorEditText = dialogView.findViewById<EditText>(R.id.editTextColor)
        val ownerEditText = dialogView.findViewById<EditText>(R.id.editTextOwner)
        val phoneEditText = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val apartmentEditText = dialogView.findViewById<EditText>(R.id.editTextApartment)

        if (isEditMode && car != null) {
            carNumberEditText.setText(car.carNumber)
            brandEditText.setText(car.brand)
            colorEditText.setText(car.color)
            ownerEditText.setText(car.owner)
            phoneEditText.setText(car.phone)
            apartmentEditText.setText(car.apartment)
        }

        builder.setPositiveButton(if (isEditMode) "Сохранить" else "Добавить") { _, _ ->
            val carNumber = carNumberEditText.text.toString().trim()
            val brand = brandEditText.text.toString().trim()
            val color = colorEditText.text.toString().trim()
            val owner = ownerEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val apartment = apartmentEditText.text.toString().trim()

            // Проверка на пустые поля
            if (carNumber.isEmpty() || brand.isEmpty() || color.isEmpty() ||
                owner.isEmpty() || phone.isEmpty() || apartment.isEmpty()
            ) {
                // Сообщение об ошибке
                Toast.makeText(
                    this,
                    "Все поля должны быть заполнены!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setPositiveButton
            }

            val updatedCar = CarInfo(carNumber, brand, color, owner, phone, apartment)

            if (isEditMode && car != null) {
                CarRepository.updateCar(car, updatedCar)
            } else {
                CarRepository.addCar(updatedCar)
            }

            populateTable(CarRepository.carList, findViewById(R.id.tableLayout))
        }

        builder.setNegativeButton("Отмена", null)
        builder.show()
    }

//метод для заполнения таблицы
    private fun populateTable(carList: List<CarInfo>, tableLayout: TableLayout) {
        tableLayout.removeAllViews()
        for (car in carList) {
            val row = TableRow(this)
            val carNumberTextView = TextView(this)
            carNumberTextView.text = car.carNumber

            val brandTextView = TextView(this)
            brandTextView.text = car.brand

            val colorTextView = TextView(this)
            colorTextView.text = car.color

            row.addView(carNumberTextView)
            row.addView(brandTextView)
            row.addView(colorTextView)

            val editButton = Button(this)
            editButton.text = "✎"
            editButton.setOnClickListener {
                showAddEditDialog(isEditMode = true, car = car)
            }
            row.addView(editButton)

            val infoButton = Button(this)
            infoButton.text = "Инфо"
            infoButton.setOnClickListener {
                showCarInfoDialog(
                    owner = car.owner,
                    phone = car.phone,
                    apartment = car.apartment,
                    carNumber = car.carNumber,
                    brand = car.brand,
                    color = car.color
                )
            }

            row.addView(infoButton)
            tableLayout.addView(row)
        }
    }

    private fun showCarInfoDialog(
        owner: String,
        phone: String,
        apartment: String,
        carNumber: String,
        brand: String,
        color: String
    ) {
        val message = """
            Владелец: $owner
            Телефон: $phone
            Квартира: $apartment
            Номер машины: $carNumber
            Марка: $brand
            Цвет: $color
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Информация о машине")
            .setMessage(message)
            .setPositiveButton("Закрыть") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}


//class CarManagementActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_car_management)
//
//        // Получаем ссылки на элементы
//        val searchView = findViewById<SearchView>(R.id.searchView)
//        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
//        val btnAdd = findViewById<Button>(R.id.btnAdd)
//
//        // Заполняем таблицу данными
//        populateTable(CarRepository.carList, tableLayout)
//
//        // Обработчик для поиска
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                // Фильтрация списка машин по номеру
//                val filteredList = CarRepository.carList.filter { it.carNumber.contains(newText ?: "", ignoreCase = true) }
//                populateTable(filteredList, tableLayout) // Обновляем таблицу
//                return true
//            }
//        })
//
//        // Обработчик нажатия кнопки "Добавить"
//        btnAdd.setOnClickListener {
//            showAddEditDialog()
//        }
//    }
//
//    // Функция для добавления данных в таблицу
//    private fun populateTable(carList: List<CarInfo>, tableLayout: TableLayout) {
//        tableLayout.removeAllViews() // Очищаем таблицу перед добавлением новых данных
//        for (car in carList) {
//            val row = TableRow(this)
//            val carNumberTextView = TextView(this)
//            carNumberTextView.text = car.carNumber
//
//            val brandTextView = TextView(this)
//            brandTextView.text = car.brand
//
//            val colorTextView = TextView(this)
//            colorTextView.text = car.color
//
//            row.addView(carNumberTextView)
//            row.addView(brandTextView)
//            row.addView(colorTextView)
//
//            // Добавляем кнопку для каждого ряда
//            val infoButton = Button(this)
//            infoButton.text = "Инфо"
//            infoButton.setOnClickListener {
//                showCarInfoDialog(
//                    owner = "Владелец: Иванов Иван",
//                    phone = "+7 (900) 123-45-67",
//                    apartment = "12",
//                    carNumber = car.carNumber,
//                    brand = car.brand,
//                    color = car.color
//                )
//            }
//
//            row.addView(infoButton)
//            tableLayout.addView(row)
//        }
//    }
//
//    // Функция для отображения информации о машине
//    private fun showCarInfoDialog(
//        owner: String,
//        phone: String,
//        apartment: String,
//        carNumber: String,
//        brand: String,
//        color: String
//    ) {
//        val message = """
//            Владелец: $owner
//            Телефон: $phone
//            Квартира: $apartment
//            Номер машины: $carNumber
//            Марка: $brand
//            Цвет: $color
//        """.trimIndent()
//
//        AlertDialog.Builder(this)
//            .setTitle("Информация о машине")
//            .setMessage(message)
//            .setPositiveButton("Закрыть") { dialog, _ -> dialog.dismiss() }
//            .show()
//    }
//
//    // Функция для отображения диалога добавления/редактирования машины
//    private fun showAddEditDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_add_car, null)
//
//        val ownerEditText = dialogView.findViewById<EditText>(R.id.ownerEditText)
//        val phoneEditText = dialogView.findViewById<EditText>(R.id.phoneEditText)
//        val apartmentEditText = dialogView.findViewById<EditText>(R.id.apartmentEditText)
//        val carNumberEditText = dialogView.findViewById<EditText>(R.id.carNumberEditText)
//        val brandEditText = dialogView.findViewById<EditText>(R.id.brandEditText)
//        val colorEditText = dialogView.findViewById<EditText>(R.id.colorEditText)
//
//        val builder = AlertDialog.Builder(this)
//            .setTitle("Добавить машину")
//            .setView(dialogView)
//            .setPositiveButton("Сохранить") { _, _ ->
//                val owner = ownerEditText.text.toString()
//                val phone = phoneEditText.text.toString()
//                val apartment = apartmentEditText.text.toString()
//                val carNumber = carNumberEditText.text.toString()
//                val brand = brandEditText.text.toString()
//                val color = colorEditText.text.toString()
//
//                val newCar = CarInfo(carNumber, brand, color)
//                CarRepository.carList.add(newCar)
//                populateTable(CarRepository.carList, findViewById(R.id.tableLayout))
//            }
//            .setNegativeButton("Отмена", null)
//
//        builder.show()
//    }
//}
