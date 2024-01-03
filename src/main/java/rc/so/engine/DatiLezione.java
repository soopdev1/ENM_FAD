/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.engine;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rcosco
 */
public class DatiLezione {

    String NUMEROLEZIONE, GIORNODILEZIONE, PROGETTO, DATALEZIONE, INIZIOLEZIONE, FINELEZIONE, ORE, UNITADIDATTICA, FASE;

    List<DocumentiLezione> files;

    public DatiLezione(String NUMEROLEZIONE, String GIORNODILEZIONE, String PROGETTO, String DATALEZIONE, String INIZIOLEZIONE, String FINELEZIONE, String ORE, String UNITADIDATTICA, String FASE) {
        this.NUMEROLEZIONE = NUMEROLEZIONE;
        this.GIORNODILEZIONE = GIORNODILEZIONE;
        this.PROGETTO = PROGETTO;
        this.DATALEZIONE = DATALEZIONE;
        this.INIZIOLEZIONE = INIZIOLEZIONE;
        this.FINELEZIONE = FINELEZIONE;
        this.ORE = ORE;
        this.UNITADIDATTICA = UNITADIDATTICA;
        this.FASE = FASE;
        this.files = new LinkedList<>();

    }

    public String getNUMEROLEZIONE() {
        return NUMEROLEZIONE;
    }

    public void setNUMEROLEZIONE(String NUMEROLEZIONE) {
        this.NUMEROLEZIONE = NUMEROLEZIONE;
    }

    public String getGIORNODILEZIONE() {
        return GIORNODILEZIONE;
    }

    public void setGIORNODILEZIONE(String GIORNODILEZIONE) {
        this.GIORNODILEZIONE = GIORNODILEZIONE;
    }

    public String getPROGETTO() {
        return PROGETTO;
    }

    public void setPROGETTO(String PROGETTO) {
        this.PROGETTO = PROGETTO;
    }

    public String getDATALEZIONE() {
        return DATALEZIONE;
    }

    public void setDATALEZIONE(String DATALEZIONE) {
        this.DATALEZIONE = DATALEZIONE;
    }

    public String getINIZIOLEZIONE() {
        return INIZIOLEZIONE;
    }

    public void setINIZIOLEZIONE(String INIZIOLEZIONE) {
        this.INIZIOLEZIONE = INIZIOLEZIONE;
    }

    public String getFINELEZIONE() {
        return FINELEZIONE;
    }

    public void setFINELEZIONE(String FINELEZIONE) {
        this.FINELEZIONE = FINELEZIONE;
    }

    public String getORE() {
        return ORE;
    }

    public void setORE(String ORE) {
        this.ORE = ORE;
    }

    public String getUNITADIDATTICA() {
        return UNITADIDATTICA;
    }

    public void setUNITADIDATTICA(String UNITADIDATTICA) {
        this.UNITADIDATTICA = UNITADIDATTICA;
    }

    public String getFASE() {
        return FASE;
    }

    public void setFASE(String FASE) {
        this.FASE = FASE;
    }

    public List<DocumentiLezione> getFiles() {
        return files;
    }

    public void setFiles(List<DocumentiLezione> files) {
        this.files = files;
    }

}
