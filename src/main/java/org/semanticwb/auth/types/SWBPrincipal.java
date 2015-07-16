package org.semanticwb.auth.types;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created by serch on 7/15/15.
 */
public class SWBPrincipal implements Principal, Serializable{

    private final String name;
    private final String id;

    public SWBPrincipal(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getId() { return id;}

    @Override
    public String toString() {
        return "SWBPrincipal{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SWBPrincipal that = (SWBPrincipal) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
