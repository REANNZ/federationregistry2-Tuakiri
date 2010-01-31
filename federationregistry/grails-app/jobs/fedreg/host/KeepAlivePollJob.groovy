package fedreg.host

/*
* A simple job to poll the database every 4 hours to keep MySQL from dropping the Hibernate connection,
* this was the easiest method at the time given a lack of understanding of JNDI amongst many folks involved
*
* @author Bradley Beddoes
*/
class KeepAlivePollJob {

	static triggers = {
		cron name: 'keepAliveTrigger', cronExpression: "0 0 0/4 * * ?"  
	}

    def execute() {
		log.debug "Executing KeepAlivePoll via Quartz"
        def users = User.list()
    }
}
