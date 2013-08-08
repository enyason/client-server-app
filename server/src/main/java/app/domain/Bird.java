package app.domain;

import java.io.Serializable;

public class Bird implements Serializable,Comparable<Object> {
	
	private static final long serialVersionUID = -1255785555991940855L;

	private String name;
	
	private String color;
	
	private int weight;
	
	private int height;
	
	
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
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public boolean equals(Object arg0) {
		return this.getName().equals(((Bird)arg0).getName());
	}
	
	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
	
	@Override
	public int compareTo(Object arg0) {
		Bird b0 = (Bird)arg0;
		return this.getName().compareTo(b0.getName());
	}


}
