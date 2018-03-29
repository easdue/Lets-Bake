package nl.erikduisters.letsbake.util;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

/**
 * Created by Erik Duisters on 19-12-2017.
 */

public class MenuUtil {
    private MenuUtil() {}

    public static void updateMenu(Menu menu, MyMenuItem myMenuItem) {
        MenuItem item = menu.findItem(myMenuItem.id);

        if (item != null) {
            item.setEnabled(myMenuItem.enabled);
            item.setVisible(myMenuItem.visible);
            item.setChecked(myMenuItem.checked);
        }
    }

    public static void updateMenu(Menu menu, List<MyMenuItem> myMenu) {
        for (MyMenuItem myItem : myMenu) {
            updateMenu(menu, myItem);
        }
    }
}
