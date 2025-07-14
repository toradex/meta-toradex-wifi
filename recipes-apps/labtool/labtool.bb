SUMMARY = "Labtool application for AzureWave manufacturing mode tests."
LICENSE = "CLOSED"

SRC_URI = "\
    ${NXP_DOWNSTREAM_DRIVER_PKG_FILENAME} \
    file://0001-Adapt-makefile-for-yocto-build.patch \
    file://0002-Bypass-problems-with-redefinition-of-min-and-max-std.patch \
    file://0003-Remove-strip-from-the-build.patch \
"
SRC_URI[sha256sum] = "${NXP_DOWNSTREAM_DRIVER_PKG_SHA1}"

S = "${WORKDIR}/src/mfgmode/labtool"

SRC_URI:append:pcie-usb:mfg-mode = " \
    file://pcie-usb/SetUp.ini \
"

TARGET_CC_ARCH += "${LDFLAGS}"

FILES:${PN} = "${ROOT_HOME}/labtool ${ROOT_HOME}/SetUp.ini"

addtask labtool_sanity_check before do_fetch
python do_labtool_sanity_check() {
    if ("mfg-mode" not in d.getVar('OVERRIDES').split(":")):
        bb.fatal("Building the labtool recipe requires mfg-mode.")
}

do_compile() {
    oe_runmake -f MakeFile_W8997_FC18 -k -C DutApiWiFiBt
}

do_install() {
    install -d ${D}${ROOT_HOME}
    install -m 0644 ${B}/DutApiWiFiBt/SetUp.ini ${D}${ROOT_HOME}
    install -m 0755 ${B}/DutApiWiFiBt/labtool ${D}${ROOT_HOME}
}

do_install:pcie-usb:mfg-mode() {
    install -d ${D}${ROOT_HOME}
    install -m 0644 ${WORKDIR}/pcie-usb/SetUp.ini ${D}${ROOT_HOME}
    install -m 0755 ${B}/DutApiWiFiBt/labtool ${D}${ROOT_HOME}
}


DEPENDS += "bluez5"
