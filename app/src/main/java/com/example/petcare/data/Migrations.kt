package com.example.petcare.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {

        // 1. Update the existing pets table

        db.execSQL("ALTER TABLE `pets` ADD COLUMN `breedId` TEXT")

        db.execSQL("ALTER TABLE `pets` ADD COLUMN `breedName` TEXT")

        db.execSQL("ALTER TABLE `pets` ADD COLUMN `notes` TEXT")


        // 2. Update the existing care_tasks table

        db.execSQL("ALTER TABLE `care_tasks` ADD COLUMN `lastNotifiedAt` INTEGER")


        // 3. Create the vaccines table

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `vaccines` (
                `id` TEXT NOT NULL,
                `petId` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `givenOn` INTEGER,
                `nextDueOn` INTEGER NOT NULL,
                `notes` TEXT,
                `lastNotifiedAt` INTEGER,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`petId`) REFERENCES `pets`(`id`)
                    ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_vaccines_petId`
            ON `vaccines` (`petId`)
        """.trimIndent())


        // 4. Create the supplies table

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `supplies` (
                `id` TEXT NOT NULL,
                `petId` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `unit` TEXT NOT NULL,
                `quantityLeft` REAL NOT NULL,
                `dailyUse` REAL NOT NULL,
                `updatedAt` INTEGER NOT NULL,
                `lastNotifiedAt` INTEGER,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`petId`) REFERENCES `pets`(`id`)
                    ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_supplies_petId`
            ON `supplies` (`petId`)
        """.trimIndent())


        // 5. Create the weights table

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `weights` (
                `id` TEXT NOT NULL,
                `petId` TEXT NOT NULL,
                `kg` REAL NOT NULL,
                `recordedAt` INTEGER NOT NULL,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`petId`) REFERENCES `pets`(`id`)
                    ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_weights_petId`
            ON `weights` (`petId`)
        """.trimIndent())


        // 6. Create the breeds table

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `breeds` (
                `id` TEXT NOT NULL,
                `species` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `temperament` TEXT,
                `lifeSpan` TEXT,
                `weightKg` TEXT,
                `origin` TEXT,
                `imageId` TEXT,
                `updatedAt` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
        """.trimIndent())
    }
}