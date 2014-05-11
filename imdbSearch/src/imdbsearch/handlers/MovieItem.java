package imdbsearch.handlers;

public class MovieItem {
	
	String imdbid;
	String imdburl;
	String genres;
	String languages;
	String country;
	String rating;
	String title;
	String year;
	
	public String output(){
		String newLine = "<br>";
		StringBuilder out = new StringBuilder();
		out.append("<b>Title</b>: " + title);
		out.append(newLine);
		out.append("<b>IMDB URL</b>: " + "<a href=\""+imdburl+"\">Go to Web site for " + title + "</a>");
		out.append(newLine);
		out.append("<b>Genres</b>:" + genres);
		out.append("<b>Languages</b>:" + languages);
		out.append(newLine);
		out.append("<b>Country</b>:" + country);
		out.append("<b>Rating</b>:" + rating);
		out.append(newLine);
		out.append("<b>Title</b>:" + title);
		out.append("<b>Year</b>:" + year);
		return out.toString();
	}

}
