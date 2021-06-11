package org.isc.myapplication.util;


public class DataType {

    private String nip;
    private String patientIdentity;

    private String idUser;

    private String  loginUser;

    private String codeAuthor;

    private String codeBannette;
    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getPatientIdentity() {
        return patientIdentity;
    }

    public void setPatientIdentity(String patientIdentity) {
        this.patientIdentity = patientIdentity;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getCodeAuthor() {
        return codeAuthor;
    }

    public void setCodeAuthor(String codeAuthor) {
        this.codeAuthor = codeAuthor;
    }

    public String getCodeBannette() {
        return codeBannette;
    }

    public void setCodeBannette(String codeBannette) {
        this.codeBannette = codeBannette;
    }

    @Override
    public String toString() {
        return "DataType{" +
                "nip='" + nip + '\'' +
                ", patientIdentity='" + patientIdentity + '\'' +
                ", idUser='" + idUser + '\'' +
                ", loginUser='" + loginUser + '\'' +
                ", codeAuthor='" + codeAuthor + '\'' +
                ", codeBannette='" + codeBannette + '\'' +
                '}';
    }
}
