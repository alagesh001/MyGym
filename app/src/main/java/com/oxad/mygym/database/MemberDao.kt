package com.oxad.mygym.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oxad.mygym.model.Member

@Dao
interface MemberDao {
    @Query("SELECT * FROM members LIMIT :limit OFFSET :offset")
    suspend fun getMembers(limit: Int, offset: Int): List<Member>

    @Query("SELECT * FROM members")
    suspend fun getAllMembers(): List<Member>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<Member>)

    @Query("DELETE FROM members")
    suspend fun clearMembers()
}
