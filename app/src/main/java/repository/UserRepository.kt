package repository
import model.UserInfo

object UserRepository {
    private val users = mutableListOf<UserInfo>(
        UserInfo("admin", "admin123", "Администратор"),
        UserInfo("user1", "password1", "Пользователь")
    )

    fun getUsers(): List<UserInfo> = users

    fun addUser(user: UserInfo) {
        users.add(user)
    }

    fun removeUser(user: UserInfo) {
        users.remove(user)
    }
}

