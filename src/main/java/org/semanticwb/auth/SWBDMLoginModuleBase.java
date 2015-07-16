package org.semanticwb.auth;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by serch on 7/15/15.
 */
public abstract class SWBDMLoginModuleBase implements LoginModule {
    static protected Logger log = Logger.getLogger("o.s.a.SWBDMLoginModule");
    protected Subject subject;
    protected CallbackHandler callbackHandler;
    protected Map<String, ?> sharedState;
    protected Map<String, ?> options;
    private boolean debug = false;
    private List<Principal> info=null;
    private boolean commited = false;

    protected abstract List<Principal> checkCredential(String name, char[] password) throws LoginException;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject=subject;
        this.callbackHandler=callbackHandler;
        this.sharedState=sharedState;
        this.options=options;
        if (options.get("debug")!=null &&
                ("true".equalsIgnoreCase((String)options.get("debug")) ||
                "yes".equalsIgnoreCase((String)options.get("debug")) ||
                "1".equalsIgnoreCase((String)options.get("debug")))) debug=true;
        if(debug) log.setLevel(Level.FINEST);
    }

    @Override
    public boolean login() throws LoginException {
        String login;
        char[] pwd = null;
        try{
            Callback[] callbacks = new Callback[2];
            callbacks[0]=new NameCallback("username: ");
            callbacks[1]=new PasswordCallback("password: ",false);
            callbackHandler.handle(callbacks);
            login=((NameCallback)callbacks[0]).getName();
            pwd=((PasswordCallback)callbacks[1]).getPassword();
            ((PasswordCallback)callbacks[1]).clearPassword();
            log.finest("Authenticating: "+login);
            info=checkCredential(login, pwd);
            log.finest("Got: "+info);
        } catch (RuntimeException ruex){
            throw new LoginException("Can't authenticate:"+ruex.getLocalizedMessage());
        } catch (UnsupportedCallbackException e) {
            throw new LoginException("Error: "+ e.getCallback());
        } catch (IOException e) {
            throw new LoginException(e.getLocalizedMessage());
        }
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        if(null==info) return false;
        info.forEach(e->subject.getPrincipals().add(e));
        commited=true;
        log.finest("Principals commited");
        return commited;
    }

    @Override
    public boolean abort() throws LoginException {
        if (info == null) {
            return false;
        } else if (null!=info && !commited){
            info=null;
        } else {
            logout();
        }
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        info.forEach(p->subject.getPrincipals().remove(p));
        info=null;
        log.finest("Login user out");
        return true;
    }
}
