package ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ui.main.MainScreenActivity
import com.example.myapplication.R
import ui.user.UserScreenActivity
import repository.UserRepository

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val editLogin = findViewById<EditText>(R.id.editLogin)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val login = editLogin.text.toString().trim()
            val password = editPassword.text.toString().trim()

            // Проверка логина и пароля
            val user = UserRepository.getUsers().find { it.login == login && it.password == password }

            if (user != null) {
                // Успешный вход
                Toast.makeText(this, "Добро пожаловать, $login!", Toast.LENGTH_SHORT).show()

                // Проверка роли пользователя и переход на нужный экран
                when (user?.role) {
                    "Администратор" -> {
                        // Переход на экран для админа
                        val intent = Intent(this, MainScreenActivity::class.java)
                        startActivity(intent)
                    }
                    "Пользователь" -> {
                        // Переход на экран для пользователя
                        val intent = Intent(this, UserScreenActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {
                        Toast.makeText(this, "Неизвестная роль пользователя", Toast.LENGTH_SHORT).show()
                    }
                }
                finish()
            } else {
                // Ошибка входа
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
