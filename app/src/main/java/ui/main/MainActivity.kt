package ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получаем кнопку и назначаем обработчик
        val loginButton: Button = findViewById(R.id.btnLogin)
        loginButton.setOnClickListener {
            // Переход к следующей активности
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
