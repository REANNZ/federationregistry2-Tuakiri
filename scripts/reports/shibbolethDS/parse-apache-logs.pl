#!/usr/bin/perl -w

use URI::Escape;
use DateTime::Format::HTTP;
use DBI;
use strict; # strongly recommended for DBI


$main::verbose=1;
$main::debug=1;


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


# Parse /var/log/httpd/ssl_access_log and extract information on sessions established at the DS
# look for records like:

# SAMLDS protocol
# 132.181.65.178 - - [19/Aug/2011:14:02:17 +1200] "GET /ds/DS?entityID=https%3A%2F%2Fregistry.test.tuakiri.ac.nz%2Fshibboleth&returnX=https%3A%2F%2Fregistry.test.tuakiri.ac.nz%2FShibboleth.sso%2FLogin%3FSAMLDS%3D1%26target%3Dss%253Amem%253A713c2c30c04d77baa0b295fcf6d1791f003740fe&returnIDParam=entityID&FedSelector=Tuakiri+TEST+Federation&action=selection&origin=https%3A%2F%2Fidp20test.canterbury.ac.nz%2Fidp%2Fshibboleth&cache=perm HTTP/1.1" 302 -

# SAML1 SSO request
# 132.181.65.178 - - [19/Aug/2011:15:03:06 +1200] "GET /ds/DS?target=cookie&shire=https%3A%2F%2Fvirtualhome.test.tuakiri.ac.nz%2FShibboleth.sso%2FSAML%2FPOST&providerId=https%3A%2F%2Fvirtualhome.test.tuakiri.ac.nz%2Fshibboleth&time=1313722980&cache=perm&action=selection&origin=https://idp20test.canterbury.ac.nz/idp/shibboleth HTTP/1.1" 302 -

while (<>) {
    if ( /^(\S+) \S+ \S+ \[([^\]]+)\] "GET [^\?]+\?([^" ]+) [^" ]+"/ ) {
	# Match should get:
	# IP address as $1
	# Timestamp as $2
	# Querystring as $3

	my $ipaddr_str = $1;
	my $timestamp_str = $2;
	my $query_str = $3;

	print "Found IP address $ipaddr_str Timestamp $timestamp_str query string $query_str\n" if ($main::debug>=2);

	# collect the information to record into DS_session hash (with key names matching the database schema)
	# note: we will also populate idp_name and sp_name which are just extra info for debugging)
	my %DS_session=();
	$DS_session{"source"} = $ipaddr_str;

	my $timestamp = $http_date_parser->parse_datetime($timestamp_str);
	$DS_session{"date_created"} = $timestamp; 
	# We need to parse the Apache log format... and DateTime::Format::HTTP supports exactly this.
	# MySQL expects: "value to be in the ODBC standard SQL_DATETIME format, which is ’YYYY-MM-DD HH:MM:SS’."
	# And it very happily accepts the format returned by this function: 2011-08-19T14:02:17


        # split the query string into individual key-value pairs
	my %query_keyvals = ();
	foreach my $query_keyval (split /\&/, $query_str) { 
            # and extract the and value
	    if ( $query_keyval =~ /^([^=]+)=(.*)$/ ) {
		$query_keyvals{$1}=$2;
	    };
	};

        # look for:
        # action=selection
	# SP: entityID=https%3A%2F%2Fregistry.test.tuakiri.ac.nz%2Fshibboleth
	# IdP: origin=https%3A%2F%2Fidp20test.canterbury.ac.nz%2Fidp%2Fshibboleth

	# SAML1 SSO request:
	# SP: providerId=https%3A%2F%2Fvirtualhome.test.tuakiri.ac.nz%2Fshibboleth

	# robot: we are not getting UserAgent header in SSL-VirtualHost log files, assume to be false?
	# TODO: start collecting UserAgent header in Apache logs
	# NOTE: It may be safe to assume robots would not pick an IdP, so we
	# would only see the robot downloading the wayf page but not making a
	# selection
	$DS_session{"robot"} = "";
	$DS_session{"ds_host"} = $main::local_hostname;

	# timestamp: ??? parse apache stamp?
	# request type: ??? DS/WAYF based on whether it's SAML1 SSO-request or SAML2 SAMLDS ?
	# source: parse user IP?

	if (defined($query_keyvals{"origin"})) { 
	    $DS_session{"idp_name"} = uri_unescape($query_keyvals{"origin"});
            my @IDres = &GetIdPID($DS_session{"idp_name"});
            if (scalar @IDres == 1) {
		$DS_session{"idpid"} = $IDres[0];
		print "Mapped IdP name " . $query_keyvals{"origin"} . " to IdP ID " . $DS_session{"idpid"} . "\n" if ($main::debug>=2);
	    } else { 
		print "IdP " . $query_keyvals{"origin"} . " not found in local FR\n" if ($main::verbose>=2);
	    };
        };

	if (defined($query_keyvals{"entityID"})) { 
	    $DS_session{"sp_name"} = uri_unescape($query_keyvals{"entityID"});
            my @IDres = &GetSPID($DS_session{"sp_name"});
            if (scalar @IDres == 1) {
		$DS_session{"spid"} = $IDres[0];
		$DS_session{"request_type"} = "DS Request"; 
		print "Mapped SP name " . $DS_session{"sp_name"} . " to SP ID " . $DS_session{"spid"} . "\n" if ($main::debug>=2);
	    } else { 
		print "SP " . $DS_session{"sp_name"} . " not found in local FR\n" if ($main::verbose>=2);
	    };
        };

	if (defined($query_keyvals{"providerId"})) { 
	    $DS_session{"sp_name"} = uri_unescape($query_keyvals{"providerId"});
            my @IDres = &GetSPID($DS_session{"sp_name"});
            if (scalar @IDres == 1) {
		$DS_session{"spid"} = $IDres[0];
		$DS_session{"request_type"} = "WAYF Request"; 
		print "Mapped SP name " . $DS_session{"sp_name"} . " to SP ID " . $DS_session{"spid"} . "\n" if ($main::debug>=2);
	    } else { 
		print "SP " . $DS_session{"sp_name"} . " not found in local FR\n" if ($main::verbose>=2);
	    };
        };

	# Do we have sufficient information about the session
	if (defined($DS_session{"idpid"}) && defined($DS_session{"spid"}) ) {
	    # insert the session into the database now

	    if ($main::verbose >= 1) {
		printf "Complete session found, inserting into database: date_created=\"%s\", ds_host=\"%s\", idpid=%d, request_type=\"%s\", robot=%d, source=\"%s\", spid=%d\n",
		    $DS_session{"date_created"}, $DS_session{"ds_host"}, $DS_session{"idpid"}, $DS_session{"request_type"}, $DS_session{"robot"}, $DS_session{"source"}, $DS_session{"spid"};
	    };

	    # based on example:
	    #
	    # insert into wayf_access_record (date_created, ds_host, idpid, request_type, robot, source, spid) 
	    #     values (curdate(), 'ds.aaf.edu.au', 1, 'DS', false, 'bradley-machine.at.home.com', 22);
	    $sth_wayf->execute($DS_session{"date_created"}, $DS_session{"ds_host"}, $DS_session{"idpid"}, 
	        $DS_session{"request_type"}, $DS_session{"robot"}, $DS_session{"source"}, $DS_session{"spid"} );
	} else {
	    if ($main::debug >= 2) {
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

