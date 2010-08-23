
	import fedreg.core.*

	/*
	 Initial attribute definition and population for Federation Registry
	*/
	
	def coreCategory = new AttributeCategory(name:'Core').save()
	def optionalCategory = new AttributeCategory(name:'Optional').save()