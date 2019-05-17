package au.com.addstar.serversigns.hooks;

import java.lang.reflect.Constructor;

public class HookWrapper<E> {
    private E hook;
    private boolean hooked = false;
    private Class<E> clazz;
    private Class<?>[] paramClasses;
    private Object[] paramObjects;

    public HookWrapper(Class<E> instantiationClass, Class[] parameterClasses, Object[] parameterObjects) {
        this.clazz = instantiationClass;
        this.paramClasses = parameterClasses;
        this.paramObjects = parameterObjects;
    }

    public void instantiateHook() throws Exception {
        this.hook = this.clazz.getConstructor(this.paramClasses).newInstance(this.paramObjects);
        this.hooked = true;
    }

    public boolean isHooked() {
        return this.hooked;
    }

    public void setHooked(boolean val) {
        this.hooked = val;
    }

    public E getHook() {
        return this.hook;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\hooks\HookWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */