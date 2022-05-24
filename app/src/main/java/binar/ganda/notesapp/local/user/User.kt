package binar.ganda.notesapp.local.user

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true) var id : Int?,
    @ColumnInfo(name = "username") var username : String?,
    @ColumnInfo(name = "name") var name : String?,
    @ColumnInfo(name = "password") var password : String?,
) : Parcelable
