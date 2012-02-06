package aaf.fr.foundation

/**
 * Provides some reusable tags for the FR user interface
 *
 * @author Bradley Beddoes
 */
class TagLib {

  static namespace = "fr"

  def attributeSelection = {
    def markup = new StringBuffer()

    markup << "<select name='attrid' class='span4'>"

    def attributeList = [] as List

    def attributeCategories = AttributeCategory.list()
    attributeCategories.each { category ->
      markup << "<optgroup label='${category.name}'>"
      
      def categoryAttributes = AttributeBase.findAllWhere(category:category).sort{it.name}
      categoryAttributes.each {
        markup << "<option value='${it.id}'>${it.name} (${it.oid})</option>"
      }
      
    }
    markup << "</select>"
    out << markup
  }

}