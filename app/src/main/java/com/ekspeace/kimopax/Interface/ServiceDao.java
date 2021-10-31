package com.ekspeace.kimopax.Interface;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ekspeace.kimopax.Model.Service;

import java.util.List;

@Dao
public interface ServiceDao {
    @Insert
    void Insert(Service service);

    @Delete
    void Delete(Service service);

    @Query("SELECT * FROM service_table ORDER BY Id DESC")
    LiveData<List<Service>> getAllServices();

    @Query("DELETE FROM service_table")
    void DeleteAll();
}
