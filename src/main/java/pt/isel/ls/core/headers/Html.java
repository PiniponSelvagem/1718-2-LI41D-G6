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
    public void addTable(String[] columns, String[][] data) {
        text.append("<table border = 1>");
        text.append("<tr>");
        for(int i = 0; i < columns.length; i++){
            text.append("<th>").append(columns[i]).append("</th>");
        }
        text.append("</tr>");
        for(int i = 0; i < data.length; i++){
            text.append("<tr>");
            for(int j = 0; j < data[i].length; j++){
                text.append("<td>").append(data[i][j]).append("</td>");
            }
            text.append("</tr>");
        }
    }

    @Override
    public void addObject(String id, String[] fieldName, String[] value) {
        addTitle(id);
        text.append("<ul>");
        for(int i = 0; i < fieldName.length && i < value.length; i++){
            text.append("<li>")
                    .append("<b>").append(fieldName[i]).append("</b>").append(": ").append(value[i]);
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