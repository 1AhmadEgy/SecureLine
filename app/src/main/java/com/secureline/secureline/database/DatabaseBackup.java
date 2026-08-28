package com.secureline.secureline.database;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DatabaseBackup {

    public static boolean backupDatabase(SQLiteDatabase db, File backupFile) {
        try {
            File dbFile = new File(db.getPath());
            FileInputStream input = new FileInputStream(dbFile);
            FileOutputStream output = new FileOutputStream(backupFile);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            output.close();
            input.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean restoreDatabase(File backupFile, SQLiteDatabase db) {
        try {
            File dbFile = new File(db.getPath());
            FileInputStream input = new FileInputStream(backupFile);
            FileOutputStream output = new FileOutputStream(dbFile);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            output.close();
            input.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}