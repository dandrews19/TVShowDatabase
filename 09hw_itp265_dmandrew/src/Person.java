
public class Person implements Comparable<Person>{
	
	private final String email;
	protected String firstName;
	protected String lastName;
	protected String name;
	public Person(String email, String firstName, String lastName) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = firstName + " " + lastName;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	@Override
	public String toString() {
		return "Person named " + name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public final int compareTo(Person o) {
		if(this.equals(o)) return 0;
		
		int num = this.lastName.compareTo(o.lastName);
		if (num == 0) {
			num = this.firstName.compareTo(o.firstName);
			if(num == 0) {
				num = this.email.compareTo(o.email);
			}
		}
		return num;
	}
	
	
	
}
