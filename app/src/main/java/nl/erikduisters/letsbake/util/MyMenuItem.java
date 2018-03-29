package nl.erikduisters.letsbake.util;

import android.support.annotation.IdRes;

/**
 * Created by Erik Duisters on 25-04-2017.
 */

public class MyMenuItem {
    public final @IdRes int id;
    public boolean enabled;
    public boolean visible;
    public boolean checked;

    public MyMenuItem(@IdRes int id) {
        this(id, true, true, false);
    }

    public MyMenuItem(@IdRes int id, boolean enabled, boolean visible) {
        this(id, enabled, visible, false);
    }

    public MyMenuItem(@IdRes int id, boolean enabled, boolean visible, boolean checked) {
        this.id = id;
        this.enabled = enabled;
        this.visible = visible;
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyMenuItem that = (MyMenuItem) o;

        return id == that.id && enabled == that.enabled && visible == that.visible && checked == that.checked;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (visible ? 1 : 0);
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }
}
