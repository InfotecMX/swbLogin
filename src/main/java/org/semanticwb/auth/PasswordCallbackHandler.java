package org.semanticwb.auth;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * Created by serch on 7/15/15.
 */
public class PasswordCallbackHandler implements CallbackHandler {

    private String userid;
    private char[] password;

    public PasswordCallbackHandler(String userid, String password) {
        this.userid = userid;
        this.password = password.toCharArray();
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for(Callback callback:callbacks)
        {
            if (callback instanceof NameCallback)
            {
                ((NameCallback)callback).setName(userid);
            } else if (callback instanceof PasswordCallback)
            {
                ((PasswordCallback)callback).setPassword(password);
            } else
            {
                throw new UnsupportedCallbackException(callback, "This only handles username/password");
            }
        }
    }
}
