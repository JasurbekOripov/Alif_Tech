package uz.juo.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.juo.domain.models.Venue

@Entity
data class BookEntity(
    val endDate: String,
    val icon: String,
    val name: String,
    val startDate: String,
    @PrimaryKey
    val url: String,
)
