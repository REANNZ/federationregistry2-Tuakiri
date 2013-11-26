# database config for parse-apache-logs.pl

# initialaze connection to FR database
$main::database = "";
$main::hostname = "";
$main::user = "";
$main::password = "";

# extra config for parse-ds-logs.pl

# configure a rule (an expression to be used with eval) to determine whether a session is spam
#
# Real life example: SP id 24 (a MediaWiki) is getting lots of spam-bot
# connections that try to sign up for an account and traverse through the DS
# (Tuakiri / ShibbolethDS), selecting the first IdP in alphabetical order (ID 45).
#
# Without any better option to filter out the spammers, filter out all
# connections that are not originating from the IP range of the institution
# the IdP belongs to.  (Substitute all of the values to match your environment)
#
# As perl expression:

# $main::check_is_spam = '($DS_session{"idpid"} == 45) && ($DS_session{"spid"} == 24) && ( $DS_session{"source"} !~ /^192\.168\./ )';

