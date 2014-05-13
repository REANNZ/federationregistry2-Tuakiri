#!/usr/bin/perl -w

use URI::Escape;
use DateTime;
use DBI;
use strict; # strongly recommended for DBI


$main::verbose=1;
$main::debug=1;
$main::dryrun=0;

while ($ARGV[0] =~ /^--/ ) {
  if ($ARGV[0] eq "--verbose") { $main::verbose += 1; }
  elsif ($ARGV[0] eq "--no-verbose") { $main::verbose=0; }
  elsif ($ARGV[0] eq "--debug") { $main::debug += 1; }
  elsif ($ARGV[0] eq "--no-debug") { $main::debug=0; }
  elsif ($ARGV[0] eq "--dry-run") { $main::dryrun=1; }
  elsif ($ARGV[0] eq "--no-dry-run") { $main::dryrun=0; }
  else { die "Invalid option $ARGV[0]"; };
  
  shift;
}


$main::local_hostname = `hostname`; # Override here - used for populating ds_host in wayf_access_record.
chomp $main::local_hostname;

# Initialaze connection to FR database
# set the the connection parameters either here or load them from dbconfig.pm
$main::database = '';
$main::hostname = '';
$main::user = '';
$main::password = '';

# load the config from the directory we are invoked from
# the c
my $base_dir = `dirname $0`;
chomp $base_dir;
do ($base_dir ne ""?$base_dir:".") . "/dbconfig.pm";

my $dsn = "DBI:mysql:database=$main::database;host=$main::hostname;mysql_enable_utf8=1";
#$dsn = "DBI:mysql:database=$database;host=$hostname;port=$port";
my $dbh = DBI->connect($dsn, $main::user, $main::password, { RaiseError => 1, AutoCommit => 0 });

my $sth_sp  = $dbh->prepare("select spssodescriptor.id from spssodescriptor,entity_descriptor where spssodescriptor.entity_descriptor_id = entity_descriptor.id and entity_descriptor.entityid = ?");
my $sth_sp_dsr  = $dbh->prepare("select discovery_response_service.descriptor_id from discovery_response_service, endpoint where discovery_response_service.id = endpoint.id and endpoint.location = ?");
my $sth_idp = $dbh->prepare("select idpssodescriptor.id from idpssodescriptor,entity_descriptor where idpssodescriptor.entity_descriptor_id = entity_descriptor.id and entity_descriptor.entityid = ?");
my $sth_wayf = $dbh->prepare("insert into wayf_access_record (date_created, ds_host, idpid, request_type, robot, source, spid, idp_entity, sp_endpoint) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

my $http_date_parser = 'DateTime::Format::HTTP';

sub GetIdPID {
  my @results = ();

  $sth_idp->execute(@_);
  while (my $ref = $sth_idp->fetchrow_hashref()) {
    push @results, $ref->{'id'};
  };
  return @results;
}

sub GetSPID_from_DSR {
  my @results = ();

  $sth_sp_dsr->execute(@_);
  while (my $ref = $sth_sp_dsr->fetchrow_hashref()) {
    push @results, $ref->{'descriptor_id'};
  };
  return @results;
}

sub GetSPID {
  my @results = ();

  $sth_sp->execute(@_);
  while (my $ref = $sth_sp->fetchrow_hashref()) {
    push @results, $ref->{'id'};
  };
  return @results;
}


# Parse /opt/shibboleth-ds/logs/discoveryService.log and extract information on sessions established at the DS
# look for records like:

# 14:58:39.760 - INFO [edu.internet2.middleware.shibboleth.wayf.DiscoveryServiceHandler:963] - Session established: 1314845919;https%3A%2F%2Fidp20test.canterbury.ac.nz%2Fidp%2Fshibboleth;https%3A%2F%2Fregistry.test.tuakiri.ac.nz%2Fshibboleth;DS Request;132.181.65.178;Mozilla%2F5.0+%28X11%3B+Linux+x86_64%3B+rv%3A5.0%29+Gecko%2F20100101+Firefox%2F5.0
# 15:00:42.131 - INFO [edu.internet2.middleware.shibboleth.wayf.DiscoveryServiceHandler:963] - Session established: 1314846042;https%3A%2F%2Fidp20test.canterbury.ac.nz%2Fidp%2Fshibboleth;https%3A%2F%2Fwiki.test.bestgrid.org%2Fshibboleth;WAYF Request;132.181.65.178;Mozilla%2F5.0+%28X11%3B+Linux+x86_64%3B+rv%3A5.0%29+Gecko%2F20100101+Firefox%2F5.0
# 15:21:09.318 - INFO [edu.internet2.middleware.shibboleth.wayf.DiscoveryServiceHandler:970] - Session established: 1314847269;https%3A%2F%2Fidp20test.canterbury.ac.nz%2Fidp%2Fshibboleth;https%3A%2F%2Fwiki.test.bestgrid.org%2Fshibboleth;WAYF Cookie;132.181.65.178;Mozilla%2F5.0+%28X11%3B+Linux+x86_64%3B+rv%3A5.0%29+Gecko%2F20100101+Firefox%2F5.0
# 15:22:14.721 - INFO [edu.internet2.middleware.shibboleth.wayf.DiscoveryServiceHandler:970] - Session established: 1314847334;https%3A%2F%2Fidp20test.canterbury.ac.nz%2Fidp%2Fshibboleth;https%3A%2F%2Fregistry.test.tuakiri.ac.nz%2Fshibboleth;DS Cookie;132.181.65.178;Mozilla%2F5.0+%28X11%3B+Linux+x86_64%3B+rv%3A5.0%29+Gecko%2F20100101+Firefox%2F5.0

# Modification: parse SWITCH/AAF DS WAYFLogFile (/var/log/wayf.log) and extract records like:
# 2012-11-01 18:07:50 182.255.120.126 DS Request https://idpdev.canterbury.ac.nz/idp/shibboleth https://virtualhome.test.tuakiri.ac.nz/Shibboleth.sso/Login?SAMLDS=1&discoveryURL=https://directory.test.tuakiri.ac.nz/discovery/DS&target=ss%3Amem%3A2cde6166c4570bce022cdba3ddbcbf30c7d5ae7324a60eb55e8e0cdce12eed90
# Created with:
#                        $entry = date('Y-m-d H:i:s').' '.$_SERVER['REMOTE_ADDR'].' '.$protocol.' '.$type.' '.$idp.' '.$sp."\n";
# logAccessEntry in functions.php has signature: function logAccessEntry($protocol, $type, $sp, $idp)
# and is invoked as:
#                 logAccessEntry('DS', 'Cookie', $_GET['return'], $cookieIdP);
#                 logAccessEntry('WAYF', 'Cookie', $_GET['shire'], $cookieIdP);
#                 logAccessEntry('DS', 'Kerberos', $_GET['return'], $kerberosIDP);
#                 logAccessEntry('WAYF', 'Kerberos', $_GET['shire'], $kerberosIDP);
#                 logAccessEntry('WAYF', 'Old-Request', $_GET['shire'], $_GET['origin']);
#                 logAccessEntry('DS', 'Path', $_GET['return'], $hintedPathIDP);
#                 logAccessEntry('WAYF', 'Path', $_GET['shire'], $hintedPathIDP);
#                 logAccessEntry('Embedded-DS', 'Request', $_GET['return'], $selectedIDP);
#                 logAccessEntry('DS', 'Request', $_GET['return'], $selectedIDP);
#                 logAccessEntry('Embedded-WAYF', 'Request', $_GET['shire'], $selectedIDP);
#                 logAccessEntry('WAYF', 'Request', $_GET['shire'], $selectedIDP);
#                 logAccessEntry('DS', 'Passive', $_GET['return'], '-');
#                 logAccessEntry('DS', 'Passive', $_GET['return'], $selectedIDP);
# NOTE: these invocations are from SWITCH-only WAYF.  AAF adds more options
# But, it looks that:
#   $protocol can be DS, WAYF or Embedded-DS
#   $type can be Cookie, Kerberos, Old-Request, Path, Request, Passive
#   $sp seems to be the return path to the SP (DSR endpoint with query string), not the SP entityID
#   $idp should be the IdP entityID
# For SP, we would need to either: 
#   (1) do an evil hack and transform the DSR URL into entityID as string
#   (2) look the entity up by the endpoint (we need the numeric ID anyway)


while (<>) {
	# $_ should be a space-delimited line with:
	#   date, time, remote-addr, protocol (DS/WAYF/Embedded-DS), type, idp(entityID), sp
        #   date_created, idpname, spname, request_type, remoteHost, user-agent
        # No fields are URL-encoded.

	# Collect the information to record into DS_session hash (with key names matching the database schema)
	# Note: we will also populate idp_name and sp_name which are just extra info for debugging)
	my %DS_session=();
	my ($date_str, $time_str, $ds_protocol, $ds_type, $sp_dsr_url);
        ( $date_str, $time_str, $DS_session{"source"}, $ds_protocol, $ds_type, $DS_session{"idp_name"}, $DS_session{"sp_dsr_url"}) = split / /,$_;

	$DS_session{"request_type"} = "$ds_protocol $ds_type";

	# Timestamp MySQL expects: "value to be in the ODBC standard SQL_DATETIME format, which is ’YYYY-MM-DD HH:MM:SS’."
        # And it very happily accepts the format returned by this function: 2011-08-19T14:02:17
	
	# But we need this to be in UTC - while the wayf.log time stamp is in local time.

	# Therefore: first construct a new DateTime object in local timezone
	# Then set timezone to UTC

	my ($date_year, $date_month, $date_day) = split /-/,$date_str;
	my ($time_hour, $time_minute, $time_second) = split /:/,$time_str;
	my $dt = new DateTime( year => $date_year, month => $date_month, day => $date_day, hour => $time_hour, minute => $time_minute, second => $time_second, time_zone => "local");
	# We want to store the time in the local time zone - so do not force a conversion to UTC
        # DO NOT: $dt->set_time_zone("UTC"); 
        $DS_session{"date_created"} = $dt;

	# robot: wild guess based on UserAgent header
	# NOTE: It may be safe to assume robots would not pick an IdP, so we
	# would only see the robot downloading the wayf page but not making a
	# selection
	# NOTE: with SWITCH WAYF, we do not have a UserAgent field so cannot even make a guess on that.
	$DS_session{"robot"} = ""; # empty string translates as MySQL false for MySQL boolean type
	$DS_session{"ds_host"} = $main::local_hostname;

	if (defined($DS_session{"idp_name"})) { 
            my @IDres = &GetIdPID($DS_session{"idp_name"});
            if (scalar @IDres == 1) {
		$DS_session{"idpid"} = $IDres[0];
		print "Mapped IdP name " . $DS_session{"idp_name"} . " to IdP ID " . $DS_session{"idpid"} . "\n" if ($main::debug>=2);
	    } else { 
		print "IdP " . $DS_session{"idp_name"} . " not found in local FR\n" if ($main::verbose>=2);
	    };
        };

        # trim SP URL - remove the query string (to get pure DSR endpoint)
        if ( $DS_session{"sp_dsr_url"} =~ /^([^\?]*)\?/ ) {
            $DS_session{"sp_dsr_url"} = $1;
        };

        # TODO: we need a new SQL query to get sp_id from DSR endpoint, not from entityID
	# SPSSODescriptor has relation: discoveryResponseServices: DiscoveryResponseService
	# class DiscoveryResponseService extends IndexedEndpoint
	# class IndexedEndpoint extends Endpoint
	# Endpoint has: UrlURI location; UrlURI responseLocation; 
	# Query is now in sp_sth_dsr - see dsr_url_to_sp_id.txt for full details

	if (defined($DS_session{"sp_dsr_url"})) { 
            my @IDres = &GetSPID_from_DSR($DS_session{"sp_dsr_url"});
            if (scalar @IDres == 1) {
		$DS_session{"spid"} = $IDres[0];
		print "Mapped SP DSR endpoint " . $DS_session{"sp_dsr_url"} . " to SP ID " . $DS_session{"spid"} . "\n" if ($main::debug>=2);
	    } else { 
		print "SP DSR endpoint " . $DS_session{"sp_dsr_url"} . " not found in local FR\n" if ($main::verbose>=2);
	    };
        };

	# Do we have sufficient information about the session
	if (defined($DS_session{"idpid"}) && defined($DS_session{"spid"}) ) {
	    # insert the session into the database now

	    if ($main::verbose >= 1) {
		printf "Complete session found, %s into database: date_created=\"%s\", ds_host=\"%s\", idpid=%d, request_type=\"%s\", robot=%d, source=\"%s\", spid=%d\n",
                    ( $main::dryrun ? "pretending to insert" : "inserting" ),
		    $DS_session{"date_created"}, $DS_session{"ds_host"}, $DS_session{"idpid"}, $DS_session{"request_type"}, ( $DS_session{"robot"} ? 1 : 0), $DS_session{"source"}, $DS_session{"spid"};
	    };

	    # based on example:
	    #
	    # insert into wayf_access_record (date_created, ds_host, idpid, request_type, robot, source, spid) 
	    #     values (curdate(), 'ds.aaf.edu.au', 1, 'DS', false, 'bradley-machine.at.home.com', 22);
            if (!$main::dryrun) {
		$sth_wayf->execute($DS_session{"date_created"}, $DS_session{"ds_host"}, $DS_session{"idpid"}, 
		    $DS_session{"request_type"}, $DS_session{"robot"}, $DS_session{"source"}, $DS_session{"spid"},
		    $DS_session{"idp_name"}, $DS_session{"sp_dsr_url"} );
            };
	} else {
	    if ($main::debug >= 1) {
		print "Incomplete session found:\n";
		foreach my $key (keys(%DS_session)) {
		    print "Key: \"$key\" value: \"$DS_session{$key}\"\n";
		};
	    };
	}

} # while (<>)

$dbh->commit;
$dbh->disconnect;

