package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;

public class DeleteView extends PlainView {

    public DeleteView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        plain.addTitle("Information deleted with success.");
    }
}