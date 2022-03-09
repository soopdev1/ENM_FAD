/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.refill.sso;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

/**
 *
 * @author Administrator
 */
public class ResponseSSO {
    
    String access_token;
    int expires_in;
    String token_type;
    String refresh_token;
    String scope;

    public ResponseSSO(String access_token, int expires_in, String token_type, String refresh_token, String scope) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.scope = scope;
    }

    public ResponseSSO(String access_token, String refresh_token) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public ResponseSSO() {
    }
    
    
    

    
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, JSON_STYLE);
    }
    
    
}
