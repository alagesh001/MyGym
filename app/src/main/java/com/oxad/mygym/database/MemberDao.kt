package com.oxad.mygym.database


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oxad.mygym.model.Member
import com.oxad.mygym.model.Workout

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
    @Query("SELECT * FROM members WHERE name LIKE '%' || :name || '%'")
    fun searchMembersByName(name: String): LiveData<List<Member>>


}
@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<Workout>)

    @Query("SELECT * FROM workouts WHERE muscle_group LIKE '%'  || :muscleGroup || '%' ")
    suspend fun getWorkoutstype(muscleGroup: String): List<Workout>

    @Query("SELECT * FROM workouts")
    suspend fun getAllWorkouts(): List<Workout>
}