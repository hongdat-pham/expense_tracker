package com.example.expense_tracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class TransactionType { EXPENSE, INCOME }

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId"),
        Index("accountId")
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val accountId: Long,
    val categoryId: String,        // must match Category.id in Constants.kt
    val amount: Double,             // always positive
    val type: TransactionType,      // EXPENSE or INCOME
    val date: Long,                 // timestamp milliseconds
    val description: String = "",
    val receiptPath: String? = null,
    val isRecurring: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)