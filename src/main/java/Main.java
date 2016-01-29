import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by The Schokomilch Foundation on 29.01.2016.
 */
public class Main
{
    public static void main(String[] args)
    {
        JSONArray webIron = WebIron.runImport();

        HashMap ipsAndMails = WebIron.createURL(webIron);

        WebIron.webBugs(ipsAndMails);
    }
}
