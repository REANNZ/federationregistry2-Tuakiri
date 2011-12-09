package fedreg.host

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
		
		out << r.img(dir:'images', file:'help.png', plugin:'federationregistry', title:msg, width:'16px', height:'16px', class:'tip')
    }

	def working = {
		out << "<div id='working'>${r.img(plugin:'federationregistry', dir:'images', file:'spinner.gif', width:'20px', height:'20px')}<br/>${g.message(code:'label.working')}</div>"
	}
}