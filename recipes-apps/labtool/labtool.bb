SUMMARY = "Labtool application for AzureWave manufacturing mode tests."
LICENSE = "CLOSED"

SRC_URI = " \
    ${NXP_PROPRIETARY_DRIVER_LOCATION}/MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip;subdir=archive \
    file://0001-Makefiles-and-setup-changes-for-Yocto-and-cross-comp.patch \
"

ROOT_HOME="/home/root"

TARGET_CC_ARCH += "${LDFLAGS}"

FILES_${PN} = "${ROOT_HOME}/labtool ${ROOT_HOME}/SetUp.ini"

addtask labtool_sanity_check before do_fetch
python do_labtool_sanity_check() {
    if ("mfg-mode" not in d.getVar('OVERRIDES').split(":")):
        bb.fatal("Building the labtool recipe requires mfg-mode.")
}

addtask nxp_driver_unpack before do_patch after do_unpack
do_nxp_driver_unpack() {
    tar -C ${S} \
        --strip-components=2 \
        -xf ${WORKDIR}/archive/MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164/bin/labtool_app_1.1.0.164-A1/labtool_1.0.0.164-src.tgz
}

do_compile() {
    oe_runmake -f makefile.${HOST_ARCH} -k -C DutApiWiFiBt
}

do_install() {
    install -d ${D}${ROOT_HOME}
    install -m 0644 ${B}/DutApiWiFiBt/SetUp.ini ${D}${ROOT_HOME}
    install -m 0755 ${B}/DutApiWiFiBt/labtool ${D}${ROOT_HOME}
}

DEPENDS += "bluez5"
