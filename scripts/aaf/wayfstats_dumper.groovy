/* 
Dumps dodgy smith tables back into original logging format (which in itself is dodgy) so it can be re-imported to new, clean, sexy FR format 421740

Bradley Beddoes
*/

import groovy.sql.Sql

def year = "2009"
def host = "ds.arcs.org.au"

sql = Sql.newInstance("jdbc:mysql://localhost:3306/wayf", "sucker", "password", "com.mysql.jdbc.Driver")

def f = new File("/tmp/${year}-${host}.aaf.log")
sql.eachRow("select date, source, destin, provider from wayf_stats where year(date) = $year and host = ${host}") { f.append( "${it.date.format('yyyy-MM-dd HH:mm:ss')} $it.source DS Request $it.provider $it.destin\n") }
