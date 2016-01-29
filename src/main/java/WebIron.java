import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

/**
 * Created by The Schokomilch Foundation on 29.01.2016.
 */

public class WebIron
{
    public static JSONArray runImport()
    {
        try
        {
            URL webIronList = new URL("https://www.webiron.com/abuse_feed//?format=json");
            Proxy Tor = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9050));
            HttpURLConnection tunnelConnection = (HttpURLConnection)webIronList.openConnection(Tor);

            tunnelConnection.connect();

            String line = null;
            StringBuffer tmp = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(tunnelConnection.getInputStream()));

            while ((line = in.readLine()) != null)
            {
                tmp.append(line);
            }

            JSONArray webIronJSON = new JSONArray(tmp.toString());

            return webIronJSON;

        }
        catch (Exception e)
        {
            e.getStackTrace();
        }

        return null;
    }

    public static HashMap createURL(JSONArray parseList)
    {
        HashMap<String, HashSet<String>> ipAndMail = new HashMap<String, HashSet<String>>();

        for (int i = 0; i < parseList.length(); i++)
        {
            JSONArray mails = parseList.getJSONObject(i).getJSONArray("event_emails");

            HashSet<String> mailSet = new HashSet<String>();
            for (int j = 0; j < mails.length(); j++)
            {
                mailSet.add(mails.get(j).toString());
            }

            ipAndMail.put(parseList.getJSONObject(i).get("attacker_ip").toString(), mailSet);
        }

        return ipAndMail;
    }

    public static List<URL> webBugs(HashMap ipsAndMails)
    {
        List<URL> webBugs = new LinkedList<URL>();

        Iterator entries = ipsAndMails.entrySet().iterator();
        while (entries.hasNext())
        {
            Map.Entry currentEntry = (Map.Entry) entries.next();
            String ip = (String) currentEntry.getKey();
            Set email = (Set) currentEntry.getValue();
            for (Object mail : email)
            {
                String stringURL = "https://www.webiron.com/images/misc/" + ip + "/" + mail + "/webiron-logo_abuse.png";

                try
                {
                    URL url = new URL(stringURL);
                    webBugs.add(url);
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }

                System.out.println(stringURL);
            }
        }

        return webBugs;
    }

}
