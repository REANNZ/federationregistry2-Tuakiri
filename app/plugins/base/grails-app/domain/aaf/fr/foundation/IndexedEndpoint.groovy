package aaf.fr.foundation

// While not marked explicitly due to GORM issues both Endpoint and IndexedEndpoint are considered 'Abstract'
abstract class IndexedEndpoint extends Endpoint  {
  static auditable = true

  boolean isDefault = false
  int index

  static mapping = {
    tablePerHierarchy false
    index column: "samlmd_index"
  }

  public String toString() { "indexedendpoint:[id:$id, location: $location]" }
}
