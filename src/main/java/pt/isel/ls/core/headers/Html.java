package pt.isel.ls.core.headers;

public class Html extends Header {
    @Override
    protected void open() {
        text.append("<html>")
                .append("<body>");
    }

    @Override
    public void addTitle(String title) {
        text.append("<h2>").append(title).append("</h2>");
    }

    @Override
    public void addTable(String title, String[] columns, String[][] data) {
        text.append("<table border = 1>");
        text.append("<tr>");
        for (int y=0; y<columns.length; ++y) {
            text.append("<th>").append(columns[y]).append("</th>");
        }
        text.append("</tr>");
        for (int y=0; y<data.length; ++y) {
            text.append("<tr>");
            for(int x=0; x<data[y].length; ++x){
                text.append("<td>").append(data[y][x]).append("</td>");
            }
            text.append("</tr>");
        }
        text.append("</table>");
    }

    @Override
    public void addObject(String nameId, String[] fieldName, String[] value) {
        addTitle(nameId);
        text.append("<ul>");
        for (int y=0; y<fieldName.length && y<value.length; ++y) {
            text.append("<li>")
                    .append("<b>").append(fieldName[y]).append("</b>").append(": ").append(value[y]);
            text.append("</li>");
        }
        text.append("</ul>");
    }

    @Override
    public void close() {
        text.append("</body>");
        text.append("</html>");
    }
}