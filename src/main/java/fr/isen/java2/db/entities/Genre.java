package fr.isen.java2.db.entities;

/**
 * Genre is an entity class that represents a genre in the database.
 */
public class Genre {
	private Integer id;
	private String name;

	public Genre() {
	}

	public Genre(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
