/*
This is case of a quick script to get output it could be a lot prettier of course but time doesn't permit.

Used to generate AAF boxes report
Bradley Beddoes
2011
*/

import fedreg.core.*
import fedreg.reporting.*

// Configure here....
def sCount = 200
def idpCount = 5
long federationOrgID = 8  // exclude everything operated by the AAF (or federation using FR)
def year = 2011
def robot = false

// We're off and reporting...
println "Federated sessions for top $sCount services showing top $idpCount IdP sources for each. \nGenerated on ${new Date()}"

querySessionTotalParams = [:]
querySessionTotalParams.year = year
querySessionTotalParams.robot = robot
querySessionTotalParams.orgID = federationOrgID
def querySessionTotal = "select count(*) from WayfAccessRecord war, RoleDescriptor rd where war.spID = rd.id and rd.organization.id != :orgID and year(war.dateCreated) = :year and war.robot = :robot"

def sessionTotal = WayfAccessRecord.executeQuery(querySessionTotal, querySessionTotalParams)[0]
println "A total of $sessionTotal sessions have been recorded during $year to date (This report excludes services offered by the federation itself such as management tools)\n\n"

// Top X services
queryParams = [:]
queryParams.year = year
queryParams.robot = robot
queryParams.orgID = federationOrgID

//  and now some funky join hackery for your enjoyment...
def query = new StringBuilder("select count(*), war.spID from WayfAccessRecord war, RoleDescriptor rd where war.spID = rd.id and rd.organization.id != :orgID and year(war.dateCreated) = :year and war.robot = :robot group by war.spID order by count(war.spID) desc")
def sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams, [max:sCount])

sessions.each { s ->
  def sp = SPSSODescriptor.get(s[1])
  if(sp && !sp.displayName.toLowerCase().contains('test') && !sp.displayName.toLowerCase().contains('tst') && !sp.displayName.toLowerCase().contains('dev') && !sp.displayName.toLowerCase().contains('uat'))  {
    def sc = s[0]
    println "Session counts for SP ${sp.displayName}"
    println "Owner: ${sp.organization.name}"
    println "Total sessions recorded: ${sc} ( ${(sc/sessionTotal) * 100}% of all federated sessions ) "
    println "\nTop $idpCount sources"

    def queryIdP = new StringBuilder("select count(*), idpID from WayfAccessRecord where spID = :spid and year(dateCreated) = :year and robot = :robot group by idpID order by count(idpID) desc")
    def queryIdPParams = [:]
    queryIdPParams.spid = sp.id
    queryIdPParams.year = year
    queryIdPParams.robot = robot

    def idpSessions = WayfAccessRecord.executeQuery(queryIdP.toString(), queryIdPParams, [max:idpCount])
    def osc = sc
    idpSessions.each { idps ->
      def idp = IDPSSODescriptor.get(idps[1])
      if(idp) {
        lsc = idps[0]
        osc = osc - lsc
        println "${idp.displayName}: ${lsc} sessions (${(lsc/sc) * 100} %)"
      }
    }
    println "Other: ${osc} sessions (${(osc/sc) * 100} %)"
    
    println "\n--------\n"
  }
}

// Non top X services
osc = 0
sessions = WayfAccessRecord.executeQuery(query.toString(), queryParams, [offset:sCount])
sessions.each { s ->
  def sp = SPSSODescriptor.get(s[1])
  if(sp) {
    // At this time we do nothing with non top 20 except add up and output, leaving here for future ext
   osc = osc + s[0]
  }
}
println "Session total for all OTHER services: $osc ( ${(osc/sessionTotal) * 100}% of all federated sessions)"

true