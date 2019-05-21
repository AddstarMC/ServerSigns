package au.com.addstar.serversigns.persist.mapping;

import org.bukkit.configuration.MemorySection;

public interface IPersistenceMapper<E> {
    void setMemorySection(MemorySection paramMemorySection);

    E getValue(String paramString)
            throws MappingException;

    void setValue(String paramString, E paramE);
}


