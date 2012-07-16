package aaf.fr

/**
 * Provides reusable tags for the wider FR user interface
 *
 * @author Bradley Beddoes
 */
class FRTagLib {

  static namespace = "fr"

  def tooltip = { attrs ->
    def msg = g.message(code:attrs.code)
    def src = g.resource()
    
    out << r.img(dir:'images', file:'help.png', plugin:'federationregistry', title:msg, width:'16px', height:'16px', rel:'tooltip', 'data-placement':'right')
  }
}