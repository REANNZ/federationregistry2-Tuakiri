The Federation Registry (FR) has also features for tracking federation usage (sessions between IdPs and SPs) and visualizing such usage.  For these features to be any useful, the FR database (table wayf_access_record) must be populated with actual session data.  The easiest to reach source of session data are the logs from the Discovery Service (DS).  The DS logs are slightly incomplete (do not cover sessions established outside of the centralized DS) and slightly incorrect (they would also include selections of an IdP that did not result into an actual session - e.g., the user did not authenticate at the IdP), but are a good starting point.

There are several options for getting the DS logs into the FR database - one of them is running the parsers on the DS directly.  However, this means granting the DS machine direct access to the FR database.  And that may not be suitable / scalable when running multiple DS instances, possibly across different administrative domains (and located in remote networks).

The scripts pull-remote-logs.sh provides an alternative solution: Remote Log Fetching.  This solution consists of the following parts:

* Each Discovery Service exports it's logs at an HTTPS URL requiring authenticated access
* A centralized host (for the time being the Federation Registry itself) periodically scans this directory for new logs, downloads them, and runs the parser that ingests them into the FR database.
  * The parsers currently included here support the AAF/SWITCH DS (parse-aafds-logs.pl) and the Tuakiri DS (based on Shibboleth project Centralized Discovery Service) - parse-ds-logs.pl

These steps are described in detail in the following sections.

1. Exporting logs from a Discovery Service machine
2. Importing DS session logs from remote sources

# Exporting logs from a Discovery Service machine

The exact instructions may vary depending on the DS implementation used.  We will use as an example the case of the Tuakiri DS, which is collecting logs in the /opt/shibboleth-ds/logs directory - first into the discoveryService.log file, with daily automatic rotation (triggered by first log message after midnight) renaming the file to a file name based on "yesterday's date" following the template "discovery-YYYY-MM-DD.log".

We want to export this directory as https://ds.host.name/dslogs/ - so run the following command as root:

    ln -s /opt/shibboleth-ds/logs /var/www/html/dslogs

We want to protect the URL with a username and password.  In this example, we will use logaccess as the username and generate a password with:

    openssl rand -base64 12

And create an Apache password file as /etc/httpd/conf/logaccess.passwd by running the Apache htdigest command (and entering the password generated above when prompted):

    htdigest -c /etc/httpd/conf/logaccess.passwd LogAccess logaccess

And configure Apache to track protect this URL by creating /etc/httpd/conf.d/ds-logs.conf with the following contents (instructing Apache to hide the current not-yet-rotated log file from the listing):

````
<Directory /var/www/html/dslogs>
  AuthType Digest
  AuthUserfile /etc/httpd/conf/logaccess.passwd
  AuthName LogAccess
  AuthDigestDomain /dslogs/
  require valid-user
  Options +Indexes
  IndexIgnore discoveryService.log
  SSLRequireSSL
</Directory>
````

Note: If using SSL is not an option (e.g. webserver behind a load balancer, with no SSL certificate installed on web server), comment out the SSLRequireSSL directive in the config file.

And finally, check the configuration for syntax errors and restart Apache:

    httpd -t
    service httpd graceful

# Importing DS session logs from remote sources

* Pre-requisites: install perl modules necessary for parser scripts (DateTime and MySQL):

        yum install perl-DateTime perl-DateTime-Format-HTTP perl-DateTime-Format-Builder perl-DBD-MySQL wget

* Create an account this would be running under:

        adduser logfetcher

* Create directory /opt/logfetcher owned by this account (and readable only to this account):

        mkdir /opt/logfetcher
        chown logfetcher.logfetcher /opt/logfetcher
        chmod 700 /opt/logfetcher

* Switch into this account (and continue with the rest of these instructions under this account):

        su - logfetcher

* Create /opt/logfetcher/bin and  install the following files from this directory there: 
  * pull-remote-logs.conf, pull-remote-logs.sh
  * Depending on the format of the logs, the correct parser:
    * For AAF/SWITCH DS: parse-aafds-logs.pl
    * For Tuakiri DS: parse-ds-logs.pl
    * For Internet2/Shibboleth Projet DS: parse-apache-logs.pl
    * For other DS implementations, it may be necessary to write your own parser
    * If using any of the above parsers, include also dbconfig.pm
* Make downloaded scripts executable:

        chmod +x /opt/logfetcher/bin/*.{pl,sh}

* Customzie dbconfig.pm with:
  * database connection details
  * settings to filter out spam sessions if needed (see commit e84594ead3aeabd8fdc3c4f3a42e1087a7ba352c from 2013-10-17)
* Customize pull-remote-logs.conf (detailed instructions in the sample config file):
  * set ERRMAILTO and MAILFROM to appropriate values for your federation
  * define log sources with LOG_SOURCE_IDS and LOG_SOURCE_<id>settings.
  * optionally, set WGET_OPTIONS_<id>="--no-check-certificate" if needed

* Run the script in dry-run mode and examine /opt/logfetcher/remote-logs/pull-remote-logs.log :

        /opt/logfetcher/bin/pull-remote-logs.sh --dry-run

* Run the script once in normal mode:

        /opt/logfetcher/bin/pull-remote-logs.sh

* Create a daily cronjob to run this script (ideally after log rotation happens at all sites): run "crontab -e" and add:

        01 7 * * * /opt/logfetcher/bin/pull-remote-logs.sh
