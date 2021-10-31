package com.ekspeace.kimopax.Constants;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ekspeace.kimopax.Interface.ServiceDao;
import com.ekspeace.kimopax.Model.Service;

@Database(entities = {Service.class}, version = 1, exportSchema = false)
public abstract class ServiceDatabase extends RoomDatabase {
    private static ServiceDatabase instance;

    public abstract ServiceDao serviceDao();

    public static synchronized ServiceDatabase getInstance(Context context)
    {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                     ServiceDatabase.class, "service_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}