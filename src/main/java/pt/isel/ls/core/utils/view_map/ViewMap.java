package pt.isel.ls.core.utils.view_map;

import java.util.HashMap;

public abstract class ViewMap {
    HashMap<String, String> viewMap = new HashMap<>();

    ViewMap() {
        fillViewMap();
    }

    abstract void fillViewMap();

    public HashMap<String, String> getViewMap() {
        return viewMap;
    }
}
