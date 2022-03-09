/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testingpack;

import static it.refill.sso.ClientSSO.login;
import static it.refill.sso.ClientSSO.logout;
import it.refill.sso.ResponseSSO;

/**
 *
 * @author Administrator
 */
public class SendSSO {
    public static void main(String[] args) {
//        ResponseSSO rs = login("testuser", "password");
       ResponseSSO rs = logout("testuser");
        System.out.println(rs.toString());
//        if (rs.getAccess_token().startsWith("ERROR")) {
//
//        } else {
//
//        }
    }
}
