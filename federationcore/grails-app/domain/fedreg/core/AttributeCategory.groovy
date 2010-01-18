package fedreg.core

class AttributeCategory {
	
	String name

    static constraints = {
		name (blank:false)
    }

	public String toString() {
		return name
	}
}
