#!/bin/bash

echo 'BBLAYERS += " ${TOPDIR}/../layers/meta-toradex-wifi"' >> $BBPATH/conf/bblayers.conf
echo 'INHERIT += "toradex-wifi-nxp-downstream-driver"' >> $BBPATH/conf/auto.conf


SHA256=$(sha256sum $1|awk '{print $1}')
PKGNAME=$(basename $1)

cat << EOF >> $BBPATH/conf/auto.conf

NXP_DOWNSTREAM_DRIVER_PKG_FILENAME = "file:///\${DL_DIR}/$PKGNAME"
NXP_DOWNSTREAM_DRIVER_PKG_SHA256 = "$SHA256"
EOF

echo 'MACHINEOVERRIDES =. "default-nxp-downstream-driver:"' >> $BBPATH/conf/local.conf
echo 'MACHINEOVERRIDES =. "mfg-mode:"' >> $BBPATH/conf/local.conf
