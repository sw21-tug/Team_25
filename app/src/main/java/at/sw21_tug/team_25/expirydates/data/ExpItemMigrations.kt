package at.sw21_tug.team_25.expirydates.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object ExpItemMigrations {
    /**
     * Migrate from:
     * version 1 to version 2
     * implement getNextExpiringItems(), nothing to change in table but migration is needed
     */
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {}
    }
}