/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author marek
 */
public class DBUtils {
    
    private static final Logger logger = Logger.getLogger(DBUtils.class.getName());
    
    public static void executeSqlScript(DataSource dataSource, URL scriptUrl) throws SQLException {
        try (Connection conn = dataSource.getConnection()){
            
            for (String sqlStatement : readSqlStatements(scriptUrl)) {
                if (!sqlStatement.trim().isEmpty()) {
                    conn.prepareStatement(sqlStatement).executeUpdate();
                }
            }
        } 
    }
    
    private static String[] readSqlStatements(URL url) {
        try {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            while (true) {
                int count = reader.read(buffer);
                if (count < 0) {
                    break;
                }
                result.append(buffer, 0, count);
            }
            return result.toString().split(";");
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read " + url, ex);
        }
    }
    
    public static void tryCreateTables(DataSource ds, URL scriptUrl) throws SQLException {
        try {
            executeSqlScript(ds, scriptUrl);
            logger.warning("Tables created");
        } catch (SQLException ex) {
            if ("X0Y32".equals(ex.getSQLState())) {
                // This code represents "Table/View/... already exists"
                // This code is Derby specific!
                return;
            } else {
                throw ex;
            }
        }
    }
}
