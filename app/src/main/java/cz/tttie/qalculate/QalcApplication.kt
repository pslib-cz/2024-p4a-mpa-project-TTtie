package cz.tttie.qalculate

import android.app.Application
import androidx.room.Room
import cz.tttie.qalculate.db.AppDatabase

class QalcApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "qalc-db"
        ).build()
    }
}