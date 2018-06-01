package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;

public class DeleteView extends PlainView {

    public DeleteView(DataContainer data) {
        super(data);
    }

    @Override
    protected void createPlain() {
        plain.addTitle("Information deleted with success.");
    }
}