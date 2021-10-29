import java.util.ArrayList;

public class TVWatcher extends Person {
	
	private ArrayList<TVShow> favorites;
	
	
	public TVWatcher(String email, String firstName, String lastName, ArrayList<TVShow> favorites) {
		super(email, firstName, lastName);
		this.favorites = favorites;
	}

	public TVWatcher(String email, String firstName, String lastName) {
		this(email, firstName, lastName, new ArrayList<>());
	}
	public TVWatcher(String email, String firstName, String lastName, TVShow s1, TVShow s2, TVShow s3) {
		this(email, firstName, lastName);
		favorites.add(s1);favorites.add(s2);favorites.add(s3);
	}

	/**
	 * @return the favorites
	 */
	public ArrayList<TVShow> getFavorites() {
		return favorites;
	}

	/**
	 * @param favorites the favorites to set
	 */
	public void setFavorites(ArrayList<TVShow> favorites) {
		this.favorites = favorites;
	}

	public void addFavorite(TVShow fav) {
		this.favorites.add(fav);
	}
	public void removeFavorite(TVShow fav) {
		this.favorites.remove(fav);
	}
	public boolean likesShow(TVShow show) {
		//return favorites.contains(show); //doesn't work because requires the streaming services to match
		boolean found = false;
		int i = 0;
		while(!found && i < favorites.size()) {
			if(favorites.get(i).getName().equalsIgnoreCase(show.getName())) {
				found = true;
			}
			i++;
		}
		return found;
	}
	
	public boolean likesShow(String showName) {
		boolean found = false;
		int i = 0;
		while(!found && i < favorites.size()) {
			if(favorites.get(i).getName().equalsIgnoreCase(showName)) {
				found = true;
			}
			i++;
		}
		return found;
	}
	
	@Override
	public String toString() {
		return super.toString() + " likes to Watch: " + favorites ;
	}
	public static void main(String[] args) {
		ArrayList<TVShow> myFavs = new ArrayList<>();
	TVShow mother = (new TVShow("How I met your mother", new Streaming[] {Streaming.AMAZON_PRIME, Streaming.NETFLIX}));
	myFavs.add(mother);
	myFavs.add(new TVShow("Zoey's Extraordinary Playlist", new Streaming[] {Streaming.HULU}));
		myFavs.add(new TVShow("Suits", new Streaming[] {Streaming.AMAZON_PRIME, Streaming.NETFLIX, Streaming.HULU}));
		
		
		TVWatcher kendra = new TVWatcher("kwalther@usc.edu", "Kendra", "Walther", myFavs);
		System.out.println(kendra);
		System.out.println("Kendra likes how I met your mother: " + kendra.likesShow("How i met your mother"));
		System.out.println("Kendra likes pokemon: " + kendra.likesShow("pokemon"));
		System.out.println("Kendra likes show how i met your mother: " + kendra.likesShow(mother));
		
	}
}
