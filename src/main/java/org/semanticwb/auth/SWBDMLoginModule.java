package org.semanticwb.auth;

import org.semanticwb.auth.types.SWBPrincipal;
import org.semanticwb.auth.types.SWBRole;
import org.semanticwb.auth.util.CryptoUtil;
import org.semanticwb.datamanager.*;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by serch on 7/15/15.
 */
public class SWBDMLoginModule extends SWBDMLoginModuleBase {

    static SWBScriptEngine loginEngine = null;
    static SWBDataSource ds = null;
    static {
        DataMgr.createInstance(new File(".").getAbsolutePath());
        loginEngine = DataMgr.getScriptEngine("/user.js", true);
        ds = loginEngine.getDataSource("User");
//        DataObject dao = new DataObject();
//        dao.put("username","serch");
//        byte[] salt = CryptoUtil.generateSalt();
//        dao.put("salt",CryptoUtil.byteToBase64(salt));
//        String password = "";
//        try {
//            password = CryptoUtil.byteToBase64(CryptoUtil.getSimpleHash("serch", salt));
//        } catch (GeneralSecurityException|UnsupportedEncodingException gse)
//        {
//            log.throwing("a.s.a.SWBDMLoginModule","staticInit",gse);
//        }
//        dao.put("clave",password);
//        DataObject dao2 = new DataObject();
//        dao2.put("data",dao);
//        try {
//            ds.add(dao2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run() {
                loginEngine.close();
            }
        });
    }


    @Override
    protected List<Principal> checkCredential(final String name, final char[] password) throws LoginException {
        try {
            String cname = name.replaceAll("\\$","_$_");
            DataObject query = new DataObject();
            query.put("username", cname);
            DataObject dao = new DataObject();
            dao.put("data", query);
            DataObject obj=ds.fetch(dao);
            Object data = obj.get("response");
            if(data instanceof DataObject){
                obj = (DataObject)data;
                data = obj.get("data");
                if (data instanceof DataList){
                    data=((DataList)data).get(0);
                    if (data instanceof DataObject){
                        obj = (DataObject)data;
                        byte[] salt = CryptoUtil.base64ToByte(obj.getString("salt"));
                        String cmpPass = CryptoUtil.byteToBase64(
                                CryptoUtil.getSimpleHash(String.valueOf(password), salt));
                        if (cmpPass.equals(obj.getString("clave"))){
                            List<Principal> lista = new ArrayList<>();
                            String id = obj.getString("_id");
                            lista.add(new SWBPrincipal(id, obj.getString("username")));
                            String[] roles = obj.getString("roles").split(",");
                            for (String role:roles){
                                lista.add(new SWBRole(id, role));
                            }
                            log.fine("user "+name+" authenticated with roles: "+obj.getString("roles"));
                            return lista;
                        }

                    }
                }
            }
        } catch (IOException|NoSuchAlgorithmException ioe) {
            log.throwing("o.s.a.SWBDMLoginModule","checkCredential", ioe);
            throw new LoginException("IOError: "+ioe.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        super.initialize(subject, callbackHandler, sharedState, options);

        System.out.println("options:"+options);
        System.out.println("shared:"+sharedState);

    }
}
