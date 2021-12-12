package com.example.weatherforecast.DB;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {InterestedCity.class,WeatherHistory.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    /*static final Migration MIGRATION_1_2=new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE InterestedCity (id INTEGER,interestedCityName TEXT,PRIMARY KEY(id))");
        }
    };
    static final Migration MIGRATION_2_3=new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE WeatherHistory (id INTEGER,cityName TEXT,high INTEGER,low INTEGER,date TEXT,time TEXT,PRIMARY KEY(id))");
        }
    };*/
    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(MyApplication.context, AppDatabase.class, "badao.db")
                            //.addMigrations(MIGRATION_1_2,MIGRATION_2_3)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract  InterestedCityDao interestedCityDao();
    public abstract  WeatherHistoryDao weatherHistoryDao();
}
