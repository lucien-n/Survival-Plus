package me.scaffus.survivalplus.sql;

public class DatabaseCredentials {
    private String host;
    private String user;
    private String password;
    private String databaseName;
    private int port;

    public DatabaseCredentials(String host, String user, String password, String databaseName, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
        this.port = port;
    }

    public String toUri() {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(databaseName);
        return sb.toString();
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
