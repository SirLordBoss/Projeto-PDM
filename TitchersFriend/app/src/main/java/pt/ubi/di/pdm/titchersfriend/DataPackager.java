package pt.ubi.di.pdm.titchersfriend;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 1.BASICALLY PACKS DATA WE WANNA SEND
 */
public class DataPackager {

    String Query,Extra;

    /*
    SECTION 1.RECEIVE ALL DATA WE WANNA SEND
     */
    public DataPackager(String Query, String Extra) {
        this.Query = Query;
        this.Extra = Extra;

    }

    /*
   SECTION 2
   1.PACK THEM INTO A JSON OBJECT
   1. READ ALL THIS DATA AND ENCODE IT INTO A FROMAT THAT CAN BE SENT VIA NETWORK
    */
    public String packData()
    {
        JSONObject jo=new JSONObject();
        StringBuffer packedData=new StringBuffer();
        int c =0;
        boolean antes = true;
        String aux="";
        ArrayList<String> col = new ArrayList<String>();
        ArrayList<String> ex = new ArrayList<String>();
        while(c<Extra.length()){
            if(!(Extra.charAt(c) =='&' ||Extra.charAt(c) =='=') ){
                aux = aux+Extra.charAt(c);
            }
            if(Extra.charAt(c) =='='){
                col.add(aux);
                aux="";
            }
            if(Extra.charAt(c) =='&'){
                ex.add(aux);
                aux="";
            }
            if(c==Extra.length()-1){
                ex.add(aux);

            }
            c++;
        }
        try
        {
            jo.put("q",Query);
            for(int i = 0; i <col.size();i++){
                jo.put(col.get(i), ex.get(i));
            }


            Boolean firstValue=true;

            Iterator it=jo.keys();

            do {
                String key=it.next().toString();
                String value=jo.get(key).toString();

                if(firstValue)
                {
                    firstValue=false;
                }else
                {
                    packedData.append("&");
                }

                packedData.append(URLEncoder.encode(key,"UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value,"UTF-8"));

            }while (it.hasNext());

            return packedData.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
