import org.codehaus.groovy.runtime.TimeCategory

import aaf.fr.reporting.*

/*
Bootstrap - Optional

This will create an example DS usage data to get a feel for the FR reporting capability.
*/

random = new Random()

use (TimeCategory) {
  def start = new Date()
  def end = start + 5.days

  (start..end).each { d ->
    rand = random.nextInt(301)
    println d

    (0..rand).each { c ->
      def war = new WayfAccessRecord(source:'1-0-0-127dummy.machine.com', requestType:'DS', dsHost:'ds01.test.aaf.edu.au',
        idpEntity:'https://idp.one.edu.au/idp/shibboleth', spEndpoint:'https://sp.one.edu.au/Shibboleth.sso/DS',
        idpID:3, spID:5, dateCreated:d)

      if(!war.save()) {
        war.errors.each { println it}
      }
    }
  }
}
