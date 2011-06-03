package fedreg.reporting

class ReportingHelpers {

	static def robots(def robot) {
		robot ? '':'and robot = false'
	}
	
	static def populateTotals(def year, def month, def day, def counts) {	
		def timeRange
		if(year && month && day){
		    timeRange = 0..23
		} else {
		    if(year && month) {
		        Calendar cal = new GregorianCalendar(year - 1, month - 1, 1)
		        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

		        timeRange = 1..days
		    } else {
		        timeRange = 1..12
		    }
		}
		
		populateTotals(timeRange, counts)
	}
	
	static def populateTotals(def timeRange, def counts) {
		def activeDates = [:]
		counts.each {
		    activeDates.put(it.t, it.c)
		}
		
		def totals = []
		def max = 0
		timeRange.each { t ->
		    def total = [:]
		    total.c = activeDates.get(t)?:0
		    total.t = t

		    if(max < total.c) {
		        max = total.c
		    }
		    totals.add(total)
		}
		
		[totals, max]
	}
	
	static def populateCumulativeTotals(def year, def month, def day, def base, def counts) {	
		def timeRange
		if(year && month && day){
		    timeRange = 0..23
		} else {
		    if(year && month) {
		        Calendar cal = new GregorianCalendar(year - 1, month - 1, 1)
		        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

		        timeRange = 1..days
		    } else {
		        timeRange = 1..12
		    }
		}
		
		populateCumulativeTotals(timeRange, base, counts)
	}
	
	static def populateCumulativeTotals(def timeRange, def base, def counts) {
		def activeDates = [:]
		counts.each {
		    activeDates.put(it.t, it.c)
		}
		
		def count = base
		def totals = []
		def max = 0
		timeRange.each { t ->
			count = count + (activeDates.get(t)?:0)
		    def total = [:]
		    total.c = count
		    total.t = t

		    if(max < total.c) {
		        max = total.c
		    }
		    totals.add(total)
		}
		
		[totals, max]
	}
	
	static def populateCumulativeTotals(def base, def counts) {		
		def count = base
		def totals = []
		def max = 0
		
		counts.eachWithIndex { it, i ->			
			count = count + it.c
		    def total = [:]
		    total.c = count
		    total.t = it.t

		    if(max < total.c) {
		        max = total.c
		    }
		    totals.add(total)
		}
		
		[totals, max]
	}

}