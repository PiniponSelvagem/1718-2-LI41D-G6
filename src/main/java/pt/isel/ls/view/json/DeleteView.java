package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;

public class DeleteView extends JsonView {

    public DeleteView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        json.addObject(new String[]{"info"}, new String[]{"Information deleted with success."});
    }
}