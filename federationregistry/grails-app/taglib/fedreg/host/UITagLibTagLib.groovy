package fedreg.host

class UITagLibTagLib {

	static namespace = 'fr'
	
	def button = { attrs -> 
		def type
		if(attrs.label && attrs.icon)
			type = 'ui-button-text-icon'
			else
				if(attrs.label)
					type = 'ui-button-text-only'
					else
						type = 'ui-button-icon-only'
		out << "<a id=\"${attrs.id}\" href=\"${attrs.href}\" class='ui-button ${type} ui-widget ui-state-default ui-corner-all' onClick=\"${attrs.onclick}\" class=\"${attrs.class}\" >"
		out << "<span class='ui-button-icon-primary ui-icon ui-icon-${attrs.icon}'></span>"
		out << "<span class='ui-button-text'>${attrs.label}</span>"
		out << "</a>"
	}

}
