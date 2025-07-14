SUMMARY = "Toradex WiFI configuration."
DESCRIPTION = "Allow for blacklisting of \
either the mwifiex or NXP downstream driver \
based on the OVERRIDES settings."

inherit systemd

LICENSE = "CLOSED"

SRC_URI = " \
    file://${BPN}-mlan.conf \
    file://${BPN}-mwifiex.conf \
"

WIFI_CONFIG_FILE_SUFFIX="mwifiex"
WIFI_CONFIG_FILE_SUFFIX:default-nxp-downstream-driver="mlan"

do_install () {
	install -d ${D}${sysconfdir}/modprobe.d/
	install -m 0644 ${WORKDIR}/${PN}-${WIFI_CONFIG_FILE_SUFFIX}.conf ${D}${sysconfdir}/modprobe.d/${PN}.conf
}

FILES:${PN} = "${sysconfdir}/modprobe.d/${PN}.conf"
