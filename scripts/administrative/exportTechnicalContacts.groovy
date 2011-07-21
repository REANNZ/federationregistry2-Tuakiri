import fedreg.core.*

def unique = [], count = 0

def cpList = ContactPerson.list()
cpList.each { cp ->
  if(cp.type.name == 'technical') {
    if(!unique.contains(cp.contact.email.uri)){
      unique.add(cp.contact.email.uri)
      print "${cp.contact.givenName} ${cp.contact.surname} <${cp.contact.email.uri}>, "
      count++
    }
  }
}
  
println "\n\n ----------\n Completed exporting $count unique technical contacts from Federation Registry at ${new Date()} \n\n"
  
true