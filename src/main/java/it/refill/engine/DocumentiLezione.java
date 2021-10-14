/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.engine;

/**
 *
 * @author rcosco
 */
public class DocumentiLezione {

    int id_docud;
    int idud;
    String tipo, path, codice_ud;

    public DocumentiLezione(int id_docud, String tipo, String path, String codice_ud) {
        this.id_docud = id_docud;
        this.tipo = tipo;
        this.path = path;
        this.codice_ud = codice_ud;
        try {
            this.idud = Integer.parseInt(codice_ud.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            this.idud = 1;
        }
    }

    public int getIdud() {
        return idud;
    }

    public void setIdud(int idud) {
        this.idud = idud;
    }
    
    public int getId_docud() {
        return id_docud;
    }

    public void setId_docud(int id_docud) {
        this.id_docud = id_docud;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCodice_ud() {
        return codice_ud;
    }

    public void setCodice_ud(String codice_ud) {
        this.codice_ud = codice_ud;
    }

}
