
package dev.wand.data.sql;

import dev.wand.BotMain;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author xWand
 */
public class SQLManager {

    public void initStaffLogs() {
        PreparedStatement ps;
        try {
            // table: id, performerId, recipientId, action, oldRoleName, newRoleName, dateTime, reason
            ps = BotMain.getSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS stafflogs " +
                    "(id INT NOT NULL AUTO_INCREMENT, " +
                    "performerId VARCHAR(255), " +
                    "recipientId VARCHAR(255), " +
                    "action VARCHAR(255), " +
                    "oldRoleName VARCHAR(255), " +
                    "newRoleName VARCHAR(255), " +
                    "dateTime BIGINT(255), " +
                    "reason VARCHAR(255), " +
                    "PRIMARY KEY (id))");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initOtherLogs() {
        PreparedStatement ps;
        try {
            // table: id, performerId, punishment, reason, proof, datetime
            ps = BotMain.getSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS otherlogs " +
                    "(id INT NOT NULL AUTO_INCREMENT, " +
                    "performerId VARCHAR(255), " +
                    "punishment VARCHAR(255), " +
                    "reason VARCHAR(255), " +
                    "proof VARCHAR(255), " +
                    "dateTime BIGINT(255), " +
                    "PRIMARY KEY (id))");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createStaffLog(String performerId, String recipientId, String action, String oldRoleName, String newRoleName, long dateTime, String reason) {
        PreparedStatement ps;
        try {
            ps = BotMain.getSQL().getConnection().prepareStatement("INSERT INTO stafflogs (performerId, recipientId, action, oldRoleName, newRoleName, dateTime, reason) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, performerId);
            ps.setString(2, recipientId);
            ps.setString(3, action);
            ps.setString(4, oldRoleName);
            ps.setString(5, newRoleName);
            ps.setLong(6, dateTime);
            ps.setString(7, reason);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
