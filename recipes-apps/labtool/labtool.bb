SUMMARY = "Labtool application for AzureWave manufacturing mode tests."
LICENSE = "CLOSED"

SRC_URI = "\
    ${NXP_DOWNSTREAM_DRIVER_PKG_FILENAME} \
"
SRC_URI[sha256sum] = "${NXP_DOWNSTREAM_DRIVER_PKG_SHA1}"


S:sd-sd = "${WORKDIR}/src/sd-sd/mfgmode/labtool"
S:sd-uart = "${WORKDIR}/src/sd-uart/mfgmode/labtool"
S:pcie-usb = "${WORKDIR}/src/pcie-usb/mfgmode/labtool"

SRC_URI:append:sd-sd:mfg-mode = " \
    file://sd-sd/0001-Adapt-Makefile-for-yocto-build.patch \
"

SRC_URI:append:sd-uart:mfg-mode = " \
    file://sd-uart/0001-Adapt-Makefile-for-yocto-build.patch \
"

SRC_URI:append:pcie-usb:mfg-mode = " \
    file://pcie-usb/SetUp.ini \
    file://pcie-usb/0001-Adapt-makefile-for-yocto-build.patch \
    file://pcie-usb/0002-Bypass-problems-with-redefinition-of-min-and-max-std.patch \
    file://pcie-usb/0003-Remove-strip-from-the-build.patch \
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
