package odev6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		
		String DELIMITERS = "[-+=" + " " + // space
				"\r\n " + // carriage return line fit
				"1234567890" + // numbers
				"â€™'\"" + // apostrophe
				"(){}<>\\[\\]" + // brackets
				":" + // colon
				"," + // comma
				"â€’â€“â€”â€•" + // dashes
				"â€¦" + // ellipsis
				"!" + // exclamation mark
				"." + // full stop/period
				"Â«Â»" + // guillemets
				"-â€�" + // hyphen
				"?" + // question mark
				"â€˜â€™â€œâ€�" + // quotation marks
				";" + // semicolon
				"/" + // slash/stroke
				"â�„" + // solidus
				"â� " + // space?
				"Â·" + // interpunct
				"&" + // ampersand
				"@" + // at sign
				"*" + // asterisk
				"\\" + // backslash
				"â€¢" + // bullet
				"^" + // caret
				"Â¤Â¢$â‚¬Â£Â¥â‚©â‚ª" + // currency
				"â€ â€¡" + // dagger
				"Â°" + // degree
				"Â¡" + // inverted exclamation point
				"Â¿" + // inverted question mark
				"Â¬" + // negation
				"#" + // number sign (hashtag)
				"â„–" + // numero sign ()
				"%â€°â€±" + // percent and related signs
				"Â¶" + // pilcrow
				"â€²" + // prime
				"Â§" + // section sign
				"~" + // tilde/swung dash
				"Â¨" + // umlaut/diaeresis
				"_" + // underscore/understrike
				"|Â¦" + // vertical/pipe/broken bar
				"â�‚" + // asterism
				"â˜�" + // index/fist
				"âˆ´" + // therefore sign
				"â€½" + // interrobang
				"â€»" + // reference mark
				"]";
		String[] splitted_delimiters = DELIMITERS.split("");

		// stop words read file
		String stop_words_string = "";
		try {
			File stop_words_file = new File("stop_words_en.txt");
			Scanner sc = new Scanner(stop_words_file);
			while (sc.hasNextLine()) {
				stop_words_string = stop_words_string + sc.nextLine() + " ";
			}
			sc.close();
		} catch (Exception e) {
			System.out.println("Not found file.");
			e.printStackTrace();
		}
		String[] splitted_stop_words = stop_words_string.split(" ");

		// search read file
		String search_string = "";
		try {
			File search_file = new File("search.txt");
			Scanner sc = new Scanner(search_file);
			while (sc.hasNextLine()) {
				search_string = search_string + sc.nextLine() + " ";
			}
			sc.close();
		} catch (Exception e) {
			System.out.println("Not found file.");
			e.printStackTrace();
		}
		String[] splitted_search = search_string.split(" ");

		HashedDictionary<String, ArrayList<Node>> HashedDataBase = new HashedDictionary<String, ArrayList<Node>>();
		long time_start = System.nanoTime();
		try {
			File folder = new File("sport");
			File[] listOfFiles = folder.listFiles();
			String file_name = "";
			int which_txt = 1;
			for (File file : listOfFiles) {
				if (file.isFile()) {
					file_name = "sport\\" + file.getName();
					File myObj = new File(file_name);
					Scanner scanner = new Scanner(myObj);
					String data = "";
					while (scanner.hasNextLine()) {
						data += scanner.nextLine() + " ";
					}
					String data_lowercase = data.toLowerCase(Locale.ENGLISH);
					data_lowercase = remove_stopWords_delimiters(splitted_stop_words, splitted_delimiters,
							data_lowercase); //remove stop words and delimiters
					String[] splitted_data = data_lowercase.split(" ");
					DictionaryInterface<String, Integer> frequencyDataBase = new Dictionary<String, Integer>();
					//counts how many of which words are in the txt
					for (int i = 0; i < splitted_data.length; i++) {
						if (frequencyDataBase.contains(splitted_data[i]))
							frequencyDataBase.add(splitted_data[i], frequencyDataBase.getValue(splitted_data[i]) + 1);
						else
							frequencyDataBase.add(splitted_data[i], 1);
					}

					Iterator<String> keyIterator = frequencyDataBase.getKeyIterator();
					Iterator<Integer> valueIterator = frequencyDataBase.getValueIterator();
					while (keyIterator.hasNext()) { // adds hash table
						ArrayList<Node> arraylist = new ArrayList<Node>();
						Node node = new Node(which_txt + ".txt | ", valueIterator.next());
						String temp_key_iterator = keyIterator.next();
						if (HashedDataBase.contains(temp_key_iterator)) {
							ArrayList<Node> temp_arraylist = HashedDataBase.getValue(temp_key_iterator);
							temp_arraylist.add(node);
							HashedDataBase.add(temp_key_iterator, temp_arraylist);
						} else {
							arraylist.add(node);
							HashedDataBase.add(temp_key_iterator, arraylist);
						}
					}
					which_txt++;
				}
			}
		} catch (Exception e) {
			System.out.println("Not found file.");
			e.printStackTrace();
		}

//		long nano_estimated_Time = System.nanoTime();
//		System.out.println(nano_estimated_Time - time_start);
//		System.out.println(HashedDataBase.collision_counter);

//		search(splitted_search, HashedDataBase);

//		Getting input from user and relevance
		Scanner valueFromUser = new Scanner(System.in);
		System.out.print("Please enter word: ");
		String value = valueFromUser.nextLine();
		String[] splitted_value = value.split(" ");
		relevance(splitted_value, HashedDataBase);
		valueFromUser.close();
	}

	public static String remove_stopWords_delimiters(String[] array_stop_words, String[] array_delimiters,
			String data) {
		for (int i = 0; i < array_delimiters.length; i++) {
			if (data.contains(array_delimiters[i])) // Are there any punctuation marks in our data?
				data = data.replace(array_delimiters[i], " "); // delete if any
		}

		for (int i = 0; i < array_stop_words.length; i++) { // remove stop words
			String a = array_stop_words[i];
			if (data.contains(" " + a + " ")) // are there any stop words?
				data = data.replace(" " + array_stop_words[i] + " ", " "); // replace if any
		}

		for (int i = 0; i < 20; i++) // converts multiple spaces to one space
			data = data.replace("  ", " ");
		return data;
	}

	public static void display(DictionaryInterface<String, Integer> dataBase) {
		Iterator<String> keyIterator = dataBase.getKeyIterator();
		Iterator<Integer> valueIterator = dataBase.getValueIterator();
		while (keyIterator.hasNext()) {
			System.out.println("Key:" + keyIterator.next() + " \t\tValue:" + valueIterator.next());
		}
	}

	public static void display_hashed_dictionary(HashedDictionary<String, ArrayList<Node>> dataBase) {
		Iterator<String> keyIterator = dataBase.getKeyIterator();
		Iterator<ArrayList<Node>> valueIterator = dataBase.getValueIterator();
		while (keyIterator.hasNext()) {
			ArrayList<Node> temp_it = valueIterator.next();
			System.out.print("Key:" + keyIterator.next() + " \t\t");
			for (int i = 0; i < temp_it.size(); i++) {
				System.out.print("    " + temp_it.get(i).getTxt() + temp_it.get(i).getValue());
			}
			System.out.println();
		}
	}

	public static void relevance(String[] splitted_words, HashedDictionary<String, ArrayList<Node>> dataBase) {
		int temp_bigger = 0, temp_index = 0;
		Iterator<String> keyIterator = dataBase.getKeyIterator();
		Iterator<ArrayList<Node>> valueIterator = dataBase.getValueIterator();
		while (keyIterator.hasNext()) {
			String temp_key_iterator = keyIterator.next();
			ArrayList<Node> temp_value_iterator = valueIterator.next();
			for (int i = 0; i < splitted_words.length; i++) {
				if (temp_key_iterator.equalsIgnoreCase(splitted_words[i])) {
					System.out.print(temp_key_iterator + " \t");
					temp_bigger = temp_value_iterator.get(0).getValue();
					temp_index = 0;
					for (int j = 1; j < temp_value_iterator.size(); j++) {
						if (temp_bigger < temp_value_iterator.get(j).getValue()) {
							temp_bigger = temp_value_iterator.get(j).getValue();
							temp_index = j;
						}
					}
					System.out.print("\t" + temp_value_iterator.get(temp_index).getTxt()
							+ temp_value_iterator.get(temp_index).getValue() + "\n");
				}
			}
		}
	}

	public static void search(String[] search_array, HashedDictionary<String, ArrayList<Node>> dataBase) {
		long time_start = System.nanoTime();
		Iterator<String> keyIterator = dataBase.getKeyIterator();
		Iterator<ArrayList<Node>> valueIterator = dataBase.getValueIterator();
		String temp_key_iterator = "";
		int i = 0;
		while (keyIterator.hasNext()) {
			temp_key_iterator = keyIterator.next();
			for (int j = 0; j < search_array.length; j++) {
				if (temp_key_iterator.equals(search_array[j])) {
//					long nano_estimated_Time = System.nanoTime(); //for max min search time
//					System.out.println(nano_estimated_Time - time_start);
					i++; //how many search words are there?
				}
				
			}
		}
//		long nano_estimated_Time = System.nanoTime(); //for avg search time
//		System.out.println(nano_estimated_Time - time_start);
//		System.out.println(i); //how many search words are there?
	}
}
