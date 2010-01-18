package fedreg.core

class AttributeScope {
	
	String name

    static constraints = {
		name (blank:false)
    }

	public String toString() {
		return name
	}
}
