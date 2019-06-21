package net.prosavage.savageskyblock;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Updater {

    public static void Update(String UrlDownload, String version) {
        try {
            URL url = new URL(UrlDownload);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
            InputStream response = connection.getInputStream();
            File out = new File("plugins//" + SavageSkyBlock.getInstance().getDescription().getName() + "-" + version + ".jar");
            out.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream("plugins//" + SavageSkyBlock.getInstance().getDescription().getName() + "-" + version + ".jar");
            final byte data[] = new byte[1024];
            int count;
            while ((count = response.read(data, 0, 1024)) != -1) {
                fileOutputStream.write(data, 0, count);
            }
            response.close();
            File delete = new File("plugins//" + SavageSkyBlock.getInstance().getDescription().getName() + "-" + SavageSkyBlock.getInstance().getDescription().getVersion() + ".jar");
            if (delete.exists()) {
                delete.delete();
            }
            Bukkit.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}