package binar.ganda.notesapp.local.user

import androidx.room.*
import binar.ganda.notesapp.local.notes.Notes


@Dao
interface UserDao {

    @Insert
    fun registerUser(user : User) : Long

    @Query("SELECT * FROM User WHERE User.username = :username AND User.password = :password")
    fun getUserInfo(username : String, password : String) : String

    //getting user data details
    @Query("select * from User where id =:id")
    fun getUserDataDetails(id:Long): User

    @Delete
    fun deleteUser(user : User) : Int

    @Update
    fun updateUser(user: User) : Int


}