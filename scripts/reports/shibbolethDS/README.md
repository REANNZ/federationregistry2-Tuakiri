This script can parse logging created by the Shibboleth DS project and inject records into the Federation Registry wayf_access_records for further presentation in the FR reporting user interface.

This was kindly provided by the [Tuakiri, the New Zealand Access Federation] [1]

The scripts and other files in this directory are:

* parse-aafds-logs.pl: parse logs produced by the AAF/SWITCH DS
* parse-apache-logs.pl: parse logs produced by Shibboleth Project Centralized DS
* parse-ds-logs.pl: parse logs produced by the Tuakiri DS (based on Shibboleth Project Centralized DS - see [University-of-Auckland/Shibboleth-Discovery-Service-Tuakiri/] [2])
* dbconfig.pm: configuration file (primarily with database connection details) used by all of the three above parsers
* README.md: this README file
* Remote-Log-Fetching.md: documentation on the log shipping solution developed by [Tuakiri] [1]
* pull-remote-logs.sh: script implementing the above log shipping solution
* pull-remote-logs.conf: configuration file for the above script
* aafds: logrotate configuration file for AAF/SWITCH DS (suitable for use with parse-aafds-logs.pl)
* push-aafds-logs.sh: wrapper script for invoking parse-aafds-logs.pl, suitable for invocation from the above aafds logrotate configurationfile
* push-ds-logs.sh: wrapper script for invoking parse-ds-logs.pl, suitable for invocation from cron
* pull-aafds-logs.sh: an alternative implementation for remote fetching of just AAF/DS logs; invokes parse-aafds-logs.pl
* pull-aafds.conf: configuration file for pull-aafds-logs.sh
* ssl_access_log_sample: sample input file for parse-apache-logs.pl

[1]: https://tuakiri.ac.nz/
[2]: https://github.com/University-of-Auckland/Shibboleth-Discovery-Service-Tuakiri/
