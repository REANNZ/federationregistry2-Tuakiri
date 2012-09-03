package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class Descriptor  {
	static auditable = true
	
	static mapping = {
		tablePerHierarchy false
	}
	
	public String toString() {	"descriptor:[id:$id]" }
}
