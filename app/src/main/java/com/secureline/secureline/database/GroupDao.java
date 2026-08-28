package com.secureline.secureline.database;

import android.content.ContentValues;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupDao {

    private final SQLiteDatabase db;

    public GroupDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long insertGroup(String groupUuid, String groupName, String adminUuid) {
        ContentValues values = new ContentValues();
        values.put("group_uuid", groupUuid);
        values.put("group_name", groupName);
        values.put("admin_uuid", adminUuid);
        return db.insert("groups", null, values);
    }

    public void addMember(String groupUuid, String memberUuid) {
        ContentValues values = new ContentValues();
        values.put("group_uuid", groupUuid);
        values.put("member_uuid", memberUuid);
        db.insert("group_members", null, values);
    }

    public List<String> getGroupMembers(String groupUuid) {
        List<String> members = new ArrayList<>();
        android.database.Cursor cursor = db.query(
            "group_members",
            new String[]{"member_uuid"},
            "group_uuid = ?",
            new String[]{groupUuid},
            null, null, null
        );

        while (cursor.moveToNext()) {
            members.add(cursor.getString(0));
        }
        cursor.close();
        return members;
    }

    public void removeMember(String groupUuid, String memberUuid) {
        db.delete("group_members", "group_uuid = ? AND member_uuid = ?",
            new String[]{groupUuid, memberUuid});
    }

    public void deleteGroup(String groupUuid) {
        db.delete("groups", "group_uuid = ?", new String[]{groupUuid});
        db.delete("group_members", "group_uuid = ?", new String[]{groupUuid});
    }
}
