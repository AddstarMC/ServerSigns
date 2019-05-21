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


