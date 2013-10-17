#!/usr/bin/perl -w

use URI::Escape;
use DateTime;
use DBI;
use strict; # strongly recommended for DBI


$main::verbose=1;
$main::debug=1;
$main::dryrun=0; # if set to 1, skip writing to the database


$main::local_hostname = `hostname`; # Override here - used for populating ds_host in wayf_access_record.
chomp $main::local_hostname;

# Initialaze connection to FR database
# set the the connection parameters either here or load them from dbconfig.pm
$main::database = '';
$main::hostname = '';
$main::user = '';
$main::password = '';

$main::check_is_spam = '';

# load the config from the directory we are invoked from
# the c
my $base_dir = `dirname $0`;
chomp $base_dir;
do ($base_dir ne ""?$base_dir:".") . "/dbconfig.pm";

while ($ARGV[0] =~ /^--/ ) {
  if ($ARGV[0] eq "--verbose") { $main::verbose=1; }
  elsif ($ARGV[0] eq "--no-verbose") { $main::verbose=0; }
  elsif ($ARGV[0] eq "--debug") { $main::debug=1; }
  elsif ($ARGV[0] eq "--no-debug") { $main::debug=0; }
  elsif ($ARGV[0] eq "--dry-run") { $main::dryrun=1; }
  elsif ($ARGV[0] eq "--no-dry-run") { $main::dryrun=0; }
  else { die "Invalid option $ARGV[0]"; };
  
  shift;
}

my $dsn = "DBI:mysql:database=$main::database;host=$main::hostname;mysql_enable_utf8=1";
#$dsn = "DBI:mysql:database=$database;host=$hostname;port=$port";
my $dbh = DBI->connect($dsn, $main::user, $main::password, { RaiseError => 1, AutoCommit => 0 });

my $sth_sp  = $dbh->prepare("select spssodescriptor.id from spssodescriptor,entity_descriptor where spssodescriptor.entity_descriptor_id = entity_descriptor.id and entity_descriptor.entityid = ?");
my $sth_idp = $dbh->prepare("select idpssodescriptor.id from idpssodescriptor,entity_descriptor where idpssodescriptor.entity_descriptor_id = entity_descriptor.id and entity_descriptor.entityid = ?");
my $sth_wayf = $dbh->prepare("insert into wayf_access_record (date_created, ds_host, idpid, request_type, robot, source, spid) values (?, ?, ?, ?, ?, ?, ?)");

my $http_date_parser = 'DateTime::Format::HTTP';

sub GetIdPID {
  my @results = ();

  $sth_idp->execute(@_);
  while (my $ref = $sth_idp->fetchrow_hashref()) {
    push @results, $ref->{'id'};
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

while (<>) {
    if ( /Session established: (.*)$/ ) {
	# Match should get a ";"-separated session record:
        #   date_created, idpname, spname, request_type, remoteHost, user-agent
        # The following fields are URL-encoded: idpname, spname, user-agent
        # The date_created field is time since epoch


	# Collect the information to record into DS_session hash (with key names matching the database schema)
	# Note: we will also populate idp_name and sp_name which are just extra info for debugging)
	my %DS_session=();
        ( $DS_session{"date_created"}, $DS_session{"idp_name"}, $DS_session{"sp_name"}, $DS_session{"request_type"}, $DS_session{"source"}, $DS_session{"user_agent"} ) = split /;/,$1;

        $DS_session{"date_created"} = DateTime->from_epoch( epoch => $DS_session{"date_created"} );
	# Timestamp MySQL expects: "value to be in the ODBC standard SQL_DATETIME format, which is ’YYYY-MM-DD HH:MM:SS’."
	# And it very happily accepts the format returned by this function: 2011-08-19T14:02:17

	# Timezone: store the date as a local time (not UTC) in MySQL
	$DS_session{"date_created"}->set_time_zone("local");

	# robot: wild guess based on UserAgent header
	# NOTE: It may be safe to assume robots would not pick an IdP, so we
	# would only see the robot downloading the wayf page but not making a
	# selection
	$DS_session{"robot"} = ""; # empty string translates as MySQL false for MySQL boolean type
	$DS_session{"ds_host"} = $main::local_hostname;

	# timestamp: ??? parse apache stamp?
	# request type: ??? DS/WAYF based on whether it's SAML1 SSO-request or SAML2 SAMLDS ?
	# source: parse user IP?

	if (defined($DS_session{"idp_name"})) { 
	    $DS_session{"idp_name"} = uri_unescape($DS_session{"idp_name"});
            my @IDres = &GetIdPID($DS_session{"idp_name"});
            if (scalar @IDres == 1) {
		$DS_session{"idpid"} = $IDres[0];
		print "Mapped IdP name " . $DS_session{"idp_name"} . " to IdP ID " . $DS_session{"idpid"} . "\n" if ($main::debug>=2);
	    } else { 
		print "IdP " . $DS_session{"idp_name"} . " not found in local FR\n" if ($main::verbose>=2);
	    };
        };

	if (defined($DS_session{"sp_name"})) { 
	    $DS_session{"sp_name"} = uri_unescape($DS_session{"sp_name"});
            my @IDres = &GetSPID($DS_session{"sp_name"});
            if (scalar @IDres == 1) {
		$DS_session{"spid"} = $IDres[0];
		print "Mapped SP name " . $DS_session{"sp_name"} . " to SP ID " . $DS_session{"spid"} . "\n" if ($main::debug>=2);
	    } else { 
		print "SP " . $DS_session{"sp_name"} . " not found in local FR\n" if ($main::verbose>=2);
	    };
        };

	# Do we have sufficient information about the session
	if (defined($DS_session{"idpid"}) && defined($DS_session{"spid"}) ) {
	    # insert the session into the database now

            # check whether the session passes the spam filter (if we have a filter set)
            if ( ($main::check_is_spam) && (eval $main::check_is_spam ) ) {
                if ($main::verbose >= 1) {
                    printf "Found spam session from %s\n", $DS_session{"source"} ;
                };
                next;
            };

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
	            $DS_session{"request_type"}, $DS_session{"robot"}, $DS_session{"source"}, $DS_session{"spid"} );
            };
	} else {
	    if ($main::debug >= 1) {
		print "Incomplete session found:\n";
		foreach my $key (keys(%DS_session)) {
		    print "Key: \"$key\" value: \"$DS_session{$key}\"\n";
		};
	    };
	}

    }; # if (/Apache pattern/)
} # while (<>)

$dbh->commit;
$dbh->disconnect;

