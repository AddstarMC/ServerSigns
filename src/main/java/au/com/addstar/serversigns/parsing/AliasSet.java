package au.com.addstar.serversigns.parsing;

import java.util.Collection;
import java.util.HashSet;

public class AliasSet extends HashSet<String> {
    public AliasSet(String... aliases) {
        super(java.util.Arrays.asList(aliases));
    }

    public AliasSet(Collection<String> aliases) {
        super(aliases);
    }

    public boolean matches(String input) {
        for (String next : this) {
            if (next.equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }
}


