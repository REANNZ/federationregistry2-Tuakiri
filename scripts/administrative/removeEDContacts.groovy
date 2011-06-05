import fedreg.core.*

/*
Removes all contacts from Entity Descriptors - AAF no longer going to publish contacts within ED

Bradley Beddoes - 3/6/2011
*/

def commit = true

def contacts = []
EntityDescriptor.list().each { ed ->
  ed.contacts.each { cp ->
    contacts.add(cp)
  }
  
  if(commit) {
    contacts.each { cp ->
      ed.removeFromContacts(cp)
    }
    
    if(!ed.save()) {
      ed.errors.each { println it }
    }
  }
  
  contacts.removeAll()
}

  
true