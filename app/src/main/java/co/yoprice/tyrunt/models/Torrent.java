package co.yoprice.tyrunt.models;

import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class Torrent implements Serializable {

	private String authorPostedIn, magnet,
			torrent, title,
			author, time,
			size, seeds,
			peers, files;
	private ORDER order;

	public static Torrent Builder() {
		return new Torrent();
	}

	public Torrent setAuthorPostedIn(String authorPostedIn) {
		this.authorPostedIn = authorPostedIn;
		return this;
	}


	@Override
	public String toString() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this, getClass());
	}

	public String getAuthorPostedIn() {
		return authorPostedIn;
	}

	public enum ORDER{
		ODD,EVEN,NA
	}

	public String getMagnet() {
		return magnet;
	}

	public Torrent setMagnet(String magnet) {
		this.magnet = magnet;
		return this;
	}

	public String getTorrent() {
		return torrent;
	}

	public Torrent setTorrent(String torrent) {
		this.torrent = torrent;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Torrent setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getAuthor() {
		return author;
	}

	public Torrent setAuthor(String author) {
		this.author = author;
		return this;
	}

	public String getTime() {
		return time;
	}

	public Torrent setTime(String time) {
		this.time = time;
		return this;
	}

	public String getSize() {
		return size;
	}

	public Torrent setSize(String size) {
		this.size = size;
		return this;
	}

	public String getSeeds() {
		return seeds;
	}

	public Torrent setSeeds(String seeds) {
		this.seeds = seeds;
		return this;
	}

	public String getPeers() {
		return peers;
	}

	public Torrent setPeers(String peers) {
		this.peers = peers;
		return this;
	}

	public String getFiles() {
		return files;
	}

	public Torrent setFiles(String files) {
		this.files = files;
		return this;
	}

	public ORDER getOrder() {
		return order;
	}

	public Torrent setOrder(ORDER order) {
		this.order = order;
		return this;
	}
}
