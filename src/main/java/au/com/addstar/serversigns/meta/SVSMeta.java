package au.com.addstar.serversigns.meta;

import java.util.Arrays;

public class SVSMeta {
    private SVSMetaKey key;
    private SVSMetaValue[] values;

    public SVSMeta(SVSMetaKey key, SVSMetaValue... values) {
        this.key = key;
        this.values = values;
    }

    public SVSMetaKey getKey() {
        return this.key;
    }

    public void addValue(SVSMetaValue value) {
        SVSMetaValue[] array = new SVSMetaValue[this.values.length + 1];
        System.arraycopy(this.values, 0, array, 0, this.values.length);

        array[this.values.length] = value;
        this.values = array;
    }

    public SVSMetaValue removeValue(int index) {
        if ((index >= this.values.length) || (index < 0)) {
            return null;
        }
        SVSMetaValue before = this.values[index];
        System.arraycopy(this.values, index + 1, this.values, index, this.values.length - 1 - index);

        this.values = Arrays.copyOf(this.values, this.values.length - 1);

        return before;
    }

    public SVSMetaValue getValue(int index) {
        if ((index >= this.values.length) || (index < 0)) {
            return null;
        }
        return this.values[index];
    }

    public SVSMetaValue getValue() {
        return getValue(0);
    }

    public boolean hasValue(int index) {
        return (index >= 0) && (index < this.values.length);
    }

    public SVSMetaValue[] getValues() {
        return this.values;
    }

    public boolean equals(Object other) {
        if ((other instanceof SVSMeta)) {
            SVSMeta meta = (SVSMeta) other;
            return (meta.getKey().equals(getKey())) && (meta.getValues().length == getValues().length);
        }

        return false;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.key == null ? 0 : this.key.hashCode());

        result = 31 * result + (this.values == null ? 1231 : 1237);
        result = 31 * result + (this.values == null ? 0 : this.values.length * 31);
        return result;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\meta\SVSMeta.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */