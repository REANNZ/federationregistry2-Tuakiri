package aaf.fr.foundation

import org.apache.shiro.SecurityUtils

/**
 * Provides some reusable tags for the FR user interface
 *
 * @author Bradley Beddoes
 */
class TagLib {

  static namespace = "fr"

  def attributeSelection = {
    def provideAdminRestricted = SecurityUtils.subject.isPermitted("federation:management:attributes:restricted")
    def markup = new StringBuffer()

    markup << "<select name='attrid' class='span4'>"

    def attributeList = [] as List

    def attributeCategories = AttributeCategory.list()
    attributeCategories.each { category ->
      markup << "<optgroup label='${category.name.encodeAsHTML()}'>"
      
      def categoryAttributes = AttributeBase.findAllWhere(category:category).sort{it.name}
      categoryAttributes.each {
        if(!it.adminRestricted || provideAdminRestricted)
        markup << "<option value='${it.id}'>${it.name.encodeAsHTML()} (${it.oid.encodeAsHTML()})</option>"
      }
      
    }
    markup << "</select>"
    out << markup
  }

}
