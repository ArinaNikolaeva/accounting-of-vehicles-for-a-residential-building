package ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ui.management.CarManagementActivity
import com.example.myapplication.R
import ui.management.UserManagementActivity

class MainScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        // Получаем ссылки на кнопки
        val buttonCars: Button = findViewById(R.id.buttonCars)
        //val buttonResidents: Button = findViewById(R.id.buttonResidents)
        val buttonUsers: Button = findViewById(R.id.buttonUsers)
        //val buttonGeneralList: Button = findViewById(R.id.buttonGeneralList)

        // Устанавливаем обработчики нажатий для кнопок
        buttonCars.setOnClickListener {
            Toast.makeText(this, "Открытие экрана 'Авто'", Toast.LENGTH_SHORT).show()
            // Создаем Intent для перехода в CarManagementActivity
            val intent = Intent(this, CarManagementActivity::class.java)

            // Запускаем активность
            startActivity(intent)
        }


        buttonUsers.setOnClickListener {
            Toast.makeText(this, "Открытие экрана 'Пользователи'", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
        }

    }
}
