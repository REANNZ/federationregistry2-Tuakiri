package fedreg.host

class UITagLib {

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
		def id = ""
		if(attrs.id) 
			id = "id = ${attrs.id}"
	
		def onclick = ""
		if(attrs.onclick)
			onclick = "onClick= \"${attrs.onclick}\""
		
		def href = ""
		if(attrs.href)
			href = "href=\'${attrs.href}\'"
		
		def cssclass = ""
		if(attrs.class)
			cssclass = "class=\'${attrs.class}\'"
		
		out << "<a $href $onclick $id class='ui-button $type ui-widget ui-state-default ui-corner-all $cssclass' >"
		out << "<span class='ui-button-icon-primary ui-icon ui-icon-${attrs.icon}'></span>"
		out << "<span class='ui-button-text'>${attrs.label}</span>"
		out << "</a>"
	}
	
	def confirmaction = { attrs, body ->
		if(attrs.action == null || attrs.title == null || attrs.msg == null || attrs.accept == null || attrs.cancel == null)
        	throwTagError("Confirm action tag requires action, title, msg, accept and cancel attributes")

		def btnAttrs = [:]
		btnAttrs.id = attrs.id
		btnAttrs.class = attrs.class
		btnAttrs.onclick = "confirmAction = function() { ${attrs.action} }; fedreg.wasConfirmed('${attrs.title}', '${attrs.msg}', '${attrs.accept}', '${attrs.cancel}');"
		btnAttrs.label = attrs.label
		btnAttrs.icon = attrs.icon

		out << button(btnAttrs)
	}

}
