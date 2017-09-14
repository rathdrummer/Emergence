package Normal;

import java.net.URL;
import java.util.Stack;

/**
 * Created by oskar on 2016-12-07.
 * This classes handles Image urls in way that makes it clearer when things goes wrong
 */
public class ImageRes {


    private URL path;
    private String aim;


    public ImageRes(String string) {
        Stack<String> fileEnds = new Stack<>();
        String testEnd;

        fileEnds.add(".png");
        fileEnds.add(".jpg");
        fileEnds.add(".jpeg");
        fileEnds.add("");


        string = "/images/" + string;

        if (!string.substring(string.length() - 4, string.length() - 3).equals(".")) {
            do {
                testEnd = string + fileEnds.pop();
                path = this.getClass().getResource(testEnd);
            }
            while (!fileEnds.isEmpty() && path == null);
        }

        aim = "'" + string + "'";
    }

    public boolean isValid() {
        return path != null;
    }

    public URL getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageRes imageRes = (ImageRes) o;

        return getPath() != null ? getPath().equals(imageRes.getPath()) : imageRes.getPath() == null;

    }

    @Override
    public int hashCode() {
        return getPath() != null ? getPath().hashCode() : 0;
    }

    public String toString() {
        return aim;
    }
}
