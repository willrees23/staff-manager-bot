package dev.wand.sql;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author xWand
 */
@Getter
public class MySQL {

    private String host, database, username, password;
    private int port;

    private Connection connection;

    public MySQL(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() throws SQLException, ClassNotFoundException {
        if (!isConnected()) {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password
            );
        }
    }

    public void disconnect() throws SQLException {
        if (isConnected()) {
            connection.close();
        }
    }
}
