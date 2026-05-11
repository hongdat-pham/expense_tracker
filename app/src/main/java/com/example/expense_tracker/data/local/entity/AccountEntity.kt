package com.example.expense_tracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class AccountType {
    SAVING,
    CHECKING,
    CREDIT_CARD,
    DIGITAL_WALLET
}

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val name: String,               // e.g. "Vietcombank", "Cash"
    val lastFourDigits: String,     // e.g. "4291"
    val type: AccountType,
    val balance: Double = 0.0,
    val isActive: Boolean = true
)