package ui.user

import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.myapplication.R
import model.CarInfo
import repository.CarRepository

class UserScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_screen)

        val searchView = findViewById<SearchView>(R.id.searchView)
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        // Используем данные из CarRepository
        populateTable(CarRepository.carList, tableLayout)

        // Обработчик для поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
    }

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

            val infoButton = Button(this)
            infoButton.text = "Инфо"
            infoButton.setOnClickListener {
                showCarInfoDialog(
                    owner = "Владелец: Иванов Иван",
                    phone = "+7 (900) 123-45-67",
                    apartment = "12",
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

