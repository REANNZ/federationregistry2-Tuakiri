package aaf.fr.admin

import org.codehaus.groovy.runtime.TimeCategory

import grails.plugins.federatedgrails.*
import aaf.fr.identity.*

class AdminDashboardController {
  
  def index = {
    def subjectCount = Subject.count()  
    def roleCount = Role.count()
    def permCount = Permission.count()

    def lastHourSessions, lastDaySessions, lastWeekSessions
    use(TimeCategory) {
      def queryParams = [:]
      queryParams.endDate = new Date()
      queryParams.startDate = queryParams.endDate - 1.hour

      lastHourSessions = SessionRecord.executeQuery("select count(*) from SessionRecord where dateCreated between :startDate and :endDate", queryParams)[0]

      queryParams.startDate = queryParams.endDate - 1.day
      lastDaySessions = SessionRecord.executeQuery("select count(*) from SessionRecord where dateCreated between :startDate and :endDate", queryParams)[0]

      queryParams.startDate = queryParams.endDate - 1.week
      lastWeekSessions = SessionRecord.executeQuery("select count(*) from SessionRecord where dateCreated between :startDate and :endDate", queryParams)[0]
    }

    [subjectCount:subjectCount, roleCount:roleCount, permCount:permCount, lastHourSessions:lastHourSessions, lastDaySessions:lastDaySessions, lastWeekSessions:lastWeekSessions]
  }

}