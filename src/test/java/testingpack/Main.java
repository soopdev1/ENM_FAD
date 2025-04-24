/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testingpack;

import rc.so.sso.ClientSSO;

/**
 *
 * @author Administrator
 */
public class Main {
    public static void main(String[] args) {
        
        String v1 = "EDTESTER";
        
        String enc1 = ClientSSO.encrypt_S(v1);
        System.out.println("testingpack.Main.main() "+v1);
        System.out.println("testingpack.Main.main() "+ClientSSO.decrypt_S(enc1));
    }
}
