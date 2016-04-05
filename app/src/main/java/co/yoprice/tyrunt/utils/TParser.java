package co.yoprice.tyrunt.utils;

import android.content.*;
import android.util.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import co.yoprice.tyrunt.models.Torrent;

public class TParser
{

	public static String getUserImage(Torrent torrent) throws IOException {
		String url = "https://kat.cr/user/" + torrent.getAuthor();
		Document document = Jsoup.parse(new URL(url),3000);
		Element e  = document.getElementById("wall_userpic");
		Log.e(torrent.getAuthor(), "https:"+e.attr("src"));
		return "https:"+e.attr("src");
	}

	public static ArrayList<Torrent> parseTags(Element table){
		ArrayList<Torrent> torrent = new ArrayList<Torrent>();
		Elements trs = table.getElementsByTag("tr");
		for(Element tr : trs){
			if(tr.hasClass("odd") || tr.hasClass("even")){
				torrent.add(parseKatTr(tr));
			}
		}
		return torrent;
	}

	private static Torrent parseKatTr(Element tr)
	{
		Torrent t = Torrent.Builder();
		if(tr.attr("class").equals("odd")) t.setOrder(Torrent.ORDER.ODD);
		else if(tr.attr("class").equals("even")) t.setOrder(Torrent.ORDER.EVEN);
		else t.setOrder(Torrent.ORDER.NA);
		String magnet = null, torrent = null, title = null, author = null, time = null, size = null, seeds = null, peers = null, files = null,author_posted_in = null;
		for(Element td : tr.getElementsByTag("td")){
			if(!td.hasAttr("class"))
			for(Element a : tr.getElementsByTag("a")){
				if(a.hasAttr("href")){
					if(a.hasAttr("data-nop")){
						Log.i("Found Magnet", magnet = a.attr("href"));
					}
					else if(a.hasAttr("data-download")){
						Log.i("Found Download", torrent = a.attr("href"));
					} 
					else if(a.hasClass("cellMainLink")){
						Log.i("Found Title",title = a.text());
						Log.i("Found Author",author_posted_in = a.nextElementSibling().text());
					}else if(a.hasClass("plain")){
						Log.i("Found Author Name", author = a.text());
					}
				}
			}
			else if(td.hasAttr("class")){
				if(td.hasAttr("title")){
					Log.i("Time",time = td.attr("title"));
				}else if(td.attr("class").equals("nobr center")){
					Log.i("Size", size = td.text());
				}else if(td.attr("class").equals("green center")){
					Log.i("Seeds", seeds = td.text());
				}else if(td.attr("class").equals("red lasttd center")){
					Log.i("Peers", peers = td.text());
				}else if(td.attr("class").equals("center")) {
					Log.i("File Count", files = td.text());
				}
			}
		}
		return t.setAuthor(author).setAuthorPostedIn(author_posted_in).setFiles(files).setMagnet(magnet).setTime(time).setPeers(peers).setSeeds(seeds).setSize(size).setTitle(title).setTorrent(torrent);
	}


	public static ArrayList<Torrent> Search(String query) throws IOException {
		return parseDoc(Jsoup.parse(new URL("https://kat.cr/usearch/"+query+"/"),3000));
	}
	
	private static ArrayList<Torrent> searchQuery(String html){
		Document doc  = Jsoup.parse(html);
		return parseDoc(doc);
	}
	private static ArrayList<Torrent> parseDoc(Document doc){
		ArrayList<Torrent> torrent = new ArrayList<Torrent>();

		Elements tables = doc.getElementsByTag("table");
		for(Element t : tables){
			if(!t.hasClass("data")) {
				Log.e("Skipping","Skipping");
				continue;
			}

			Log.e("Found Data","At index " + String.valueOf(tables.indexOf(t)));
			//Log.i("Content", t.html());
			torrent.addAll(parseTags(t));
		}
		return torrent;
	}
	
	public static String getHtml(Context context, int raw) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(raw)));
		StringBuilder sb = new StringBuilder();
		String s = null;
		while((s=br.readLine()) != null){
			sb.append(s);
		}
		br.close();
		return sb.toString();
	}
}
