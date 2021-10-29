import java.util.Arrays;

public class TVShow implements Comparable<TVShow>{
	private String name;
	private Streaming[] services;
	public TVShow(String name, Streaming service) {
		this.name = name;
		this.services = new Streaming[] {service};
	}
	public TVShow(String name, Streaming[] services) {
		this.name = name;
		this.services = services;
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
	 * @return the sevice
	 */
	public Streaming[] getServices() {
		return services;
	}

	/**
	 * Add another service to the array of all services that provide this show
	 * @param service
	 */
	public void addSevice(Streaming service) {
		//only add if not already there
		if(!availableOn(service)) {
			Streaming[] newService = new Streaming[this.services.length +1];
			// copy elements over
			for(int i = 0; i < this.services.length; i++) {
				newService[i] = services[i];
			}
			newService[this.services.length] = service;
			this.services = newService;
		}
	}

	public boolean availableOn(Streaming service) {
		boolean found = false;
		int i = 0;
		while(!found && i < services.length) {
			if(services[i] == service) {
				found = true;
			}
			i++;
		}
		return found;
	}
	/**
	 * Remove one service from the array of services
	 * @param service
	 */
	public void removeSevice(Streaming serviceToDelete) {
		//first make sure the service is in the array
		if(availableOn(serviceToDelete)) {
			// one smaller array

			Streaming[] newService = new Streaming[this.services.length-1];
			// copy elements over
			int newIndex = 0;
			for(int i = 0; i < this.services.length; i++) {
				if(services[i] != serviceToDelete) {

					newService[newIndex] = services[i];
					newIndex++;
				}

			}

			this.services = newService;
		}
		// else nothing to delete, so services doesn't change

	}

	/**
	 * Reset the array of services that provide this show.
	 * @param services
	 */
	public void setServices(Streaming[] services) {
		this.services = services;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.toLowerCase().hashCode());
		result = prime * result + Arrays.hashCode(services);
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
		TVShow other = (TVShow) obj;
		return name.equalsIgnoreCase(other.name)
				&&  (Arrays.equals(services, other.services));

	}
	@Override
	public String toString() {
		return name + ", available on: " + Arrays.toString(services);
	}

	public static void main(String[] args) {
		TVShow ex = new TVShow("How I met your mother", new Streaming[] {Streaming.AMAZON_PRIME, Streaming.NETFLIX});
		System.out.println("Created show: " + ex);
		ex.addSevice(Streaming.HULU);
		System.out.println("Added hulu to services: " + ex);
		ex.removeSevice(Streaming.AMAZON_PRIME);
		System.out.println("Removed amazon prime from services: " + ex);
		ex.removeSevice(Streaming.DISNEY_PLUS);
		System.out.println("Removed disney from services: (not there)" + ex);
		boolean avail = ex.availableOn(Streaming.HBO_MAX);
		System.out.println("Is " + ex.getName() + " on HBO? " + avail);

	}
	@Override
	public int compareTo(TVShow o) {
		// TODO Auto-generated method stub
		return this.name.compareTo(o.name);
	}
}
