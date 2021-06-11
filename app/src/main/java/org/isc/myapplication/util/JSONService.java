package org.isc.myapplication.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONService {

    public static DataType extractData(String json) {
        try {
            JSONObject data = new JSONObject(json);

            DataType res = new DataType();

            res.setNip(data.getString("nip"));
            res.setCodeAuthor(data.getString("auteur"));
            res.setCodeBannette(data.getString("banette"));
            res.setIdUser(data.getString("idUtilisateur"));
            res.setLoginUser(data.getString("loginUtilisateur"));

            res.setPatientIdentity(data.getString("identitePatient"));


            return res;
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }


    }
}
