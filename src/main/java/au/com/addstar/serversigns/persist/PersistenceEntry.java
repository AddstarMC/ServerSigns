package au.com.addstar.serversigns.persist;

import au.com.addstar.serversigns.persist.mapping.DefaultPersistenceMapper;
import au.com.addstar.serversigns.persist.mapping.IPersistenceMapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistenceEntry {
    Class<? extends IPersistenceMapper> configMapper() default DefaultPersistenceMapper.class;

    String configPath() default "";

    String[] comments() default {};
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\PersistenceEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */