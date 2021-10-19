package id.co.app.core.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


/**
 * Created by Lukas Kristianto on 5/29/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

interface BaseDao<T> {
	/**
	 * Insert an object in the database.
	 *
	 * @param obj the object to be inserted.
	 */
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(obj: T): Long

	/**
	 * Insert an array of objects in the database.
	 *
	 * @param obj the objects to be inserted.
	 */
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(obj: List<T>): List<Long>

	/**
	 * Update an object from the database.
	 *
	 * @param obj the object to be updated
	 */
	@Update
	fun update(obj: T): Int


	/**
	 * Update an object from the database.
	 *
	 * @param obj the object to be updated
	 */
	@Update
	fun update(obj: List<T>): Int

	/**
	 * Delete an object from the database
	 *
	 * @param obj the object to be deleted
	 */
	@Delete
	fun delete(obj: T)
}