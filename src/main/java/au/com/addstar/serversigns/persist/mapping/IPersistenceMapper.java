package au.com.addstar.serversigns.persist.mapping;

import org.bukkit.configuration.MemorySection;

public interface IPersistenceMapper<E> {
    void setMemorySection(MemorySection paramMemorySection);

    E getValue(String paramString)
            throws MappingException;

    void setValue(String paramString, E paramE);
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\IPersistenceMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */