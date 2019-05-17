package au.com.addstar.serversigns.config;

import au.com.addstar.serversigns.utils.StringUtils;
import au.com.addstar.serversigns.persist.PersistenceEntry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collection;

public class ConfigGenerator {
    private static final String[] HEADER = {"# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #", "#                                                                       #", "#  This configuration is for ServerSigns. All keys have accompanying    #", "#  comment descriptions upon file generation; these comments MAY NOT    #", "#  remain after new values are loaded by the plugin. Please be sure to  #", "#  refer to the help page for more information: http://exl.li/svsconfig #", "#                                                                       #", "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #"};


    private static final String VERSION_COMMENT = "# Don't touch this! It might wipe your entire config!!";


    private static void writeArray(BufferedWriter writer, String[] arr)
            throws IOException {
        for (String s : arr) {
            writer.write(s);
            writer.newLine();
        }
    }

    private static String getValueOf(Object o) {
        if (o.getClass().equals(String.class)) {
            return "'" + StringUtils.decolour(o.toString()) + "'";
        }

        return o.toString();
    }

    public static void generate(IServerSignsConfig config, Path path) throws ConfigLoadingException {
        try {
            BufferedWriter writer = java.nio.file.Files.newBufferedWriter(path, Charset.defaultCharset());
            Throwable localThrowable2 = null;
            try {
                writeArray(writer, HEADER);
                writer.newLine();

                writer.write("# Don't touch this! It might wipe your entire config!!");
                writer.newLine();
                writer.write(String.format("config-version: %s", Integer.valueOf(config.getVersion())));
                writer.newLine();


                Field[] declaredFields = config.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    PersistenceEntry configEntry = declaredField.getAnnotation(PersistenceEntry.class);
                    if (configEntry != null) {
                        declaredField.setAccessible(true);
                        writer.newLine();

                        for (String comment : configEntry.comments()) {
                            writer.write(comment);
                            writer.newLine();
                        }
                        try {
                            String name = configEntry.configPath().isEmpty() ? declaredField.getName() : configEntry.configPath();
                            writer.write(name);
                            if (Collection.class.isAssignableFrom(declaredField.getType())) {
                                writer.write(58);
                                writer.newLine();

                                Collection<?> collection = (Collection) declaredField.get(config);
                                for (Object obj : collection) {
                                    writer.write("- ");
                                    writer.write(getValueOf(obj));
                                    writer.newLine();
                                }
                            } else {
                                writer.write(": ");
                                writer.write(getValueOf(declaredField.get(config)));
                                writer.newLine();
                            }
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (Throwable localThrowable1) {
                localThrowable2 = localThrowable1;
                throw localThrowable1;


            } finally {


                if (writer != null) if (localThrowable2 != null) try {
                    writer.close();
                } catch (Throwable x2) {
                    localThrowable2.addSuppressed(x2);
                }
                else writer.close();
            }
        } catch (IOException e) {
            throw new ConfigLoadingException("Exception while generating config file", e);
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\config\ConfigGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */