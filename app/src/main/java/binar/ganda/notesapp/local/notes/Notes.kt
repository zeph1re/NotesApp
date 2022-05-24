package binar.ganda.notesapp.local.notes

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Entity
@Parcelize
data class Notes(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "description") var desc: String?,
    @ColumnInfo(name = "date") var date: String?
) : Parcelable
