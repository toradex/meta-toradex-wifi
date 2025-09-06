SUMMARY = "Kernel firmware for NXP Bluetooth chip"
LICENSE = "CLOSED"

FILES:${PN} = "${base_libdir}/firmware/nxp"

SRC_URI = "\
    ${NXP_DOWNSTREAM_DRIVER_PKG_FILENAME} \
"

SRC_URI[sha256sum] = "${NXP_DOWNSTREAM_DRIVER_PKG_SHA1}"

S:pcie-usb = "${WORKDIR}/src/pcie-usb/fw"
S:sd-sd = "${WORKDIR}/src/sd-sd/fw"
S:sd-uart = "${WORKDIR}/src/sd-uart/fw"

MFGMODE_FW_PATH:pcie-usb = "${WORKDIR}/src/pcie-usb/mfgmode/fw"
MFGMODE_FW_PATH:sd-sd = "${WORKDIR}/src/sd-sd/mfgmode/fw"
MFGMODE_FW_PATH:sd-uart = "${WORKDIR}/src/sd-uart/mfgmode/fw"

# firmware binaries are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${base_libdir}/firmware/nxp
    install -m 0644 ${S}/${FIRMWARE_BIN} ${D}${base_libdir}/firmware/nxp
}

do_install:append:mfgmode-fw () {
    install -m 0644 ${MFGMODE_FW_PATH}/${FIRMWARE_BIN_MFGMODE} ${D}${base_libdir}/firmware/nxp
}

do_install:sd-uart() {
    install -d ${D}${base_libdir}/firmware/nxp
    # necessary because the standard mwifiex driver installs a firmware with the same name
    # we're adding this firmware name on the modprobe configuration for the downstream driver
    NEW_FW_NAME=$(echo ${FIRMWARE_BIN}|sed 's/\.bin//')"_downstream.bin"
    install -m 0644 ${S}/${FIRMWARE_BIN} ${D}${base_libdir}/firmware/nxp/${NEW_FW_NAME}
}

COMPATIBLE_MACHINE = "(colibri-imx6ull|colibri-imx8x|verdin-imx8mm|verdin-imx8mp|verdin-am62|apalis-imx8)"

