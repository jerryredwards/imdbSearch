package imdbsearch.handlers;

import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import org.json.JSONObject;

public class Search {
	private String searchType;
	private JTextField searchField;
	private JEditorPane resultField;
	
	public Search(String name){
		
		searchType = name;
		buildFrame();
	}
	
	private void buildFrame(){
		
		JFrame frame = new 	JFrame(searchType);
		frame.setSize(500, 300);
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		buildContents(frame);
		frame.setVisible(true);
		
	}

	private void buildContents(JFrame frame) {
		
		JLabel label = new JLabel(searchType);
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		buildPanel(controlPanel);
		buildResultPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		frame.add(label,c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		frame.add(controlPanel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;      //make this component tall
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		frame.add(resultField, c);
	}

	private void buildResultPanel() {
		resultField = new JEditorPane();
		resultField.setEditorKit(new HTMLEditorKit());
		resultField.addHyperlinkListener(createHyperLinkListner());
		resultField.setEditable(false);
		resultField.setText("Results");
	}

	private HyperlinkListener createHyperLinkListner() {
		return new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent link) {
		        if(link.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		        	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		                try {
		                    desktop.browse(link.getURL().toURI());
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		            }
		        }
		    }
		};
	}

	private void buildPanel(JPanel controlPanel) {

		searchField = new JTextField ();
		searchField.setSize(200, 24);
		JButton inputButton = buildButton();
		controlPanel.add(searchField);
		controlPanel.add(inputButton);
		
	}

	private JButton buildButton() {
		JButton button = new JButton("Search");
		button.addActionListener(createListner());
		return button;
	}

	private ActionListener createListner() {
		ActionListener listner = new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 try {
					resultField.setText(createMovieList(new URL("http://deanclatworthy.com/imdb/?q=" + parseSearchText(searchField.getText()))));
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					resultField.setText("Something went wrong with the creation of the search url. The developer did not take the time to put in better string parsing and should be shot");
				}      
	         }
		};
		return listner;
	}
	
	private String createMovieList(URL url) {
		String forReturn;
		BufferedReader reader = null;
	    try {
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        forReturn = readSite(buffer.toString());
	    } catch (IOException e1) {
			// TODO Auto-generated catch block
	    	forReturn = e1.getMessage();
		} finally {
	        if (reader != null)
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    }
	    
	    return forReturn;
	}
	
	private String readSite(String string) {
		MovieItem item = new MovieItem();
		JSONObject json = new JSONObject(string);
		item.country = json.getString("country");
		item.genres = json.getString("genres");
		item.imdbid = json.getString("imdbid");
		item.imdburl = json.getString("imdburl");
		item.languages = json.getString("languages");
		item.rating = json.getString("rating");
		item.title = json.getString("title");
		item.year = json.getString("year");
		return item.output();
	}

	private String parseSearchText(String text) {
		return text.replace(' ', '+');
	}
}
