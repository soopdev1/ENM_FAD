/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.engine;

/**
 *
 * @author rcosco
 */
public class GenericUser {

    String idallievi, nome, cognome, codicefiscale, email, numero, tipo,idpro;

    public GenericUser(String idallievi, String nome, String cognome, String codicefiscale, String email, String numero) {
        this.idallievi = idallievi;
        this.nome = nome;
        this.cognome = cognome;
        this.codicefiscale = codicefiscale;
        this.email = email;
        this.numero = numero;
    }

    public String getIdpro() {
        return idpro;
    }

    public void setIdpro(String idpro) {
        this.idpro = idpro;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public static String formatType(String tipo) {
        if (tipo.equals("S")) {
            return "ALLIEVO";
        } else if (tipo.equals("O")) {
            return "OSPITE";
        } else if (tipo.equals("D")) {
            return "DOCENTE";
        }
        return null;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getIdallievi() {
        return idallievi;
    }

    public void setIdallievi(String idallievi) {
        this.idallievi = idallievi;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodicefiscale() {
        return codicefiscale;
    }

    public void setCodicefiscale(String codicefiscale) {
        this.codicefiscale = codicefiscale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
