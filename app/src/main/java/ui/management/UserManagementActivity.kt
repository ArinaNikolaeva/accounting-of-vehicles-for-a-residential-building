package ui.management

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.myapplication.R
import model.UserInfo
import repository.UserRepository

class UserManagementActivity : AppCompatActivity() {

    private lateinit var searchViewUsers: SearchView
    private lateinit var tableLayoutUsers: TableLayout
    private lateinit var btnAddUser: Button
    private lateinit var btnDeleteUser: Button

    private val userRepository = UserRepository // Подключаем репозиторий пользователей

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        // Получаем ссылки на элементы
        searchViewUsers = findViewById(R.id.searchViewUsers)
        tableLayoutUsers = findViewById(R.id.tableLayoutUsers)
        btnAddUser = findViewById(R.id.btnAddUser)
        btnDeleteUser = findViewById(R.id.btnDeleteUser)

        // Заполняем таблицу пользователями
        populateUserTable(userRepository.getUsers())

        // Добавляем обработчик для поиска

        searchViewUsers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredUsers = UserRepository.getUsers().filter {
                    it.login.contains(newText ?: "", ignoreCase = true)
                }
                populateUserTable(filteredUsers)
                return true
            }
        })

        // Обработчик для кнопки добавления пользователя
        btnAddUser.setOnClickListener {
            showAddUserDialog { login, password, role ->
                userRepository.addUser(UserInfo(login, password, role))
                populateUserTable(userRepository.getUsers())
            }
        }

        // Обработчик для кнопки удаления пользователя
        btnDeleteUser.setOnClickListener {
            showDeleteUserDialog()
        }
    }

    private fun populateUserTable(users: List<UserInfo>) {
        tableLayoutUsers.removeAllViews() // Очищаем таблицу

        val headerRow = TableRow(this)
        val loginHeader = TextView(this).apply { text = "Логин" }
        val passwordHeader = TextView(this).apply { text = "Пароль" }
        val roleHeader = TextView(this@UserManagementActivity).apply { text = "Роль" }

        headerRow.addView(loginHeader)
        headerRow.addView(passwordHeader)
        headerRow.addView(roleHeader)
        tableLayoutUsers.addView(headerRow)

        for (user in users) {
            val row = TableRow(this)
            val loginTextView = TextView(this).apply { text = user.login }
            val passwordTextView = TextView(this).apply { text = user.password }
            val roleTextView = TextView(this@UserManagementActivity).apply { text = user.role }

            row.addView(loginTextView)
            row.addView(passwordTextView)
            row.addView(roleTextView)
            tableLayoutUsers.addView(row)
        }
    }

    private fun showAddUserDialog(onUserAdded: (login: String, password: String, role: String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_user, null)

        val editLogin = dialogView.findViewById<EditText>(R.id.editLogin)
        val editPassword = dialogView.findViewById<EditText>(R.id.editPassword)
        val spinnerRole = dialogView.findViewById<Spinner>(R.id.spinnerRole)
        val btnSaveUser = dialogView.findViewById<Button>(R.id.btnSaveUser)

        val roles = arrayOf("Пользователь", "Администратор")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        val builder = AlertDialog.Builder(this)
            .setTitle("Добавить пользователя")
            .setView(dialogView)
            .setCancelable(true)

        val dialog = builder.create()

        btnSaveUser.setOnClickListener {
            val login = editLogin.text.toString()
            val password = editPassword.text.toString()
            val role = spinnerRole.selectedItem.toString()

            if (login.isNotEmpty() && password.isNotEmpty()) {
                onUserAdded(login, password, role)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun showDeleteUserDialog() {
        val userLogins = userRepository.getUsers().map { it.login }.toTypedArray()

        if (userLogins.isEmpty()) {
            Toast.makeText(this, "Список пользователей пуст, нечего удалять.", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Выберите пользователя для удаления")
            .setItems(userLogins) { dialog, which ->
                val selectedUserLogin = userLogins[which]
                val userToRemove = userRepository.getUsers().find { it.login == selectedUserLogin }
                if (userToRemove != null) {
                    userRepository.removeUser(userToRemove)
                    populateUserTable(userRepository.getUsers())
                    Toast.makeText(this, "Пользователь $selectedUserLogin удален.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
