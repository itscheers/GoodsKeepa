package com.mycompany.inventory.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {
    public static void init() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             InputStream is = DBInitializer.class
                     .getClassLoader()
                     .getResourceAsStream("init.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) continue;
                buf.append(line).append(" ");
                if (line.endsWith(";")) {
                    stmt.execute(buf.toString());
                    buf.setLength(0);
                }
            }
            System.out.println("[OK] БД инициализирована");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
