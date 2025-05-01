package com.mycompany.inventory.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {

    public static void init() {
        try (Connection conn = DBUtil.getConnection()) {
            InputStream is = DBInitializer.class.getResourceAsStream("/init.sql");
            if (is == null) {
                throw new RuntimeException("init.sql не знайдено у ресурсах");
            }
            String raw = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String noComments = raw.replaceAll("(?m)^--.*$", "");

            try (Statement st = conn.createStatement()) {
                for (String stmt : noComments.split(";")) {
                    String sql = stmt.trim();
                    if (!sql.isEmpty()) {
                        st.execute(sql);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося ініціалізувати БД: " + e.getMessage(), e);
        }
    }
}
