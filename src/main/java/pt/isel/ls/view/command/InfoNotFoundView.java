package pt.isel.ls.view.command;

import pt.isel.ls.core.utils.DataContainer;

public class InfoNotFoundView extends CommandView {

    @Override
    public void printAllInfo() {
        System.out.println("Requested information not found.");
    }
}
