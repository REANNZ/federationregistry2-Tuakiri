package fedreg.host

import aaf.fr.foundation.AttributeBase
import aaf.fr.foundation.AttributeCategory

/**
 * Provides some reusable tags for the FR user interface
 *
 * @author Bradley Beddoes
 */
class FedRegTagLib {

  static namespace = "fr"

  def tooltip = { attrs ->
    def msg = g.message(code:attrs.code)
    def src = g.resource()
    
    out << r.img(dir:'images', file:'help.png', plugin:'federationregistry', title:msg, width:'16px', height:'16px', rel:'twipsy', 'data-placement':'right')
  }
}