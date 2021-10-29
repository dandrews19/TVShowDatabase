import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;
import java.io.File;

public class TVShowMainProgram {

	private BFF bff;
	private List<TVWatcher> people; // a simple list of all the data
	private List<TVShow> shows; // a list of shows, with duplicates combined

	private Map<String, TVWatcher> userMap;
	private Map<TVShow, Integer> showCount ;
	private Map<Streaming, List<TVShow>> serviceMap ;

	public TVShowMainProgram() {
		bff = new BFF();
		people = new ArrayList<>();
		shows = new ArrayList<>();
		readInitialDataFromFile();
		shows = collapseDuplicates(shows);
		setupMaps(); // after the data is read in and the lists are made, set up the maps

	}
	public void setupMaps() {
		userMap =  makeUserMap();
		showCount =  makePopularityMap();
		serviceMap =  makeServiceMap();
	}

	////////////////////////////////////////////////////////////
	// STEP 1: Handling errors 
	////////////////////////////////////////////////////////////
	private void readInitialDataFromFile() {
		String file = "bin/tvShowFormData.tsv";
		ArrayList<String> data = FileReader.readFile(file);
		String header = data.get(0);
		System.out.println("Data is formatted like the following: \n" + header);

		for(int i = 1; i < data.size(); i++) {
			String line = data.get(i);
			try {
				TVWatcher person = parseDataLineToStudentWatcher(line); 
				people.add(person);
			for(TVShow show: person.getFavorites()) {
					this.shows.add(show);
				}
			}
			catch (InvalidTVWatcherException e) {
				System.out.println("Ignoring bad data, based on this exception" + e);
			}
			}
		}

	


	private TVWatcher parseDataLineToStudentWatcher(String lineOfFile) throws InvalidTVWatcherException {
		//Timestamp	Email Address	Your first name	Your last name	Which section are you in?	
		//Your number 1 favorite tv show	Which streaming service is it on?	Your number 2 favorite tv show	Which streaming service is it on?	Your number 3 favorite tv show	Which streaming service is it on?
		Scanner sc = new Scanner(lineOfFile);
		sc.useDelimiter("\t");

		try {
			String timestamp = sc.next();
			String email = sc.next();
			String fname = sc.next();
			String lname = sc.next();
			String section = sc.next().toUpperCase();
			ArrayList<TVShow> shows = new ArrayList<>();

			//additional loop allows to getting 1+ shows from the line
			while(sc.hasNext()) {
				String show = sc.next().toLowerCase();
				if(! show.isBlank()) {
					char let = show.charAt(0);
					String cap = (let+"").toUpperCase();
					show = cap + show.substring(1);
					String platform = sc.next(); // platforms could have multiple items
					String[] str_array = platform.split(",");
					Streaming[] services = new Streaming[str_array.length];
					for(int i = 0; i < str_array.length; i++) {
						services[i] = Streaming.matchService(str_array[i]);
					}
					shows.add(new TVShow(show, services));
				}
				// else : Missing data for rest of show, not adding
			}
			TVWatcher watcher = new StudentTVWatcher(email, fname, lname, shows, Section.valueOf(section));
			return watcher;
		}
		catch (Exception e) {
			System.err.println("An exception happened");
			throw new InvalidTVWatcherException("Error turning " + lineOfFile + " into a show");
			
		}
	}


	////////////////////////////////////////////////////////////
	//STEP 2: Commenting Helper Methods
	////////////////////////////////////////////////////////////

	//TODO: Comment (For Step 2)
	// This method ensure there are no duplicate shows by creating a new list for the shows, and then one by one adding
	// adding each show from the data into the new list. This method checks if the latest show has already been added to the
	// new list. If it has, then it checks if it has the same streaming service. If it has the same streaming service, then
	// it disregards that show, ensuring the original copy remains in the new list. If the show has different streaming service,
	// then it adds the other service to the list of services that show had in the original list. We need to do this when 
	// gathering open-ended data from users because people might be watching the same show from different streaming services.
	private List<TVShow> collapseDuplicates(List<TVShow> allData) {
		Set<String> showNames = new HashSet<>();
		List<TVShow> noDups = new ArrayList<>();
		for(TVShow show : allData) {
			String name = show.getName();
			if(showNames.contains(name)) {
				// showName is in set, does the show match one in the list?
				if(!noDups.contains(show)) {
					//show name matches, but the TVShow object is different, so match up services
					TVShow other = findMatch(name, noDups);
					for(Streaming serv: show.getServices()) {
						other.addSevice(serv);
					}

				}
			     }
			else {
				showNames.add(name);
				noDups.add(show);

			}
		}
		return noDups;
	}

	// This method is used to find the match in the no duplicates list so that the additional streaming service can be added
	// to the list of streaming seervices for the show in the no duplicates list. It simply takes in the name of the show its
	// looking for a match for, going through the list and seeing which one matches the name, and returning that match from the no
	// duplicates list.
	private TVShow findMatch(String name, List<TVShow> allShows) {
		TVShow match = null;
		int i = 0;
		while (i < allShows.size() && match == null) {
			TVShow show = allShows.get(i);
			if(show.getName().equalsIgnoreCase(name)) {
				match = show;
			}
			i++;
		}
		return match;
	}
	////////////////////////////////////////////////////////////
	//STEP 3: Making maps
	////////////////////////////////////////////////////////////
	
	
	// takes info from people list, creates a map with their email as the key, and their TVWatcher object
	// as the value
	private Map<String, TVWatcher> makeUserMap() {
		Map<String, TVWatcher> user = new HashMap<>();
		for (int i = 0; i < this.people.size(); i++) {
			user.put(this.people.get(i).getEmail(), people.get(i));
			
		}
		return user;
		
	}
	
	
	// takes the Streaming enum  as a value and creates an ArrayList of TVshows corresponding to the streaming service as the value
	private Map<Streaming, List<TVShow>> makeServiceMap() {
		Map<Streaming, List<TVShow>> serviceMap = new HashMap<>();
		for(Streaming service: Streaming.values()) {
			serviceMap.put(service, new ArrayList<TVShow>() );
			
		}
		for(TVShow show: shows) {
			for(Streaming service: Streaming.values()) {
				if(show.availableOn(service)) {
					serviceMap.get(service).add(show);
				}
			}
		}
		
		return serviceMap;
	}
	
	// Creates a hash map with each TV show in the dataset as a key and the number of people who liked it as a value
	private Map<TVShow, Integer> makePopularityMap() {
		Map<TVShow, Integer> popularityMap = new HashMap<>();
		shows = collapseDuplicates(shows);
		for(TVShow s: shows) {
			popularityMap.put(s, 0);
		}
		for(TVWatcher p: people) {
			for(TVShow sh: p.getFavorites()) {
				TVShow newShow = findMatch(sh.getName(), shows);
				popularityMap.put(newShow, popularityMap.get(newShow)+1);
				
			}
		}
		
		return popularityMap;
	}



	public void run(){
		bff.print("Welcome to the program for exploring tv shows");


		boolean keepGoing = true;
		while(keepGoing){
			TVProgramMenu.printMenu();
			int num = bff.inputInt(">", 1,  TVProgramMenu.getNumOptions());
			TVProgramMenu option = TVProgramMenu.getOption(num);
			switch(option){

			case PRINT_MAP_USERS: 
				printUserMap();
				break;
			case  PRINT_SERVICE_MAP: 
				printServiceMap();
				break;
			case PRINT_MAP_POPULARITY_SHOWS: 
				printPopularityMap();
				break;
			case FIND_NUMBER_LIKES: 
				BFF helper = new BFF();
				String input = helper.inputLine("Enter the show you want to find count for");
				printPopularityOfShow(input);
				

				break;
			case GET_MOST_POPULAR: //optional
				mostPopularShow();
				break;
			case WRITE_LIST_TO_FILE: 
				writeServiceToFile();
				
				break;
			case MAKE_USER_FILE: // ("Make user file of people who like a show"),
				writeShowToFile();
				
				break;
			case READ_USER_FILE: // ("Read user file based on name of file"),
				readUserFile();
				
				break;
			case QUIT   :  keepGoing = false; break;
			}
		}
		bff.print("Goodbye");
	}


	////////////////////////////////////////////////////////////
	//STEP 4: Methods for the TVProgram Menu
	////////////////////////////////////////////////////////////
	
	// takes the userMap and prints all the key-value pairings
	private void printUserMap() {
		for (Entry<String, TVWatcher> s: userMap.entrySet()) {
			System.out.println(s.getKey() + " -> " + s.getValue().toString());
		}

	}
	
	// prints each streaming service along with the shows that are available on it
	private void printServiceMap() {
		for (Entry<Streaming, List<TVShow>> s: serviceMap.entrySet()) {
			System.out.println(s.getKey() + "-> ");
			s.getValue().sort(null);
			for(TVShow sh: s.getValue()) {
				System.out.println("\t" + sh);
			}
		}

		
	}
	
	
	// prints each show alongside the number of people in ITP 265 who said they watch it
	private void printPopularityMap() {
		System.out.println("Popularity Show MAP");
		for(Entry<TVShow, Integer> s: showCount.entrySet()) {
			System.out.println("\t" + s.getKey().toString() + ": " + s.getValue());
		}
	}
	
	// takes user input of TV show, and prints out how many people like it if it is in the database
	private void printPopularityOfShow(String show) {
		 TVShow s = findMatch(show, shows);
		 if(showCount.get(s) != null) {
			 System.out.println("There are: " + showCount.get(s) + " people who like this show");
		 }
		 else  {
			 System.out.println("Did not find that show in the data");
		 }
		 
	}

	// writes all the tv shows on a specific service to a file
	public void writeServiceToFile() {
		BFF helper = new BFF();
		String userService = helper.inputLine("Enter the service you want to make a file for");
		Streaming newService = Streaming.matchService(userService);
		for(Streaming service: Streaming.values()) {
			
		if(newService.name().equalsIgnoreCase(service.name())) {
			
			
			try (
					FileOutputStream fileStream = new FileOutputStream("src/" + service.name() + ".txt");
					PrintWriter outFS = new PrintWriter(fileStream)) {
				
				for (TVShow s: shows) {
					if(s.availableOn(service) ) {
						outFS.println(s.toString());
						
					}
				}
				System.out.println("Wrote the data to file: " + "src/" + service.name() + ".txt");
				
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		}
		
		}
	
	
	// gets user input for a tv show and outputs a list of students who indicated that they liked the show
	public void writeShowToFile() {
		BFF helper = new BFF();
		String userShow = helper.inputLine("Enter the show you want to find");
		shows = collapseDuplicates(shows);
		TVShow updatedShow = findMatch(userShow, shows);
		if (updatedShow == null) {
			System.out.println("TV show not in database");
		}
		else {
			try (
					FileOutputStream fileStream = new FileOutputStream("src/" + userShow + ".txt");
					PrintWriter outFS = new PrintWriter(fileStream)) {
				
				 for(TVWatcher p: people) {
					 if(p.likesShow(updatedShow)) {
						
						outFS.println(p.getEmail() + "/" + p.getName());
						
					 }
				 }
				
				System.out.println("Wrote the data to file: " + "src/" + userShow + ".txt");
				
				
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
		}
				
	}
	
	// reads already-created files, and reccomends shows based on people in the file who like other shows
	public void readUserFile() {
		BFF bff = new BFF();
		List<TVShow> recs = new ArrayList<TVShow>();
		String fileName1 = bff.inputLine("Enter the name of the user file you want to read");
		String fileName = "src/" + fileName1 + ".txt";
		try {
			FileInputStream fis = new FileInputStream(fileName);
			Scanner scan = new Scanner(fis);
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] separated = line.split("/");
				for(TVWatcher p: people) {
					if (p.getEmail().equalsIgnoreCase(separated[0])) {
						for(TVShow show: p.getFavorites()) {
							recs.add(show);
						}
					}
				}
				
				}
			recs = collapseDuplicates(recs);
			recs.remove(findMatch(fileName1, recs));
			System.out.println("Based on people who like " + fileName1 + " you might also like:");
			for(TVShow rec: recs) {
				System.out.println(rec.toString());
			}
			
			
		} catch (FileNotFoundException e) {
			System.out.println("No file with that name found: " + fileName);
		}
		
	}
	
	// find the most popular show(s) and prints them
	public void mostPopularShow() {
		int popularity = 0;
		List<TVShow> popularShows = new ArrayList<TVShow>();
		for(Entry<TVShow, Integer> s: showCount.entrySet()) {
			if(s.getValue() > popularity) {
				popularShows.clear();
				popularity = s.getValue();
				popularShows.add(s.getKey());
				
			}
			else if (s.getValue() == popularity) {
				popularShows.add(s.getKey());
			}
		}
		System.out.println("These popular shows are liked by " + popularity + " people:");
		for(TVShow show: popularShows) {
			System.out.println("\t" + show.toString());
		}
	}

	public static void main(String[] args) {
		TVShowMainProgram program = new TVShowMainProgram();
		program.run();

	}

}
