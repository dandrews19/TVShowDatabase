import java.util.ArrayList;

public class StudentTVWatcher extends TVWatcher {
	private Section section;

	public StudentTVWatcher(String email, String firstName, String lastName, ArrayList<TVShow> favorites,
			Section section) {
		super(email, firstName, lastName, favorites);
		this.section = section;
	}

	public StudentTVWatcher(String email, String firstName, String lastName, TVShow s1, TVShow s2, TVShow s3, 	Section section) {
		super(email, firstName, lastName, s1, s2, s3);
		this.section = section;
	}

	public StudentTVWatcher(String email, String firstName, String lastName, Section section) {
		super(email, firstName, lastName);
		this.section = section ;
	}

	@Override
	public String toString() {
		return super.toString() + " Student in " + section + " section";
	}
	
	
}
