export rcstring="-RC5"
export vstring="5.0.0"

gpg --armor --detach-sig weblogger-assembly/target/roller-weblogger-${vstring}${rcstring}-for-javaee.tar.gz
gpg --armor --detach-sig weblogger-assembly/target/roller-weblogger-${vstring}${rcstring}-for-javaee.zip
