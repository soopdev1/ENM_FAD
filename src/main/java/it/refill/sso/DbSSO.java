/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.refill.sso;

import static it.refill.engine.Action.conf;
import static it.refill.engine.Action.estraiEccezione;
import static it.refill.engine.Action.log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 *
 * @author Administrator
 */
public class DbSSO {

    private Connection c = null;

    public DbSSO() {

        String driver = "com.mysql.cj.jdbc.Driver";
        String user = conf.getString("db.user.identity");
        String password = conf.getString("db.pass.identity");
        String host = conf.getString("db.host") + ":3306/enm_identity";
        try {
            Class.forName(driver).newInstance();
            Properties p = new Properties();
            p.put("user", user);
            p.put("password", password);
            p.put("characterEncoding", "UTF-8");
            p.put("passwordCharacterEncoding", "UTF-8");
            p.put("useSSL", "false");
            p.put("connectTimeout", "1000");
            p.put("useUnicode", "true");
            this.c = DriverManager.getConnection(host, p);
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
            if (this.c != null) {
                try {
                    this.c.close();
                } catch (Exception ex1) {
                    log.severe(estraiEccezione(ex1));
                }
            }
            this.c = null;
        }
    }

    public void closeDB() {
        try {
            if (this.c != null) {
                this.c.close();
            }
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
    }

    public Connection getConnection() {
        return c;
    }

    public boolean executequery(String qu1) {
        try {
            try ( Statement st1 = this.c.createStatement()) {
                st1.executeUpdate(qu1);
                return true;
            }
        } catch (Exception ex1) {
            log.severe(estraiEccezione(ex1));
        }
        return false;
    }

}
